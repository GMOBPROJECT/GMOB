package br.com.gmob.imovel.api;

import br.com.gmob.imovel.api.dto.CreateTipoImovelRequest;
import br.com.gmob.imovel.api.dto.TipoImovelResponse;
import br.com.gmob.imovel.api.dto.UpdateTipoImovelRequest;
import br.com.gmob.imovel.application.CreateTipoImovelUseCase;
import br.com.gmob.imovel.application.DeleteTipoImovelUseCase;
import br.com.gmob.imovel.application.ListTiposImovelUseCase;
import br.com.gmob.imovel.application.UpdateTipoImovelUseCase;
import br.com.gmob.infra.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tipos-imoveis")
public class TipoImovelController {

    private final CreateTipoImovelUseCase createTipoImovelUseCase;
    private final ListTiposImovelUseCase listTiposImovelUseCase;
    private final UpdateTipoImovelUseCase updateTipoImovelUseCase;
    private final DeleteTipoImovelUseCase deleteTipoImovelUseCase;

    public TipoImovelController(
            CreateTipoImovelUseCase createTipoImovelUseCase,
            ListTiposImovelUseCase listTiposImovelUseCase,
            UpdateTipoImovelUseCase updateTipoImovelUseCase,
            DeleteTipoImovelUseCase deleteTipoImovelUseCase
    ) {
        this.createTipoImovelUseCase = createTipoImovelUseCase;
        this.listTiposImovelUseCase = listTiposImovelUseCase;
        this.updateTipoImovelUseCase = updateTipoImovelUseCase;
        this.deleteTipoImovelUseCase = deleteTipoImovelUseCase;
    }

    @PostMapping
    public TipoImovelResponse create(@Valid @RequestBody CreateTipoImovelRequest request) {
        return createTipoImovelUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public List<TipoImovelResponse> findAll() {
        return listTiposImovelUseCase.execute();
    }

    @PatchMapping("/{id}")
    public TipoImovelResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTipoImovelRequest request
    ) {
        return updateTipoImovelUseCase.execute(id, request, SecurityUtils.getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public TipoImovelResponse remove(@PathVariable Long id) {
        return deleteTipoImovelUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
