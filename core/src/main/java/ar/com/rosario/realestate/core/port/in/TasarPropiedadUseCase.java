package ar.com.rosario.realestate.core.port.in;

import ar.com.rosario.realestate.core.domain.TasacionResult;

public interface TasarPropiedadUseCase {

    TasacionResult tasar(TasarPropiedadCommand command);
}
