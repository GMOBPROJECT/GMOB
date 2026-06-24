package br.com.gmob.infra.infrastructure.persistence;

import org.hibernate.annotations.JdbcTypeCode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Types;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@Target({FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@JdbcTypeCode(Types.INTEGER)
public @interface PostgresIntegerId {
}
