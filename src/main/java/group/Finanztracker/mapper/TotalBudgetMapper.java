package group.Finanztracker.mapper;

import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.entity.security.AppUser;
import org.springframework.stereotype.Component;

@Component
public class TotalBudgetMapper {

    public TotalBudget toEntity(TotalBudgetRequest request, AppUser user) {
        return TotalBudget.builder()
                .totalMonthlyLimit(request.getTotalMonthlyLimit())
                .rolloverEnabled(false)
                .user(user)
                .build();
    }

    public TotalBudgetResponse toResponse(TotalBudget entity) {
        return TotalBudgetResponse.builder()
                .id(entity.getId())
                .totalMonthlyLimit(entity.getTotalMonthlyLimit())
                .rolloverEnabled(entity.isRolloverEnabled())
                .rolloverStartMonth(entity.getRolloverStartMonth())
                .build();
    }

    public void updateEntity(TotalBudget entity, TotalBudgetRequest request) {
        entity.setTotalMonthlyLimit(request.getTotalMonthlyLimit());
    }
}
