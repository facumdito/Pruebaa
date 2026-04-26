package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.Lead;

public interface RegistrarLeadUseCase {

    Lead registrar(RegistrarLeadCommand command);
}
