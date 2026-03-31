package group.Finanztracker.service;

import group.Finanztracker.dto.CategoryRequest;
import group.Finanztracker.entity.Category;
import group.Finanztracker.mapper.CategoryMapper;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryBudgetRepository categoryBudgetRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(
                categoryRepository,
                categoryBudgetRepository,
                transactionRepository,
                new CategoryMapper()
        );
    }

    @Test
    void shouldRejectDeleteWhenTransactionsStillExist() {
        Category category = Category.builder().id(7L).name("Freizeit").build();
        when(categoryRepository.findById(7L)).thenReturn(Optional.of(category));
        when(transactionRepository.existsByCategory_Id(7L)).thenReturn(true);

        assertThatThrownBy(() -> categoryService.delete(7L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Transaktionen");

        verify(categoryRepository, never()).delete(category);
    }

    @Test
    void shouldRejectCreateForDuplicateNameIgnoringCase() {
        when(categoryRepository.findByNameIgnoreCase("lebensmittel"))
                .thenReturn(Optional.of(Category.builder().id(1L).name("Lebensmittel").build()));

        assertThatThrownBy(() -> categoryService.create(CategoryRequest.builder().name("lebensmittel").build()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("existiert bereits");
    }
}
