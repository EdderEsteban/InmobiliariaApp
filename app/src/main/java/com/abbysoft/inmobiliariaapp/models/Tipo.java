package com.abbysoft.inmobiliariaapp.models;

import java.io.Serializable;

public class Tipo implements Serializable {
    private int id_tipo;
    private String tipo;

    public Tipo() {
    }

    public Tipo(int id_tipo, String tipo) {
        this.id_tipo = id_tipo;
        this.tipo = tipo;
    }

    // Getters y setters
    public int getId_tipo() { return id_tipo; }
    public void setId_tipo(int id_tipo) { this.id_tipo = id_tipo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Sobrescribir toString para mostrar solo el nombre en el Spinner
    @Override
    public String toString() {
        return tipo;
    }
}
