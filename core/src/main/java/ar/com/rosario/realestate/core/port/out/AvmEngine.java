package ar.com.rosario.realestate.core.port.out;

import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.TasacionResult;

public interface AvmEngine {

    TasacionResult tasar(Propiedad propiedad);

    String modelVersion();
}
