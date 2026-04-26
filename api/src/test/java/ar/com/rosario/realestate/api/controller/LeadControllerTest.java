package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.auth.config.SecurityConfig;
import ar.com.rosario.realestate.auth.jwt.JwtTenantConverter;
import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.LeadRepository;
import ar.com.rosario.realestate.api.service.RegistrarLeadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LeadController.class)
@Import({SecurityConfig.class, JwtTenantConverter.class})
@ActiveProfiles("test")
class LeadControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean LeadRepository leadRepository;
    @MockBean RegistrarLeadService registrarLeadService;
    @MockBean JwtDecoder jwtDecoder;

    private static final String TENANT = "tenant-lead-api";

    @Test
    void registrar_lead_returns_201() throws Exception {
        Lead lead = sampleLead();
        when(registrarLeadService.registrar(any())).thenReturn(lead);

        String body = mapper.writeValueAsString(Map.of(
            "nombre", "Juan Gomez",
            "telefono", "+5493412000000",
            "email", "juan@example.com",
            "fuente", "WHATSAPP"
        ));

        mvc.perform(post("/api/v1/leads")
                .with(jwt().jwt(j -> j.claim("tenant_id", TENANT)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Juan Gomez"));
    }

    @Test
    void listar_leads_returns_200() throws Exception {
        when(leadRepository.findByTenant(any())).thenReturn(List.of(sampleLead()));

        mvc.perform(get("/api/v1/leads")
                .with(jwt().jwt(j -> j.claim("tenant_id", TENANT))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }

    private Lead sampleLead() {
        Lead l = new Lead(LeadId.newId(), ar.com.rosario.realestate.shared.TenantId.of(TENANT),
                          "Juan Gomez", FuenteLead.WHATSAPP);
        l.setTelefono("+5493412000000");
        l.setEmail("juan@example.com");
        return l;
    }
}
