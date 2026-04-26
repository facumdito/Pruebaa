package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.Visita;

public interface AgendarVisitaUseCase {

    Visita agendar(AgendarVisitaCommand command);
}
