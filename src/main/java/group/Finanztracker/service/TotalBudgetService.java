package group.Finanztracker.service;

import group.Finanztracker.dto.TotalBudgetRequest;
import group.Finanztracker.dto.TotalBudgetResponse;
import group.Finanztracker.entity.TotalBudget;
import group.Finanztracker.exception.ResourceNotFoundException;
import group.Finanztracker.mapper.TotalBudgetMapper;
import group.Finanztracker.repository.TotalBudgetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TotalBudgetService {

	private final TotalBudgetRepository totalBudgetRepository;
	private final TotalBudgetMapper totalBudgetMapper;

	@Transactional(readOnly = true)
	public List<TotalBudgetResponse> getAll() {
		return totalBudgetRepository.findAll().stream()
				.map(totalBudgetMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public TotalBudgetResponse getById(Long id) {
		TotalBudget entity = totalBudgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TotalBudget not found with id: " + id));
		return totalBudgetMapper.toResponse(entity);
	}

	@Transactional
	public TotalBudgetResponse create(TotalBudgetRequest request) {
        if (totalBudgetRepository.findFirstByOrderByIdAsc().isPresent()) {
            throw new IllegalStateException("Es existiert bereits ein Gesamtbudget. Mehrere Gesamtbudgets sind nicht erlaubt.");
        }
        TotalBudget entity = totalBudgetMapper.toEntity(request);
        entity = totalBudgetRepository.save(entity);
        return totalBudgetMapper.toResponse(entity);
	}

	@Transactional
	public TotalBudgetResponse update(Long id, TotalBudgetRequest request) {
		TotalBudget entity = totalBudgetRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("TotalBudget not found with id: " + id));
		totalBudgetMapper.updateEntity(entity, request);
		entity = totalBudgetRepository.save(entity);
		return totalBudgetMapper.toResponse(entity);
	}

	@Transactional
	public void delete(Long id) {
		if (!totalBudgetRepository.existsById(id)) {
			throw new ResourceNotFoundException("TotalBudget not found with id: " + id);
		}
		totalBudgetRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Optional<TotalBudgetResponse> getCurrentBudget() {
		return totalBudgetRepository.findFirstByOrderByIdAsc()
				.map(totalBudgetMapper::toResponse);
	}

	@Transactional(readOnly = true)
	public BigDecimal getCurrentBudgetLimit() {
		return totalBudgetRepository.findFirstByOrderByIdAsc()
				.map(TotalBudget::getTotalMonthlyLimit)
				.orElse(BigDecimal.ZERO);
	}

	@Transactional
	public TotalBudgetResponse saveOrUpdate(TotalBudgetRequest request) {
		return totalBudgetRepository.findFirstByOrderByIdAsc()
				.map(existing -> update(existing.getId(), request))
				.orElseGet(() -> create(request));
	}
}
