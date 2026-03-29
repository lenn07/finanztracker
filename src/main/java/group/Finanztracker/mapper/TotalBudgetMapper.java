package group.Finanztracker.mapper;

import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import org.springframework.stereotype.Component;

@Component
public class TotalBudgetMapper {

    public TotalBudget toEntity(TotalBudgetRequest request) {
        return TotalBudget.builder()
                .totalMonthlyLimit(request.getTotalMonthlyLimit())
                .build();
    }

    public TotalBudgetResponse toResponse(TotalBudget entity) {
        return TotalBudgetResponse.builder()
                .id(entity.getId())
                .totalMonthlyLimit(entity.getTotalMonthlyLimit())
                .build();
    }

    public void updateEntity(TotalBudget entity, TotalBudgetRequest request) {
        entity.setTotalMonthlyLimit(request.getTotalMonthlyLimit());
    }
}