package br.com.gmob.cliente.application.port;

import br.com.gmob.cliente.domain.model.Cliente;

public interface ClienteLookupPort {

    Cliente findByCpf(String cpf);
}
