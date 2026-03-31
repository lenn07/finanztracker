package group.Finanztracker.controller;

import group.Finanztracker.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleNotFound(ResourceNotFoundException ex) {
        return buildErrorPage(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ModelAndView handleIllegalState(IllegalStateException ex) {
        return buildErrorPage(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ModelAndView buildErrorPage(String message, HttpStatus status) {
        ModelAndView modelAndView = new ModelAndView("error-page");
        modelAndView.setStatus(status);
        modelAndView.addObject("errorMessage", message);
        return modelAndView;
    }
}
