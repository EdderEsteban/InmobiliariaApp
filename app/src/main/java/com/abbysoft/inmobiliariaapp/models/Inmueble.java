package com.abbysoft.inmobiliariaapp.models;

import java.io.Serializable;
import java.util.List;

public class Inmueble implements Serializable {
    private int id_inmueble;
    private String direccion;
    private String uso;//(Residencial, Comercial)
    private int id_tipo;
    private Tipo tipo;  // Tipo de inmueble
    private int cantidad_Ambientes;
    private double precio_Alquiler;
    private String latitud;
    private String longitud;
    private boolean activo;
    private boolean disponible;

    private int id_propietario;

    private String fecha;
    private boolean borrado;
    private List<Foto> fotos;

    // Getters y setters
    public int getId_inmueble() { return id_inmueble; }
    public void setId_inmueble(int id_inmueble) { this.id_inmueble = id_inmueble; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getUso() { return uso; }
    public void setUso(String uso) { this.uso = uso; }

    public int getId_tipo() { return id_tipo; }
    public void setId_tipo(int id_tipo) { this.id_tipo = id_tipo; }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }

    public int getCantidad_Ambientes() { return cantidad_Ambientes; }
    public void setCantidad_Ambientes(int cantidad_Ambientes) { this.cantidad_Ambientes = cantidad_Ambientes; }

    public double getPrecio_Alquiler() { return precio_Alquiler; }
    public void setPrecio_Alquiler(double precio_Alquiler) { this.precio_Alquiler = precio_Alquiler; }

    public String getLatitud() { return latitud; }
    public void setLatitud(String latitud) { this.latitud = latitud; }

    public String getLongitud() { return longitud; }
    public void setLongitud(String longitud) { this.longitud = longitud; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public int getId_propietario() {
        return id_propietario;
    }

    public void setId_propietario(int id_propietario) {
        this.id_propietario = id_propietario;
    }

    public boolean isBorrado() { return borrado; }
    public void setBorrado(boolean borrado) { this.borrado = borrado; }

    public List<Foto> getFotos() { return fotos; }
    public void setFotos(List<Foto> fotos) { this.fotos = fotos; }
}
