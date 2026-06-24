package br.com.gmob.infra.validation;

public final class ValidationPatterns {

    public static final String CPF_PATTERN = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
    public static final String TELEFONE_PATTERN = "^\\(\\d{2}\\)\\s\\d{4,5}-\\d{4}$";

    private ValidationPatterns() {
    }
}
