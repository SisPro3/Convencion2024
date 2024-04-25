package com.abarrotescasavargas.convencion.Gerencia.Sugeridos;

import com.google.gson.annotations.SerializedName;

public class Productos {
    @SerializedName("clave")
    private String clave;
    @SerializedName("clavePro")
    private String clavePro;
    @SerializedName("nombre")
    private String nombre;

    @SerializedName("existencia")
    private String existencia;

    @SerializedName("sugerido")
    private String sugerido;
    @SerializedName("mes")
    private String mes;
    @SerializedName("DS_COMENTARIO")
    private String comentario;
    public Productos(String clavePro ,String clave, String nombre, String existencia, String sugerido, String mes,String comentario) {
        this.clave = clave;
        this.nombre = nombre;
        this.existencia = existencia;
        this.sugerido = sugerido;
        this.mes = mes;
        this.clavePro=clavePro;
        this.comentario = comentario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getClavePro() {
        return clavePro;
    }

    public void setClavePro(String clavePro) {
        this.clavePro = clavePro;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getSugerido() {
        return sugerido;
    }

    public void setSugerido(String sugerido) {
        this.sugerido = sugerido;
    }
}
