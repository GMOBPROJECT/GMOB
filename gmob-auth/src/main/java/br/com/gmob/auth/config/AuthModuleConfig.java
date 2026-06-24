package br.com.gmob.auth.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "br.com.gmob.auth")
@EnableConfigurationProperties(JwtProperties.class)
public class AuthModuleConfig {
}
