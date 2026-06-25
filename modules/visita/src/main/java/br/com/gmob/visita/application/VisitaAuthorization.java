package br.com.gmob.visita.application;

import br.com.gmob.infra.domain.enums.Perfil;
import br.com.gmob.infra.exception.ForbiddenException;
import br.com.gmob.infra.security.AuthenticatedUser;
import br.com.gmob.visita.domain.model.Visita;

public final class VisitaAuthorization {

    private VisitaAuthorization() {
    }

    public static void validateRole(AuthenticatedUser user) {
        if (user.perfil() != Perfil.CORRETOR && user.perfil() != Perfil.ADMINISTRADOR) {
            throw new ForbiddenException("Acesso negado");
        }
    }

    public static void validateOwnership(Visita visita, AuthenticatedUser user) {
        if (user.perfil() == Perfil.CORRETOR && !visita.corretorId().equals(user.corretorId())) {
            throw new ForbiddenException("Agendamento não encontrado");
        }
    }

    public static Long corretorFilter(AuthenticatedUser user) {
        return user.perfil() == Perfil.CORRETOR ? user.corretorId() : null;
    }
}
