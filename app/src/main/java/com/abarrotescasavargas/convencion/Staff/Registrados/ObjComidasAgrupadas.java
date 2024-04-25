package com.abarrotescasavargas.convencion.Staff.Registrados;

import com.google.gson.annotations.SerializedName;

public class ObjComidasAgrupadas {
    @SerializedName("Identificador")
    private String identificador;

    @SerializedName("Total_Comidas")
    private String totalComidas;

    @SerializedName("Total_Saldo")
    private String totalSaldo;

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTotalComidas() {
        return totalComidas;
    }

    public void setTotalComidas(String totalComidas) {
        this.totalComidas = totalComidas;
    }

    public String getTotalSaldo() {
        return totalSaldo;
    }

    public void setTotalSaldo(String totalSaldo) {
        this.totalSaldo = totalSaldo;
    }
}
