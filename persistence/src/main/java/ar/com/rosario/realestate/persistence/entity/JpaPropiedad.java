package ar.com.rosario.realestate.persistence.entity;

import ar.com.rosario.realestate.core.domain.EstadoPropiedad;
import ar.com.rosario.realestate.core.domain.OperacionPropiedad;
import ar.com.rosario.realestate.core.domain.TipoPropiedad;
import ar.com.rosario.realestate.shared.SyncState;
import jakarta.persistence.*;
import org.hibernate.annotations.TenantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "propiedades",
    indexes = {
        @Index(name = "idx_prop_tenant_updated", columnList = "tenant_id,updated_at"),
        @Index(name = "idx_prop_estado",         columnList = "tenant_id,estado"),
        @Index(name = "idx_prop_barrio",          columnList = "tenant_id,barrio")
    }
)
public class JpaPropiedad {

    @Id
    @Column(name = "id", length = 36, nullable = false, updatable = false)
    private String id;

    @TenantId
    @Column(name = "tenant_id", length = 36, nullable = false, updatable = false)
    private String tenantId;

    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "barrio")
    private String barrio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoPropiedad tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "operacion", nullable = false)
    private OperacionPropiedad operacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPropiedad estado;

    @Column(name = "latitud", precision = 10, scale = 7)
    private Double latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private Double longitud;

    @Column(name = "precio_usd", precision = 15, scale = 2)
    private BigDecimal precioUsd;

    @Column(name = "tipo_cambio_mep", precision = 15, scale = 4)
    private BigDecimal tipoCambioMep;

    @Column(name = "fecha_cotizacion")
    private LocalDate fechaCotizacion;

    @Column(name = "m2_cubiertos", precision = 8, scale = 2)
    private BigDecimal m2Cubiertos;

    @Column(name = "m2_totales", precision = 8, scale = 2)
    private BigDecimal m2Totales;

    @Column(name = "ambientes")
    private Integer ambientes;

    @Column(name = "dormitorios")
    private Integer dormitorios;

    @Column(name = "banos")
    private Integer banos;

    @Column(name = "cocheras")
    private Integer cocheras;

    @Column(name = "antiguedad_anios")
    private Integer antiguedadAnios;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_state", nullable = false)
    private SyncState syncState;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    public TipoPropiedad getTipo() { return tipo; }
    public void setTipo(TipoPropiedad tipo) { this.tipo = tipo; }
    public OperacionPropiedad getOperacion() { return operacion; }
    public void setOperacion(OperacionPropiedad operacion) { this.operacion = operacion; }
    public EstadoPropiedad getEstado() { return estado; }
    public void setEstado(EstadoPropiedad estado) { this.estado = estado; }
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    public BigDecimal getPrecioUsd() { return precioUsd; }
    public void setPrecioUsd(BigDecimal precioUsd) { this.precioUsd = precioUsd; }
    public BigDecimal getTipoCambioMep() { return tipoCambioMep; }
    public void setTipoCambioMep(BigDecimal tipoCambioMep) { this.tipoCambioMep = tipoCambioMep; }
    public LocalDate getFechaCotizacion() { return fechaCotizacion; }
    public void setFechaCotizacion(LocalDate fechaCotizacion) { this.fechaCotizacion = fechaCotizacion; }
    public BigDecimal getM2Cubiertos() { return m2Cubiertos; }
    public void setM2Cubiertos(BigDecimal m2Cubiertos) { this.m2Cubiertos = m2Cubiertos; }
    public BigDecimal getM2Totales() { return m2Totales; }
    public void setM2Totales(BigDecimal m2Totales) { this.m2Totales = m2Totales; }
    public Integer getAmbientes() { return ambientes; }
    public void setAmbientes(Integer ambientes) { this.ambientes = ambientes; }
    public Integer getDormitorios() { return dormitorios; }
    public void setDormitorios(Integer dormitorios) { this.dormitorios = dormitorios; }
    public Integer getBanos() { return banos; }
    public void setBanos(Integer banos) { this.banos = banos; }
    public Integer getCocheras() { return cocheras; }
    public void setCocheras(Integer cocheras) { this.cocheras = cocheras; }
    public Integer getAntiguedadAnios() { return antiguedadAnios; }
    public void setAntiguedadAnios(Integer antiguedadAnios) { this.antiguedadAnios = antiguedadAnios; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public SyncState getSyncState() { return syncState; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public boolean isDeleted() { return deleted; }
    public void setDeleted(boolean deleted) { this.deleted = deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
