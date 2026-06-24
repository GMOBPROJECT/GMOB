package br.com.gmob.cliente.api;

import br.com.gmob.cliente.api.dto.CreateClienteRequest;
import br.com.gmob.cliente.api.dto.ClienteListResponse;
import br.com.gmob.cliente.api.dto.ClienteResponse;
import br.com.gmob.cliente.api.dto.UpdateClienteRequest;
import br.com.gmob.cliente.application.ArquivarClienteUseCase;
import br.com.gmob.cliente.application.CreateClienteUseCase;
import br.com.gmob.cliente.application.FindClienteUseCase;
import br.com.gmob.cliente.application.ListClientesUseCase;
import br.com.gmob.cliente.application.UpdateClienteUseCase;
import br.com.gmob.infra.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final CreateClienteUseCase createClienteUseCase;
    private final ListClientesUseCase listClientesUseCase;
    private final FindClienteUseCase findClienteUseCase;
    private final UpdateClienteUseCase updateClienteUseCase;
    private final ArquivarClienteUseCase arquivarClienteUseCase;

    public ClienteController(
            CreateClienteUseCase createClienteUseCase,
            ListClientesUseCase listClientesUseCase,
            FindClienteUseCase findClienteUseCase,
            UpdateClienteUseCase updateClienteUseCase,
            ArquivarClienteUseCase arquivarClienteUseCase
    ) {
        this.createClienteUseCase = createClienteUseCase;
        this.listClientesUseCase = listClientesUseCase;
        this.findClienteUseCase = findClienteUseCase;
        this.updateClienteUseCase = updateClienteUseCase;
        this.arquivarClienteUseCase = arquivarClienteUseCase;
    }

    @PostMapping
    public ClienteResponse create(@Valid @RequestBody CreateClienteRequest request) {
        return createClienteUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public ClienteListResponse findAll(
            @RequestParam(required = false) String tipoInteresse,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return listClientesUseCase.execute(SecurityUtils.getCurrentUser(), tipoInteresse, page, limit);
    }

    @GetMapping("/{id}")
    public ClienteResponse findOne(@PathVariable Long id) {
        return findClienteUseCase.execute(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}")
    public ClienteResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateClienteRequest request
    ) {
        return updateClienteUseCase.execute(id, request, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}/arquivar")
    public ClienteResponse arquivar(@PathVariable Long id) {
        return arquivarClienteUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
