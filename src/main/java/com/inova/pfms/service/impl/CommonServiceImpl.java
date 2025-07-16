package com.inova.pfms.service.impl;

import com.inova.pfms.entity.Budget;
import com.inova.pfms.entity.User;
import com.inova.pfms.exception.ObjectDoesNotExistException;
import com.inova.pfms.repository.BudgetRepository;
import com.inova.pfms.service.CommonService;
import com.inova.pfms.util.UserUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.List;

@Service
public class CommonServiceImpl implements CommonService {

    private final BudgetRepository budgetRepository;
    private final UserUtil userUtil;

    public CommonServiceImpl(BudgetRepository budgetRepository, UserUtil userUtil) {
        this.budgetRepository = budgetRepository;
        this.userUtil = userUtil;
    }

    public Budget getActiveBudget() {
        User loggedInUser = userUtil.getLoggedInUser();
        Date currentDate = new Date(System.currentTimeMillis());

        List<Budget> existingBudgetList = budgetRepository.findByUserIdAndDeletedFalseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(loggedInUser.getId(), currentDate, currentDate);
        if (CollectionUtils.isEmpty(existingBudgetList)) {
            throw new ObjectDoesNotExistException("No active budget found for today", currentDate.toString());
        }
        return existingBudgetList.get(0);
    }

}
