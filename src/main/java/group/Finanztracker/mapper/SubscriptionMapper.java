package group.Finanztracker.mapper;

import group.Finanztracker.dto.SubscriptionRequest;
import group.Finanztracker.dto.SubscriptionResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.Subscription;
import group.Finanztracker.entity.security.AppUser;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public Subscription toEntity(SubscriptionRequest request, Category category, AppUser user) {
        return Subscription.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .type(request.getType())
                .category(category)
                .interval(request.getInterval())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .note(request.getNote())
                .active(request.isActive())
                .user(user)
                .build();
    }

    public SubscriptionResponse toResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .title(subscription.getTitle())
                .amount(subscription.getAmount())
                .type(subscription.getType())
                .categoryId(subscription.getCategory().getId())
                .categoryName(subscription.getCategory().getName())
                .interval(subscription.getInterval())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .note(subscription.getNote())
                .active(subscription.isActive())
                .build();
    }

    public void updateEntity(Subscription subscription, SubscriptionRequest request, Category category) {
        subscription.setTitle(request.getTitle());
        subscription.setAmount(request.getAmount());
        subscription.setType(request.getType());
        subscription.setCategory(category);
        subscription.setInterval(request.getInterval());
        subscription.setStartDate(request.getStartDate());
        subscription.setEndDate(request.getEndDate());
        subscription.setNote(request.getNote());
        subscription.setActive(request.isActive());
    }
}
