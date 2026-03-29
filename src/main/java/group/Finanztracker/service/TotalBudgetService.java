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

import java.util.List;

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
        if (!totalBudgetRepository.findAll().isEmpty()) {
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
}
