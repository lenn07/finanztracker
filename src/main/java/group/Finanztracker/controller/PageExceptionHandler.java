package group.Finanztracker.controller;

import group.Finanztracker.exception.ResourceNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes = {
        HomeController.class,
        DashboardPageController.class,
        TransactionPageController.class,
        CategoryPageController.class,
        BudgetPageController.class
})
public class PageExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, ResourceNotFoundException.class})
    public String handlePageExceptions(RuntimeException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-page";
    }
}
