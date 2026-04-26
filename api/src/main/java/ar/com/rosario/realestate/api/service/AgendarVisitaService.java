package ar.com.rosario.realestate.api.service;

import ar.com.rosario.realestate.core.domain.Visita;
import ar.com.rosario.realestate.core.domain.VisitaId;
import ar.com.rosario.realestate.core.port.in.AgendarVisitaCommand;
import ar.com.rosario.realestate.core.port.in.AgendarVisitaUseCase;
import ar.com.rosario.realestate.core.port.out.VisitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AgendarVisitaService implements AgendarVisitaUseCase {

    private final VisitaRepository repo;

    public AgendarVisitaService(VisitaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Visita agendar(AgendarVisitaCommand cmd) {
        Visita visita = new Visita(
            VisitaId.newId(),
            cmd.tenantId(),
            cmd.propiedadId(),
            cmd.leadId(),
            cmd.agenteId(),
            cmd.fechaHora(),
            cmd.duracionMinutos()
        );
        repo.save(visita);
        return visita;
    }
}
