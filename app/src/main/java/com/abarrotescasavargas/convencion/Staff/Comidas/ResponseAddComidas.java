package com.abarrotescasavargas.convencion.Staff.Comidas;
import com.google.gson.annotations.SerializedName;

public class ResponseAddComidas {
    private String mensaje;
    private String codigo;
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
