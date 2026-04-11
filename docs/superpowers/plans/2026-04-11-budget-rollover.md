# Budget-Übertrag (Rollover) Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Nicht verbrauchtes Budget wird monatlich vorgetragen; Überschreitungen werden abgezogen. Das Feature ist pro Nutzer konfigurierbar.

**Architecture:** Zwei neue Spalten in `budget_settings` speichern Aktivierungsstatus und Startmonat. Der Übertrag wird dynamisch aus den gespeicherten Transaktionen berechnet. Dashboard zeigt Übertragsbetrag und effektives Budget zusätzlich zum Grundbudget.

**Tech Stack:** Spring Boot, Spring Data JPA, Thymeleaf, Flyway, Lombok, PostgreSQL

---

## File Map

| Aktion | Datei |
|---|---|
| Erstellen | `src/main/resources/db/migration/V7__add_rollover_to_budget_settings.sql` |
| Erstellen | `src/main/java/group/Finanztracker/dto/RolloverSettingsForm.java` |
| Ändern | `src/main/java/group/Finanztracker/entity/TotalBudget.java` |
| Ändern | `src/main/java/group/Finanztracker/dto/TotalBudgetResponse.java` |
| Ändern | `src/main/java/group/Finanztracker/dto/MonthlyOverviewResponse.java` |
| Ändern | `src/main/java/group/Finanztracker/mapper/TotalBudgetMapper.java` |
| Ändern | `src/main/java/group/Finanztracker/service/TotalBudgetService.java` |
| Ändern | `src/main/java/group/Finanztracker/service/MonthlyOverviewService.java` |
| Ändern | `src/main/java/group/Finanztracker/controller/SettingsPageController.java` |
| Ändern | `src/main/resources/templates/settings.html` |
| Ändern | `src/main/resources/templates/dashboard.html` |

---

## Task 1: Flyway-Migration V7

**Files:**
- Create: `src/main/resources/db/migration/V7__add_rollover_to_budget_settings.sql`

- [ ] **Datei erstellen**

```sql
ALTER TABLE budget_settings
    ADD COLUMN rollover_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN rollover_start_month DATE;
```

---

## Task 2: TotalBudget Entity erweitern

**Files:**
- Modify: `src/main/java/group/Finanztracker/entity/TotalBudget.java`

- [ ] **Zwei neue Felder hinzufügen**

Vollständige Datei nach Änderung:

```java
package group.Finanztracker.entity;

import group.Finanztracker.entity.security.AppUser;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "budget_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_monthly_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalMonthlyLimit;

    @Column(name = "rollover_enabled", nullable = false)
    private boolean rolloverEnabled;

    @Column(name = "rollover_start_month")
    private LocalDate rolloverStartMonth;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;
}
```

---

## Task 3: TotalBudgetResponse und Mapper aktualisieren

**Files:**
- Modify: `src/main/java/group/Finanztracker/dto/TotalBudgetResponse.java`
- Modify: `src/main/java/group/Finanztracker/mapper/TotalBudgetMapper.java`

- [ ] **TotalBudgetResponse — rollover-Felder ergänzen**

```java
package group.Finanztracker.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalBudgetResponse {
    private Long id;
    private BigDecimal totalMonthlyLimit;
    private boolean rolloverEnabled;
    private LocalDate rolloverStartMonth;
}
```

- [ ] **TotalBudgetMapper — toResponse aktualisieren**

```java
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
```

---

## Task 4: RolloverSettingsForm DTO erstellen

**Files:**
- Create: `src/main/java/group/Finanztracker/dto/RolloverSettingsForm.java`

- [ ] **DTO erstellen**

```java
package group.Finanztracker.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolloverSettingsForm {
    private boolean rolloverEnabled;
    // Format: "yyyy-MM" — kommt von <input type="month">
    private String rolloverStartMonth;
}
```

---

## Task 5: MonthlyOverviewResponse erweitern

**Files:**
- Modify: `src/main/java/group/Finanztracker/dto/MonthlyOverviewResponse.java`

- [ ] **rolloverAmount und effectiveBudget ergänzen**

```java
package group.Finanztracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyOverviewResponse {

    private YearMonth month;
    private BigDecimal totalBudget;
    private BigDecimal totalSpent;
    private BigDecimal remainingBudget;
    private boolean overBudget;
    private BigDecimal totalIncome;
    private BigDecimal configuredCategoryBudgetSum;
    private boolean categoryBudgetSumExceedsTotalBudget;
    private List<MonthlyCategorySummaryResponse> categories;

    // Rollover-Felder — 0 wenn Feature deaktiviert
    private BigDecimal rolloverAmount;
    // max(0, totalBudget + rolloverAmount) — gleich totalBudget wenn deaktiviert
    private BigDecimal effectiveBudget;
    private boolean rolloverEnabled;
}
```

---

## Task 6: TotalBudgetService — Rollover-Einstellungen speichern und lesen

**Files:**
- Modify: `src/main/java/group/Finanztracker/service/TotalBudgetService.java`

- [ ] **getRolloverSettings und saveRolloverSettings hinzufügen**

Vollständige Datei nach Änderung:

```java
package group.Finanztracker.service;

import group.Finanztracker.dto.RolloverSettingsForm;
import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.TotalBudgetMapper;
import group.Finanztracker.repository.TotalBudgetRepository;
import group.Finanztracker.repository.security.AppUserRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TotalBudgetService {

    private final TotalBudgetRepository totalBudgetRepository;
    private final TotalBudgetMapper totalBudgetMapper;
    private final CurrentUserService currentUserService;
    private final AppUserRepository appUserRepository;

    public Optional<TotalBudgetResponse> getCurrentBudget() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(totalBudgetMapper::toResponse);
    }

    public BigDecimal getCurrentBudgetLimit() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(TotalBudget::getTotalMonthlyLimit)
                .orElse(BigDecimal.ZERO);
    }

    public Optional<RolloverSettingsForm> getRolloverSettings() {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(b -> {
                    String startMonth = b.getRolloverStartMonth() != null
                            ? YearMonth.from(b.getRolloverStartMonth()).format(DateTimeFormatter.ofPattern("yyyy-MM"))
                            : "";
                    return RolloverSettingsForm.builder()
                            .rolloverEnabled(b.isRolloverEnabled())
                            .rolloverStartMonth(startMonth)
                            .build();
                });
    }

    @Transactional
    public TotalBudgetResponse saveOrUpdate(TotalBudgetRequest request) {
        return totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .map(existing -> update(existing.getId(), request))
                .orElseGet(() -> create(request));
    }

    @Transactional
    public void saveRolloverSettings(RolloverSettingsForm form) {
        TotalBudget budget = totalBudgetRepository.findFirstByUser_IdOrderByIdAsc(currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Kein Gesamtbudget konfiguriert"));
        budget.setRolloverEnabled(form.isRolloverEnabled());
        if (form.isRolloverEnabled()
                && form.getRolloverStartMonth() != null
                && !form.getRolloverStartMonth().isBlank()) {
            LocalDate startDate = YearMonth.parse(form.getRolloverStartMonth()).atDay(1);
            budget.setRolloverStartMonth(startDate);
        }
        totalBudgetRepository.save(budget);
    }

    private TotalBudgetResponse create(TotalBudgetRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        TotalBudget entity = totalBudgetMapper.toEntity(
                request,
                appUserRepository.findById(userId)
                        .orElseThrow(() -> new ResourceNotFoundException("Benutzer nicht gefunden"))
        );
        entity = totalBudgetRepository.save(entity);
        return totalBudgetMapper.toResponse(entity);
    }

    private TotalBudgetResponse update(Long id, TotalBudgetRequest request) {
        TotalBudget entity = totalBudgetRepository.findByIdAndUser_Id(id, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Gesamtbudget mit ID " + id + " nicht gefunden"));
        totalBudgetMapper.updateEntity(entity, request);
        entity = totalBudgetRepository.save(entity);
        return totalBudgetMapper.toResponse(entity);
    }
}
```

---

## Task 7: MonthlyOverviewService — Rollover berechnen

**Files:**
- Modify: `src/main/java/group/Finanztracker/service/MonthlyOverviewService.java`

- [ ] **getOverview aktualisieren und calculateRollover hinzufügen**

Vollständige Datei nach Änderung:

```java
package group.Finanztracker.service;

import group.Finanztracker.dto.MonthlyCategorySummaryResponse;
import group.Finanztracker.dto.MonthlyOverviewResponse;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.Category;
import group.Finanztracker.entity.CategoryBudget;
import group.Finanztracker.repository.CategoryBudgetRepository;
import group.Finanztracker.repository.CategoryRepository;
import group.Finanztracker.service.security.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonthlyOverviewService {

    private final CategoryRepository categoryRepository;
    private final CategoryBudgetRepository categoryBudgetRepository;
    private final TransactionService transactionService;
    private final TotalBudgetService totalBudgetService;
    private final CurrentUserService currentUserService;

    public MonthlyOverviewResponse getOverview(YearMonth month) {
        Long userId = currentUserService.getCurrentUserId();

        Optional<TotalBudgetResponse> budgetOpt = totalBudgetService.getCurrentBudget();
        BigDecimal totalBudget = budgetOpt.map(TotalBudgetResponse::getTotalMonthlyLimit).orElse(BigDecimal.ZERO);
        BigDecimal totalSpent = transactionService.sumExpensesForMonth(month);
        BigDecimal totalIncome = transactionService.sumIncomeForMonth(month);

        BigDecimal rolloverAmount = budgetOpt.map(b -> calculateRollover(b, month)).orElse(BigDecimal.ZERO);
        boolean rolloverEnabled = budgetOpt.map(TotalBudgetResponse::isRolloverEnabled).orElse(false);
        BigDecimal effectiveBudget = totalBudget.add(rolloverAmount).max(BigDecimal.ZERO);

        List<CategoryBudget> allBudgets = categoryBudgetRepository.findAllByCategory_User_Id(userId);

        Map<Long, CategoryBudget> budgetsByCategoryId = new HashMap<>();
        for (CategoryBudget budget : allBudgets) {
            budgetsByCategoryId.put(budget.getCategory().getId(), budget);
        }

        BigDecimal configuredCategoryBudgetSum = allBudgets.stream()
                .map(CategoryBudget::getMonthlyLimit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MonthlyCategorySummaryResponse> categorySummaries = categoryRepository.findAllByUser_IdOrderByNameAsc(userId).stream()
                .map(category -> buildCategorySummary(category, budgetsByCategoryId.get(category.getId()), month))
                .toList();

        return MonthlyOverviewResponse.builder()
                .month(month)
                .totalBudget(totalBudget)
                .totalSpent(totalSpent)
                .remainingBudget(totalBudget.subtract(totalSpent))
                .overBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0 && totalSpent.compareTo(totalBudget) > 0)
                .totalIncome(totalIncome)
                .configuredCategoryBudgetSum(configuredCategoryBudgetSum)
                .categoryBudgetSumExceedsTotalBudget(totalBudget.compareTo(BigDecimal.ZERO) > 0
                        && configuredCategoryBudgetSum.compareTo(totalBudget) > 0)
                .categories(categorySummaries)
                .rolloverAmount(rolloverAmount)
                .effectiveBudget(effectiveBudget)
                .rolloverEnabled(rolloverEnabled)
                .build();
    }

    private BigDecimal calculateRollover(TotalBudgetResponse budget, YearMonth currentMonth) {
        if (!budget.isRolloverEnabled() || budget.getRolloverStartMonth() == null) {
            return BigDecimal.ZERO;
        }
        YearMonth startMonth = YearMonth.from(budget.getRolloverStartMonth());
        if (!startMonth.isBefore(currentMonth)) {
            return BigDecimal.ZERO;
        }
        BigDecimal rollover = BigDecimal.ZERO;
        YearMonth m = startMonth;
        while (m.isBefore(currentMonth)) {
            BigDecimal spent = transactionService.sumExpensesForMonth(m);
            rollover = rollover.add(budget.getTotalMonthlyLimit().subtract(spent));
            m = m.plusMonths(1);
        }
        return rollover;
    }

    private MonthlyCategorySummaryResponse buildCategorySummary(Category category, CategoryBudget budget, YearMonth month) {
        BigDecimal spent = transactionService.sumExpensesForCategoryAndMonth(category.getId(), month);
        BigDecimal limit = budget != null ? budget.getMonthlyLimit() : null;
        BigDecimal remaining = limit != null ? limit.subtract(spent) : null;
        return MonthlyCategorySummaryResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .monthlyLimit(limit)
                .spent(spent)
                .remaining(remaining)
                .overLimit(limit != null && spent.compareTo(limit) > 0)
                .hasBudget(limit != null)
                .build();
    }
}
```

---

## Task 8: SettingsPageController erweitern

**Files:**
- Modify: `src/main/java/group/Finanztracker/controller/SettingsPageController.java`

- [ ] **GET-Handler mit Rollover-Daten befüllen, POST-Handler für /settings/rollover hinzufügen**

```java
package group.Finanztracker.controller;

import group.Finanztracker.dto.RolloverSettingsForm;
import group.Finanztracker.service.AccountService;
import group.Finanztracker.service.TotalBudgetService;
import group.Finanztracker.service.security.CurrentUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
@RequiredArgsConstructor
public class SettingsPageController {

    private final AccountService accountService;
    private final CurrentUserService currentUserService;
    private final TotalBudgetService totalBudgetService;

    @GetMapping
    public String settingsPage(Model model) {
        totalBudgetService.getRolloverSettings().ifPresentOrElse(
                form -> {
                    model.addAttribute("rolloverSettings", form);
                    model.addAttribute("budgetExists", true);
                },
                () -> model.addAttribute("budgetExists", false)
        );
        return "settings";
    }

    @PostMapping("/rollover")
    public String saveRolloverSettings(@ModelAttribute RolloverSettingsForm form) {
        totalBudgetService.saveRolloverSettings(form);
        return "redirect:/settings?rolloverSaved=true";
    }

    @PostMapping("/delete")
    public String deleteAccount(HttpSession session) {
        Long userId = currentUserService.getCurrentUserId();
        accountService.deleteAccount(userId);
        session.invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?accountDeleted=true";
    }
}
```

---

## Task 9: settings.html — Rollover-Card hinzufügen

**Files:**
- Modify: `src/main/resources/templates/settings.html`

- [ ] **Neue Card nach der "Konto"-Card einfügen**

Den Block `<main class="page single-column">` ersetzen:

```html
<main class="page single-column">
    <section class="card narrow">
        <h2>Konto</h2>

        <p class="account-email" th:text="${currentUserEmail}">user@example.com</p>

        <form method="post" th:action="@{/auth/logout}" class="stack-form">
            <button type="submit" class="button-link secondary logout-button">Abmelden</button>
        </form>

        <div class="stack-form">
            <button type="button" class="danger-button"
                    onclick="document.getElementById('deleteModal').removeAttribute('hidden')">
                Konto löschen
            </button>
        </div>
    </section>

    <section class="card narrow" th:if="${budgetExists}">
        <h2>Budget-Übertrag</h2>
        <p class="message success" th:if="${param.rolloverSaved}">Einstellungen gespeichert.</p>

        <form method="post" th:action="@{/settings/rollover}" th:object="${rolloverSettings}" class="stack-form">
            <div class="form-row">
                <label for="rolloverEnabled">Übertrag aktivieren</label>
                <input type="checkbox" id="rolloverEnabled" name="rolloverEnabled"
                       th:checked="${rolloverSettings.rolloverEnabled}"
                       onchange="document.getElementById('rolloverStartMonthGroup').hidden = !this.checked">
            </div>

            <div id="rolloverStartMonthGroup"
                 th:attr="hidden=${!rolloverSettings.rolloverEnabled} ? 'hidden' : null">
                <label for="rolloverStartMonth">Übertrag berechnen ab</label>
                <input type="month" id="rolloverStartMonth" name="rolloverStartMonth"
                       th:value="${rolloverSettings.rolloverStartMonth}">
            </div>

            <button type="submit">Speichern</button>
        </form>
    </section>

    <section class="card narrow" th:if="${!budgetExists}">
        <h2>Budget-Übertrag</h2>
        <p>Richte zuerst ein <a th:href="@{/budgets}">Gesamtbudget</a> ein, um den Übertrag zu konfigurieren.</p>
    </section>
</main>
```

---

## Task 10: dashboard.html — Rollover-Metriken hinzufügen

**Files:**
- Modify: `src/main/resources/templates/dashboard.html`

- [ ] **Rollover-Block nach den bestehenden metric-cards einfügen**

Den Block `<section class="metrics">` ersetzen:

```html
    <section class="metrics">
        <article class="metric-card">
            <span>Gesamtbudget</span>
            <strong th:text="${#numbers.formatDecimal(overview.totalBudget, 1, 'POINT', 2, 'COMMA')} + ' EUR'">0,00 EUR</strong>
        </article>
        <article class="metric-card">
            <span>Ausgaben</span>
            <strong th:text="${#numbers.formatDecimal(overview.totalSpent, 1, 'POINT', 2, 'COMMA')} + ' EUR'">0,00 EUR</strong>
        </article>
        <article class="metric-card" th:classappend="${overview.overBudget} ? ' danger' : ' success'">
            <span>Restbudget</span>
            <strong th:text="${#numbers.formatDecimal(overview.remainingBudget, 1, 'POINT', 2, 'COMMA')} + ' EUR'">0,00 EUR</strong>
        </article>
        <article class="metric-card">
            <span>Einnahmen</span>
            <strong th:text="${#numbers.formatDecimal(overview.totalIncome, 1, 'POINT', 2, 'COMMA')} + ' EUR'">0,00 EUR</strong>
        </article>
    </section>

    <section class="metrics" th:if="${overview.rolloverEnabled}">
        <article class="metric-card"
                 th:classappend="${overview.rolloverAmount.compareTo(T(java.math.BigDecimal).ZERO) >= 0} ? ' success' : ' danger'">
            <span>Übertrag</span>
            <strong th:text="${(overview.rolloverAmount.compareTo(T(java.math.BigDecimal).ZERO) >= 0 ? '+' : '') + #numbers.formatDecimal(overview.rolloverAmount, 1, 'POINT', 2, 'COMMA')} + ' EUR'">+0,00 EUR</strong>
        </article>
        <article class="metric-card">
            <span>Effektives Budget</span>
            <strong th:text="${#numbers.formatDecimal(overview.effectiveBudget, 1, 'POINT', 2, 'COMMA')} + ' EUR'">0,00 EUR</strong>
        </article>
    </section>
```

---

## Reihenfolge der Commits (Vorschlag)

1. `feat(db): add rollover columns to budget_settings (V7)`
2. `feat(entity): add rollover fields to TotalBudget`
3. `feat(dto): add rollover fields to TotalBudgetResponse and MonthlyOverviewResponse`
4. `feat(dto): add RolloverSettingsForm`
5. `feat(mapper): map rollover fields in TotalBudgetMapper`
6. `feat(service): add rollover calculation and settings methods`
7. `feat(controller): add rollover settings endpoint`
8. `feat(ui): add rollover settings card to settings page`
9. `feat(ui): add rollover metrics to dashboard`
