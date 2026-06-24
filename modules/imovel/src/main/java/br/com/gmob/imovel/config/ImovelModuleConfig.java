package br.com.gmob.imovel.config;

import br.com.gmob.imovel.infrastructure.cloudinary.CloudinaryProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "br.com.gmob.imovel")
@EnableConfigurationProperties(CloudinaryProperties.class)
public class ImovelModuleConfig {
}
