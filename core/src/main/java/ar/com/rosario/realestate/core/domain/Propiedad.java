package ar.com.rosario.realestate.core.domain;

import ar.com.rosario.realestate.shared.SyncState;
import ar.com.rosario.realestate.shared.TenantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class Propiedad {

    private final PropiedadId id;
    private final TenantId tenantId;

    private String direccion;
    private Barrio barrio;
    private TipoPropiedad tipo;
    private OperacionPropiedad operacion;
    private EstadoPropiedad estado;
    private Coordenadas coordenadas;

    private BigDecimal precioUsd;
    private BigDecimal tipoCambioMep;
    private LocalDate fechaCotizacion;

    private BigDecimal m2Cubiertos;
    private BigDecimal m2Totales;
    private int ambientes;
    private int dormitorios;
    private int banos;
    private int cocheras;
    private int antiguedadAnios;

    private String descripcion;

    private long version;
    private SyncState syncState;
    private boolean deleted;
    private Instant createdAt;
    private Instant updatedAt;

    public Propiedad(PropiedadId id, TenantId tenantId, String direccion,
                     TipoPropiedad tipo, OperacionPropiedad operacion) {
        this.id = id;
        this.tenantId = tenantId;
        this.direccion = direccion;
        this.tipo = tipo;
        this.operacion = operacion;
        this.estado = EstadoPropiedad.DISPONIBLE;
        this.version = 0;
        this.syncState = SyncState.LOCAL;
        this.deleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void actualizarPrecio(BigDecimal precioUsd, BigDecimal tipoCambioMep, LocalDate fechaCotizacion) {
        this.precioUsd = precioUsd;
        this.tipoCambioMep = tipoCambioMep;
        this.fechaCotizacion = fechaCotizacion;
        touch();
    }

    public void cambiarEstado(EstadoPropiedad nuevoEstado) {
        this.estado = nuevoEstado;
        touch();
    }

    public void marcarEliminada() {
        this.deleted = true;
        touch();
    }

    private void touch() {
        this.version++;
        this.updatedAt = Instant.now();
        if (this.syncState == SyncState.SYNCED) {
            this.syncState = SyncState.LOCAL;
        }
    }

    // Getters
    public PropiedadId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getDireccion() { return direccion; }
    public Barrio getBarrio() { return barrio; }
    public TipoPropiedad getTipo() { return tipo; }
    public OperacionPropiedad getOperacion() { return operacion; }
    public EstadoPropiedad getEstado() { return estado; }
    public Coordenadas getCoordenadas() { return coordenadas; }
    public BigDecimal getPrecioUsd() { return precioUsd; }
    public BigDecimal getTipoCambioMep() { return tipoCambioMep; }
    public LocalDate getFechaCotizacion() { return fechaCotizacion; }
    public BigDecimal getM2Cubiertos() { return m2Cubiertos; }
    public BigDecimal getM2Totales() { return m2Totales; }
    public int getAmbientes() { return ambientes; }
    public int getDormitorios() { return dormitorios; }
    public int getBanos() { return banos; }
    public int getCocheras() { return cocheras; }
    public int getAntiguedadAnios() { return antiguedadAnios; }
    public String getDescripcion() { return descripcion; }
    public long getVersion() { return version; }
    public SyncState getSyncState() { return syncState; }
    public boolean isDeleted() { return deleted; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters for reconstruction from persistence
    public void setBarrio(Barrio barrio) { this.barrio = barrio; }
    public void setCoordenadas(Coordenadas coordenadas) { this.coordenadas = coordenadas; }
    public void setM2Cubiertos(BigDecimal m2Cubiertos) { this.m2Cubiertos = m2Cubiertos; }
    public void setM2Totales(BigDecimal m2Totales) { this.m2Totales = m2Totales; }
    public void setAmbientes(int ambientes) { this.ambientes = ambientes; }
    public void setDormitorios(int dormitorios) { this.dormitorios = dormitorios; }
    public void setBanos(int banos) { this.banos = banos; }
    public void setCocheras(int cocheras) { this.cocheras = cocheras; }
    public void setAntiguedadAnios(int antiguedadAnios) { this.antiguedadAnios = antiguedadAnios; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setVersion(long version) { this.version = version; }
    public void setSyncState(SyncState syncState) { this.syncState = syncState; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
