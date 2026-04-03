package group.Finanztracker.mapper;

import group.Finanztracker.dto.TransactionRequest;
import group.Finanztracker.dto.TransactionResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequest request, Category category) {
        return Transaction.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .type(request.getType())
                .category(category)
                .date(request.getDate())
                .note(request.getNote())
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .title(transaction.getTitle())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .subscriptionId(transaction.getSubscription() != null ? transaction.getSubscription().getId() : null)
                .generatedFromSubscription(transaction.getSubscription() != null)
                .date(transaction.getDate())
                .note(transaction.getNote())
                .build();
    }

    public void updateEntity(Transaction transaction, TransactionRequest request, Category category) {
        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(category);
        transaction.setDate(request.getDate());
        transaction.setNote(request.getNote());
    }
}
