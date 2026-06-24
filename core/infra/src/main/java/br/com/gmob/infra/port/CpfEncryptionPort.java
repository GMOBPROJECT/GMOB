package br.com.gmob.infra.port;

public interface CpfEncryptionPort {

    String encrypt(String cpf);

    String decrypt(String encryptedCpf);
}
