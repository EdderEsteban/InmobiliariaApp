package com.abbysoft.inmobiliariaapp.models;

import java.util.Date;

public class Contrato {
    public int id_contrato;
    public int monto;
    public Date fecha_inicio;
    public Date fecha_fin;
    public Boolean vigencia;

    public Contrato() {
    }

    public Contrato(int id_contrato, int monto, Date fecha_inicio, Date fecha_fin, Boolean vigencia, Date fecha) {
        id_contrato = id_contrato;
        monto = monto;
        fecha_inicio = fecha_inicio;
        fecha_fin = fecha_fin;
        vigencia = vigencia;

    }

    public int getId_contrato() {
        return id_contrato;
    }

    public void setId_contrato(int id_contrato) {
        this.id_contrato = id_contrato;
    }

    public int getMonto() {
        return monto;
    }

    public void setMonto(int monto) {
        this.monto = monto;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public Boolean getVigencia() {
        return vigencia;
    }

    public void setVigencia(Boolean vigencia) {
        this.vigencia = vigencia;
    }
}
