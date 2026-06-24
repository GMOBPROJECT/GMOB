package br.com.gmob.corretor.api;

import br.com.gmob.corretor.api.dto.CreateCorretorRequest;
import br.com.gmob.corretor.api.dto.CorretorListResponse;
import br.com.gmob.corretor.api.dto.CorretorResponse;
import br.com.gmob.corretor.api.dto.UpdateCorretorRequest;
import br.com.gmob.corretor.application.CreateCorretorUseCase;
import br.com.gmob.corretor.application.DeleteCorretorUseCase;
import br.com.gmob.corretor.application.FindCorretorUseCase;
import br.com.gmob.corretor.application.ListCorretoresUseCase;
import br.com.gmob.corretor.application.UpdateCorretorUseCase;
import br.com.gmob.infra.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/corretor")
public class CorretorController {

    private final CreateCorretorUseCase createCorretorUseCase;
    private final ListCorretoresUseCase listCorretoresUseCase;
    private final FindCorretorUseCase findCorretorUseCase;
    private final UpdateCorretorUseCase updateCorretorUseCase;
    private final DeleteCorretorUseCase deleteCorretorUseCase;

    public CorretorController(
            CreateCorretorUseCase createCorretorUseCase,
            ListCorretoresUseCase listCorretoresUseCase,
            FindCorretorUseCase findCorretorUseCase,
            UpdateCorretorUseCase updateCorretorUseCase,
            DeleteCorretorUseCase deleteCorretorUseCase
    ) {
        this.createCorretorUseCase = createCorretorUseCase;
        this.listCorretoresUseCase = listCorretoresUseCase;
        this.findCorretorUseCase = findCorretorUseCase;
        this.updateCorretorUseCase = updateCorretorUseCase;
        this.deleteCorretorUseCase = deleteCorretorUseCase;
    }

    @PostMapping
    public CorretorResponse create(@Valid @RequestBody CreateCorretorRequest request) {
        return createCorretorUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public CorretorListResponse findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return listCorretoresUseCase.execute(SecurityUtils.getCurrentUser(), page, limit);
    }

    @GetMapping("/{id}")
    public CorretorResponse findOne(@PathVariable Long id) {
        return findCorretorUseCase.execute(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}")
    public CorretorResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCorretorRequest request
    ) {
        return updateCorretorUseCase.execute(id, request, SecurityUtils.getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> remove(@PathVariable Long id) {
        return deleteCorretorUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
