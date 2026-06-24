package br.com.gmob.cliente.api;

import br.com.gmob.cliente.api.dto.CreateTransacaoRequest;
import br.com.gmob.cliente.api.dto.CreateTransacaoResultResponse;
import br.com.gmob.cliente.api.dto.MessageResponse;
import br.com.gmob.cliente.api.dto.TransacaoFullResponse;
import br.com.gmob.cliente.api.dto.TransacaoListResponse;
import br.com.gmob.cliente.application.CreateTransacaoUseCase;
import br.com.gmob.cliente.application.DeleteTransacaoUseCase;
import br.com.gmob.cliente.application.FindTransacaoUseCase;
import br.com.gmob.cliente.application.ListTransacoesUseCase;
import br.com.gmob.infra.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final CreateTransacaoUseCase createTransacaoUseCase;
    private final ListTransacoesUseCase listTransacoesUseCase;
    private final FindTransacaoUseCase findTransacaoUseCase;
    private final DeleteTransacaoUseCase deleteTransacaoUseCase;

    public TransacaoController(
            CreateTransacaoUseCase createTransacaoUseCase,
            ListTransacoesUseCase listTransacoesUseCase,
            FindTransacaoUseCase findTransacaoUseCase,
            DeleteTransacaoUseCase deleteTransacaoUseCase
    ) {
        this.createTransacaoUseCase = createTransacaoUseCase;
        this.listTransacoesUseCase = listTransacoesUseCase;
        this.findTransacaoUseCase = findTransacaoUseCase;
        this.deleteTransacaoUseCase = deleteTransacaoUseCase;
    }

    @PostMapping
    public CreateTransacaoResultResponse create(@Valid @RequestBody CreateTransacaoRequest request) {
        return createTransacaoUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public TransacaoListResponse findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return listTransacoesUseCase.execute(SecurityUtils.getCurrentUser(), page, limit);
    }

    @GetMapping("/{id}")
    public TransacaoFullResponse findOne(@PathVariable Long id) {
        return findTransacaoUseCase.execute(id, SecurityUtils.getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public MessageResponse remove(@PathVariable Long id) {
        return deleteTransacaoUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
