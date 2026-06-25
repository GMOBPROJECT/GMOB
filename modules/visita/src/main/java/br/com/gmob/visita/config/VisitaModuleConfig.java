package br.com.gmob.visita.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@ComponentScan(basePackages = "br.com.gmob.visita")
@Import(AsyncConfig.class)
public class VisitaModuleConfig {
}
