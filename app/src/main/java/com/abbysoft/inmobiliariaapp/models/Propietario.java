package com.abbysoft.inmobiliariaapp.models;

import com.google.gson.annotations.SerializedName;

public class Propietario {
    // Atributos
    @SerializedName("id_Propietario")
    private int id_Propietario;

    @SerializedName("nombre")
    private String Nombre;

    @SerializedName("apellido")
    private String Apellido;

    @SerializedName("dni")
    private String Dni;

    @SerializedName("direccion")
    private String Direccion;

    @SerializedName("telefono")
    private String Telefono;

    @SerializedName("correo")
    private String Correo;

    @SerializedName("avatar")
    private String Avatar;

    // Constructores y métodos getter y setter


    public Propietario() {
    }

    public Propietario(int id_Propietario, String nombre, String apellido, String dni, String direccion, String telefono, String correo, String avatar) {
        this.id_Propietario = id_Propietario;
        Nombre = nombre;
        Apellido = apellido;
        Dni = dni;
        Direccion = direccion;
        Telefono = telefono;
        Correo = correo;
        Avatar = avatar;
    }

    public int getId_Propietario() {
        return id_Propietario;
    }

    public void setId_Propietario(int id_Propietario) {
        this.id_Propietario = id_Propietario;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String apellido) {
        Apellido = apellido;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
