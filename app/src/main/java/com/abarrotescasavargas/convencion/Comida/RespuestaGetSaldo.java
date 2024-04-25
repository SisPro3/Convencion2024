package com.abarrotescasavargas.convencion.Comida;
import com.google.gson.annotations.SerializedName;
public class RespuestaGetSaldo {
    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("Código")
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
