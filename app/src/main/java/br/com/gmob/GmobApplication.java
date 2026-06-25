package br.com.gmob;

import br.com.gmob.auth.config.AuthModuleConfig;
import br.com.gmob.cliente.config.ClienteModuleConfig;
import br.com.gmob.corretor.config.CorretorModuleConfig;
import br.com.gmob.imovel.config.ImovelModuleConfig;
import br.com.gmob.visita.config.VisitaModuleConfig;
import br.com.gmob.config.JpaConfig;
import br.com.gmob.infra.config.WebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = "br.com.gmob")
@Import({
        WebConfig.class,
        JpaConfig.class,
        CorretorModuleConfig.class,
        AuthModuleConfig.class,
        ImovelModuleConfig.class,
        VisitaModuleConfig.class,
        ClienteModuleConfig.class
})
public class GmobApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmobApplication.class, args);
    }
}
