package br.com.gmob.visita.api;

import br.com.gmob.infra.domain.enums.StatusAgendamento;
import br.com.gmob.infra.security.SecurityUtils;
import br.com.gmob.visita.api.dto.CreateVisitaRequest;
import br.com.gmob.visita.api.dto.UpdateVisitaRequest;
import br.com.gmob.visita.api.dto.VisitaListResponse;
import br.com.gmob.visita.api.dto.VisitaResponse;
import br.com.gmob.visita.application.CreateVisitaUseCase;
import br.com.gmob.visita.application.DeleteVisitaUseCase;
import br.com.gmob.visita.application.FindVisitaUseCase;
import br.com.gmob.visita.application.ListVisitasUseCase;
import br.com.gmob.visita.application.UpdateVisitaUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/visitas")
public class VisitaController {

    private final CreateVisitaUseCase createVisitaUseCase;
    private final ListVisitasUseCase listVisitasUseCase;
    private final FindVisitaUseCase findVisitaUseCase;
    private final UpdateVisitaUseCase updateVisitaUseCase;
    private final DeleteVisitaUseCase deleteVisitaUseCase;

    public VisitaController(
            CreateVisitaUseCase createVisitaUseCase,
            ListVisitasUseCase listVisitasUseCase,
            FindVisitaUseCase findVisitaUseCase,
            UpdateVisitaUseCase updateVisitaUseCase,
            DeleteVisitaUseCase deleteVisitaUseCase
    ) {
        this.createVisitaUseCase = createVisitaUseCase;
        this.listVisitasUseCase = listVisitasUseCase;
        this.findVisitaUseCase = findVisitaUseCase;
        this.updateVisitaUseCase = updateVisitaUseCase;
        this.deleteVisitaUseCase = deleteVisitaUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VisitaResponse create(@Valid @RequestBody CreateVisitaRequest request) {
        return createVisitaUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public VisitaListResponse list(
            @RequestParam(required = false) Long imovelId,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) LocalDate data,
            @RequestParam(required = false) StatusAgendamento status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return listVisitasUseCase.execute(
                SecurityUtils.getCurrentUser(),
                imovelId,
                clienteId,
                data,
                status,
                page,
                limit
        );
    }

    @GetMapping("/{id}")
    public VisitaResponse findOne(@PathVariable Long id) {
        return findVisitaUseCase.execute(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}")
    public VisitaResponse update(@PathVariable Long id, @RequestBody UpdateVisitaRequest request) {
        return updateVisitaUseCase.execute(id, request, SecurityUtils.getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public VisitaResponse remove(@PathVariable Long id) {
        return deleteVisitaUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
