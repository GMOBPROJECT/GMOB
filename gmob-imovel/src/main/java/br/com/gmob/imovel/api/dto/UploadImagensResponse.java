package br.com.gmob.imovel.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UploadImagensResponse(
        boolean success,
        String message,
        long total,
        List<ImagemUploadDataResponse> data
) {
}
