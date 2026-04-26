package ar.com.rosario.realestate.api.service;

import ar.com.rosario.realestate.core.domain.Propiedad;
import ar.com.rosario.realestate.core.domain.Tasacion;
import ar.com.rosario.realestate.core.domain.TasacionId;
import ar.com.rosario.realestate.core.domain.TasacionResult;
import ar.com.rosario.realestate.core.port.in.TasarPropiedadCommand;
import ar.com.rosario.realestate.core.port.in.TasarPropiedadUseCase;
import ar.com.rosario.realestate.core.port.out.AvmEngine;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.core.port.out.TasacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TasarPropiedadService implements TasarPropiedadUseCase {

    private final PropiedadRepository propiedades;
    private final TasacionRepository tasaciones;
    private final AvmEngine avmEngine;

    public TasarPropiedadService(PropiedadRepository propiedades,
                                  TasacionRepository tasaciones,
                                  AvmEngine avmEngine) {
        this.propiedades = propiedades;
        this.tasaciones = tasaciones;
        this.avmEngine = avmEngine;
    }

    @Override
    public TasacionResult tasar(TasarPropiedadCommand cmd) {
        Propiedad propiedad = propiedades.findById(cmd.tenantId(), cmd.propiedadId())
            .orElseThrow(() -> new IllegalArgumentException("Propiedad no encontrada: " + cmd.propiedadId().value()));

        TasacionResult result = avmEngine.tasar(propiedad);

        Tasacion tasacion = Tasacion.desdeResultadoAvm(
            TasacionId.newId(), cmd.tenantId(), null, result
        );
        tasaciones.save(tasacion);

        return result;
    }
}
