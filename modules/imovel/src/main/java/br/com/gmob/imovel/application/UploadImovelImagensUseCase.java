package br.com.gmob.imovel.application;

import br.com.gmob.imovel.api.dto.ImagemUploadDataResponse;
import br.com.gmob.imovel.api.dto.UploadImagensResponse;
import br.com.gmob.imovel.application.port.CloudinaryStoragePort;
import br.com.gmob.imovel.domain.model.ImagemImovel;
import br.com.gmob.imovel.domain.port.ImagemImovelRepositoryPort;
import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.BusinessException;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadImovelImagensUseCase {

    private static final int MAX_FILES = 10;

    private final CloudinaryStoragePort cloudinaryStorage;
    private final ImagemImovelRepositoryPort imagemRepository;
    private final FindImovelUseCase findImovelUseCase;

    public UploadImovelImagensUseCase(
            CloudinaryStoragePort cloudinaryStorage,
            ImagemImovelRepositoryPort imagemRepository,
            FindImovelUseCase findImovelUseCase
    ) {
        this.cloudinaryStorage = cloudinaryStorage;
        this.imagemRepository = imagemRepository;
        this.findImovelUseCase = findImovelUseCase;
    }

    @Transactional
    public UploadImagensResponse execute(Long id, List<MultipartFile> files, AuthenticatedUser currentUser) {
        validateRole(currentUser);

        if (files == null || files.isEmpty()) {
            throw new BusinessException("Nenhuma imagem enviada.");
        }

        if (files.size() > MAX_FILES) {
            throw new BusinessException("Máximo de " + MAX_FILES + " imagens por upload.");
        }

        findImovelUseCase.execute(id, currentUser);

        List<ImagemImovel> imagensToSave = new ArrayList<>();

        for (MultipartFile file : files) {
            String url = cloudinaryStorage.uploadImage(readBytes(file), file.getOriginalFilename());
            imagensToSave.add(new ImagemImovel(null, url, id));
        }

        List<ImagemUploadDataResponse> data = imagemRepository.saveAll(imagensToSave).stream()
                .map(img -> new ImagemUploadDataResponse(img.imovelId(), img.url()))
                .toList();

        return new UploadImagensResponse(
                true,
                "The property images were successfully stored.",
                data.size(),
                data
        );
    }

    private byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new BusinessException("Falha ao ler arquivo enviado.");
        }
    }

    private void validateRole(AuthenticatedUser currentUser) {
        if (currentUser.perfil() != Perfil.CORRETOR && currentUser.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }
}
