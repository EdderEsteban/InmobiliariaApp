package com.abbysoft.inmobiliariaapp.models;

import java.util.Date;

public class Pago {
    public int id_Pago;
    public int id_Contrato;
    public Date fecha_Pago;
    public double monto;
    public Date periodo;

    public Pago() {
    }

    public Pago(int id_Pago, int id_Contrato, Date fecha_Pago, double monto, Date periodo) {
        this.id_Pago = id_Pago;
        this.id_Contrato = id_Contrato;
        this.fecha_Pago = fecha_Pago;
        this.monto = monto;
        this.periodo = periodo;
    }

    public int getId_Pago() {
        return id_Pago;
    }

    public void setId_Pago(int id_Pago) {
        this.id_Pago = id_Pago;
    }

    public int getId_Contrato() {
        return id_Contrato;
    }

    public void setId_Contrato(int id_Contrato) {
        this.id_Contrato = id_Contrato;
    }

    public Date getFecha_Pago() {
        return fecha_Pago;
    }

    public void setFecha_Pago(Date fecha_Pago) {
        this.fecha_Pago = fecha_Pago;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Date periodo) {
        this.periodo = periodo;
    }
}