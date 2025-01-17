package com.abbysoft.inmobiliariaapp.models;

import java.io.Serializable;

public class Foto implements Serializable {
    private int id_foto;
    private String fotoUrl;
    private int id_inmueble;

    public Foto() {
    }

    public Foto(int id_foto, String fotoUrl, int id_inmueble) {
        this.id_foto = id_foto;
        this.fotoUrl = fotoUrl;
        this.id_inmueble = id_inmueble;
    }

    // Getters y setters
    public int getId_foto() { return id_foto; }
    public void setId_foto(int id_foto) { this.id_foto = id_foto; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public int getId_inmueble() { return id_inmueble; }
    public void setId_inmueble(int id_inmueble) { this.id_inmueble = id_inmueble; }
}
