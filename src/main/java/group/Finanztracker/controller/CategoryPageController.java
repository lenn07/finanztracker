package group.Finanztracker.controller;

import group.Finanztracker.dto.CategoryRequest;
import group.Finanztracker.dto.CategoryResponse;
import group.Finanztracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryPageController {

    private final CategoryService categoryService;

    @GetMapping
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("categoryForm", new CategoryRequest());
        return "categories";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("categoryForm") CategoryRequest request,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "categories";
        }
        categoryService.create(request);
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie wurde angelegt.");
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id, Model model) {
        CategoryResponse category = categoryService.findById(id);
        model.addAttribute("categoryId", id);
        model.addAttribute("categoryForm", CategoryRequest.builder().name(category.getName()).build());
        return "category-edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("categoryForm") CategoryRequest request,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryId", id);
            return "category-edit";
        }
        categoryService.update(id, request);
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie wurde aktualisiert.");
        return "redirect:/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        categoryService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Kategorie wurde gelöscht.");
        return "redirect:/categories";
    }
}
