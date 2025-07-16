package com.inova.pfms.repository.impl;

import com.inova.pfms.entity.DeadLetters;
import com.inova.pfms.repository.DeadLetterCustomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.inova.pfms.constants.LogMessages.SEARCH_DEAD_LETTER_EVENT_WITH_CRITERIA;

@Repository
@Slf4j
public class DeadLetterCustomRepositoryImpl implements DeadLetterCustomRepository {

    private static final String CREATED_AT = "createdAt";


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<DeadLetters> search(String eventType, Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DeadLetters> cq = cb.createQuery(DeadLetters.class);
        Root<DeadLetters> root = cq.from(DeadLetters.class);

        List<Predicate> predicates = new ArrayList<>();

        if (eventType != null && !eventType.isBlank()) {
            predicates.add(cb.equal(root.get("eventName"), eventType));
        }

        if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(CREATED_AT), startDate));
        }

        if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(CREATED_AT), endDate));
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.desc(root.get(CREATED_AT)));

        log.info(SEARCH_DEAD_LETTER_EVENT_WITH_CRITERIA, eventType, startDate, endDate);
        return entityManager.createQuery(cq).getResultList();
    }
}

