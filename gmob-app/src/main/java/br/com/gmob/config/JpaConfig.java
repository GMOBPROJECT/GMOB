package br.com.gmob.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {
        "br.com.gmob.corretor.infrastructure.persistence",
        "br.com.gmob.imovel.infrastructure.persistence",
        "br.com.gmob.cliente.infrastructure.persistence"
})
@EntityScan(basePackages = {
        "br.com.gmob.corretor.infrastructure.persistence",
        "br.com.gmob.imovel.infrastructure.persistence",
        "br.com.gmob.cliente.infrastructure.persistence"
})
public class JpaConfig {
}
