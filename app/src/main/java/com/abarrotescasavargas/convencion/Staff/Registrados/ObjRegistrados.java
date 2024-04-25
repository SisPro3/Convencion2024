package com.abarrotescasavargas.convencion.Staff.Registrados;

import com.google.gson.annotations.SerializedName;

public class ObjRegistrados {
    @SerializedName("total_reuniones")
    private int total_reuniones;

    @SerializedName("reuniones_completadas")
    private int reuniones_completadas;

    public int getTotal_reuniones() {
        return total_reuniones;
    }

    public void setTotal_reuniones(int total_reuniones) {
        this.total_reuniones = total_reuniones;
    }

    public int getReuniones_completadas() {
        return reuniones_completadas;
    }

    public void setReuniones_completadas(int reuniones_completadas) {
        this.reuniones_completadas = reuniones_completadas;
    }
}
