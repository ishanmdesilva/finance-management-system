package com.inova.pfms.service.impl;

import com.inova.pfms.dto.response.DeadLetterEventResponseDTO;
import com.inova.pfms.dto.response.EventResponseDTO;
import com.inova.pfms.dto.response.SuccessResponseDTO;
import com.inova.pfms.entity.DeadLetters;
import com.inova.pfms.entity.User;
import com.inova.pfms.enums.EventEnum;
import com.inova.pfms.exception.ObjectCreationFailedException;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.exception.ObjectSearchFailedException;
import com.inova.pfms.kafka.event.ExpenseEvent;
import com.inova.pfms.kafka.event.IncomeEvent;
import com.inova.pfms.repository.DeadLettersRepository;
import com.inova.pfms.repository.UserRepository;
import com.inova.pfms.service.DeadLetterEventService;
import com.inova.pfms.util.CommonMethod;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import static com.inova.pfms.constants.LogMessages.EVENT_SEARCH_CALLED;
import static com.inova.pfms.constants.LogMessages.EVENT_SEARCH_COMPLETED;
import static com.inova.pfms.constants.LogMessages.EXPENSE_EVENT_ERROR_HANDLER_CALLED;
import static com.inova.pfms.constants.LogMessages.EXPENSE_EVENT_ERROR_HANDLER_COMPLETED;
import static com.inova.pfms.constants.LogMessages.FIND_ALL_EVENT_TYPES_CALLED;
import static com.inova.pfms.constants.LogMessages.FIND_ALL_EVENT_TYPES_COMPLETED;
import static com.inova.pfms.constants.LogMessages.INCOME_EVENT_ERROR_HANDLER_CALLED;
import static com.inova.pfms.constants.LogMessages.INCOME_EVENT_ERROR_HANDLER_COMPLETED;
import static com.inova.pfms.constants.LogMessages.USER_NOT_FOUND;

@Service
@Slf4j
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class DeadLetterEventServiceImpl implements DeadLetterEventService {

    private final DeadLettersRepository deadLettersRepository;
    private final UserRepository userRepository;

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public SuccessResponseDTO findAllEventTypes() {
        try {
            log.info(FIND_ALL_EVENT_TYPES_CALLED);
            List<EventResponseDTO> responseList = Arrays.stream(EventEnum.values())
                    .map(event -> new EventResponseDTO(event.getName(), event.getCode()))
                    .toList();

            log.info(FIND_ALL_EVENT_TYPES_COMPLETED);

            return CommonMethod.getSuccessResponse(
                    "dead letter event types retrieved successfully",
                    responseList
            );
        } catch (Exception e) {
            throw new ObjectDoesNotExistException("Failed to find dead letter event types ", e.getMessage());
        }
    }

    @Override
    @Transactional
    public void handleIncomeEventError(String message) {
        try {
            log.info(INCOME_EVENT_ERROR_HANDLER_CALLED);
            IncomeEvent incomeEvent = CommonMethod.getObjectMapper().readValue(message, IncomeEvent.class);
            User user = userRepository.findById(incomeEvent.getUserId())
                    .orElseThrow(() -> new ObjectDoesNotExistException(incomeEvent.getUserId(), USER_NOT_FOUND));
            /*-- dead_letter [CREATE] --*/
            DeadLetters deadLetters = DeadLetters.builder()
                    .id(UUID.randomUUID().toString())
                    .eventName(EventEnum.INCOME_EVENT.getCode())
                    .failedMessage(message)
                    .createdBy(user)
                    .createdAt(incomeEvent.getDate())
                    .build();

            deadLettersRepository.save(deadLetters);
            log.info(INCOME_EVENT_ERROR_HANDLER_COMPLETED, incomeEvent.getIncomeId());
        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to save income error event details");
        }
    }

    @Override
    @Transactional
    public void handleExpenseEventError(String message) {
        try {
            log.info(EXPENSE_EVENT_ERROR_HANDLER_CALLED);
            ExpenseEvent expenseEvent = CommonMethod.getObjectMapper().readValue(message, ExpenseEvent.class);
            User user = userRepository.findById(expenseEvent.getUserId())
                    .orElseThrow(() -> new ObjectDoesNotExistException(expenseEvent.getUserId(), USER_NOT_FOUND));
            /*-- dead_letter [CREATE] --*/
            DeadLetters deadLetters = DeadLetters.builder()
                    .id(UUID.randomUUID().toString())
                    .eventName(EventEnum.EXPENSE_EVENT.getCode())
                    .failedMessage(message)
                    .createdBy(user)
                    .createdAt(expenseEvent.getDate())
                    .build();

            deadLettersRepository.save(deadLetters);
            log.info(EXPENSE_EVENT_ERROR_HANDLER_COMPLETED, expenseEvent.getExpenseId());
        } catch (Exception e) {
            throw new ObjectCreationFailedException("Failed to save expense error event details");
        }
    }

    @Override
    @Transactional
    public SuccessResponseDTO searchEvent(Map<String, Object> searchParams) {
        try {

            log.info(EVENT_SEARCH_CALLED);

            List<DeadLetters> deadLetters;
            if (Boolean.TRUE.equals(searchParams.get("all"))) {
                deadLetters = deadLettersRepository.findAll();
            } else {
                Date startDate = null, endDate = null;
                if (searchParams.get("startDate") != null)
                    startDate = formatter.parse(searchParams.get("startDate").toString());
                if (searchParams.get("endDate") != null)
                    endDate = formatter.parse(searchParams.get("endDate").toString());

                String eventType = searchParams.get("eventType") != null ? searchParams.get("eventType").toString() : "";
                deadLetters = deadLettersRepository.search(eventType, startDate, endDate);
            }

            List<DeadLetterEventResponseDTO> responseList = deadLetters.stream()
                    .map(this::convertToDeadLetterEventResponseDTO)
                    .toList();

            log.info(EVENT_SEARCH_COMPLETED);

            return CommonMethod.getSuccessResponse(
                    "search dead letter events successfully",
                    responseList
            );
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd.");
        } catch (Exception e) {
            throw new ObjectSearchFailedException("Failed to search dead letter events");
        }
    }


    private DeadLetterEventResponseDTO convertToDeadLetterEventResponseDTO(DeadLetters deadLetters) {
        return DeadLetterEventResponseDTO.builder()
                .id(deadLetters.getId())
                .eventType(deadLetters.getEventName())
                .data(deadLetters.getFailedMessage())
                .createdAt(deadLetters.getCreatedAt())
                .createdUserEmail(deadLetters.getCreatedBy().getEmail())
                .createdUserFname(deadLetters.getCreatedBy().getFirstName())
                .createdUserLname(deadLetters.getCreatedBy().getLastName())
                .build();
    }
}
