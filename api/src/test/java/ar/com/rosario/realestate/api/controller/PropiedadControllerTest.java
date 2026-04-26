package ar.com.rosario.realestate.api.controller;

import ar.com.rosario.realestate.auth.config.SecurityConfig;
import ar.com.rosario.realestate.auth.jwt.JwtTenantConverter;
import ar.com.rosario.realestate.core.domain.*;
import ar.com.rosario.realestate.core.port.out.PropiedadRepository;
import ar.com.rosario.realestate.shared.TenantId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PropiedadController.class)
@Import({SecurityConfig.class, JwtTenantConverter.class})
@ActiveProfiles("test")
class PropiedadControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean PropiedadRepository propiedadRepository;
    @MockBean ar.com.rosario.realestate.api.service.TasarPropiedadService tasarPropiedadService;
    @MockBean JwtDecoder jwtDecoder;

    private static final String TENANT = "tenant-test-api";

    @Test
    void listar_propiedades_returns_200() throws Exception {
        Propiedad p = samplePropiedad();
        when(propiedadRepository.findByTenant(any())).thenReturn(List.of(p));

        mvc.perform(get("/api/v1/propiedades")
                .with(jwtForTenant(TENANT)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].direccion").value("Corrientes 456, Rosario"))
            .andExpect(jsonPath("$[0].tipo").value("DEPARTAMENTO"));
    }

    @Test
    void obtener_propiedad_existente_returns_200() throws Exception {
        Propiedad p = samplePropiedad();
        when(propiedadRepository.findById(any(TenantId.class), any(PropiedadId.class)))
            .thenReturn(Optional.of(p));

        mvc.perform(get("/api/v1/propiedades/" + p.getId().value())
                .with(jwtForTenant(TENANT)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(p.getId().value()));
    }

    @Test
    void obtener_propiedad_inexistente_returns_404() throws Exception {
        when(propiedadRepository.findById(any(), any())).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/propiedades/no-existe")
                .with(jwtForTenant(TENANT)))
            .andExpect(status().isNotFound());
    }

    @Test
    void sin_jwt_returns_401() throws Exception {
        mvc.perform(get("/api/v1/propiedades"))
            .andExpect(status().isUnauthorized());
    }

    private static Propiedad samplePropiedad() {
        Propiedad p = new Propiedad(
            PropiedadId.newId(), TenantId.of(TENANT),
            "Corrientes 456, Rosario",
            TipoPropiedad.DEPARTAMENTO,
            OperacionPropiedad.VENTA
        );
        p.actualizarPrecio(BigDecimal.valueOf(95000), BigDecimal.valueOf(985), LocalDate.now());
        return p;
    }

    private static SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor jwtForTenant(String tenantId) {
        return jwt().jwt(j -> j
            .subject("user@test.com")
            .claim("tenant_id", tenantId)
            .claim("roles", List.of("BROKER")));
    }
}
