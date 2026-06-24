package br.com.gmob.imovel.api;

import br.com.gmob.imovel.api.dto.CreateImovelRequest;
import br.com.gmob.imovel.api.dto.ImovelListResponse;
import br.com.gmob.imovel.api.dto.ImovelResponse;
import br.com.gmob.imovel.api.dto.UpdateImovelRequest;
import br.com.gmob.imovel.api.dto.UploadImagensResponse;
import br.com.gmob.imovel.application.CreateImovelUseCase;
import br.com.gmob.imovel.application.DeleteImovelUseCase;
import br.com.gmob.imovel.application.FindImovelUseCase;
import br.com.gmob.imovel.application.ListImoveisUseCase;
import br.com.gmob.imovel.application.UpdateImovelUseCase;
import br.com.gmob.imovel.application.UploadImovelImagensUseCase;
import br.com.gmob.infra.domain.enums.Disponibilidade;
import br.com.gmob.infra.domain.enums.StatusImovel;
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
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/imoveis")
public class ImovelController {

    private final CreateImovelUseCase createImovelUseCase;
    private final ListImoveisUseCase listImoveisUseCase;
    private final FindImovelUseCase findImovelUseCase;
    private final UpdateImovelUseCase updateImovelUseCase;
    private final DeleteImovelUseCase deleteImovelUseCase;
    private final UploadImovelImagensUseCase uploadImovelImagensUseCase;

    public ImovelController(
            CreateImovelUseCase createImovelUseCase,
            ListImoveisUseCase listImoveisUseCase,
            FindImovelUseCase findImovelUseCase,
            UpdateImovelUseCase updateImovelUseCase,
            DeleteImovelUseCase deleteImovelUseCase,
            UploadImovelImagensUseCase uploadImovelImagensUseCase
    ) {
        this.createImovelUseCase = createImovelUseCase;
        this.listImoveisUseCase = listImoveisUseCase;
        this.findImovelUseCase = findImovelUseCase;
        this.updateImovelUseCase = updateImovelUseCase;
        this.deleteImovelUseCase = deleteImovelUseCase;
        this.uploadImovelImagensUseCase = uploadImovelImagensUseCase;
    }

    @PostMapping
    public ImovelResponse create(@Valid @RequestBody CreateImovelRequest request) {
        return createImovelUseCase.execute(request, SecurityUtils.getCurrentUser());
    }

    @PostMapping("/{id}/imagens")
    public UploadImagensResponse uploadImagens(
            @PathVariable Long id,
            @RequestParam("files") List<MultipartFile> files
    ) {
        return uploadImovelImagensUseCase.execute(id, files, SecurityUtils.getCurrentUser());
    }

    @GetMapping
    public ImovelListResponse findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(required = false) Long tipo,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax,
            @RequestParam(required = false) StatusImovel status,
            @RequestParam(required = false) Disponibilidade disponibilidade,
            @RequestParam(required = false) BigDecimal valorAluguelMin,
            @RequestParam(required = false) BigDecimal valorAluguelMax
    ) {
        return listImoveisUseCase.execute(
                SecurityUtils.getCurrentUser(),
                page,
                limit,
                tipo,
                estado,
                cidade,
                valorMin,
                valorMax,
                status,
                disponibilidade,
                valorAluguelMin,
                valorAluguelMax
        );
    }

    @GetMapping("/{id}")
    public ImovelResponse findOne(@PathVariable Long id) {
        return findImovelUseCase.execute(id, SecurityUtils.getCurrentUser());
    }

    @PatchMapping("/{id}")
    public ImovelResponse update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateImovelRequest request
    ) {
        return updateImovelUseCase.execute(id, request, SecurityUtils.getCurrentUser());
    }

    @DeleteMapping("/{id}")
    public ImovelResponse remove(@PathVariable Long id) {
        return deleteImovelUseCase.execute(id, SecurityUtils.getCurrentUser());
    }
}
