package br.com.gmob.infra.infrastructure.crypto;

import br.com.gmob.infra.port.CpfEncryptionPort;
import org.springframework.stereotype.Component;

@Component
public class NoOpCpfEncryptionAdapter implements CpfEncryptionPort {

    @Override
    public String encrypt(String cpf) {
        return cpf;
    }

    @Override
    public String decrypt(String encryptedCpf) {
        return encryptedCpf;
    }
}
