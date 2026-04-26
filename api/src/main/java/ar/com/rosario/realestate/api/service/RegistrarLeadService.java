package ar.com.rosario.realestate.api.service;

import ar.com.rosario.realestate.core.domain.Lead;
import ar.com.rosario.realestate.core.domain.LeadId;
import ar.com.rosario.realestate.core.port.in.RegistrarLeadCommand;
import ar.com.rosario.realestate.core.port.in.RegistrarLeadUseCase;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrarLeadService implements RegistrarLeadUseCase {

    private final LeadRepository repo;

    public RegistrarLeadService(LeadRepository repo) {
        this.repo = repo;
    }

    @Override
    public Lead registrar(RegistrarLeadCommand cmd) {
        Lead lead = new Lead(LeadId.newId(), cmd.tenantId(), cmd.nombre(), cmd.fuente());
        lead.setTelefono(cmd.telefono());
        lead.setEmail(cmd.email());
        repo.save(lead);
        return lead;
    }
}
