package com.abarrotescasavargas.convencion.Staff.Registrados;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRegistrados {
    @GET("getComidasAgrupadas.php")
    Call<List<ObjComidasAgrupadas>> obtenerDatos();

    @GET("getReuniones.php")
    Call<ObjRegistrados> getMeetingStats();

    @GET("getRegistrados.php")
    Call<ObjEsperadosRegistrados> getRegistrados();
}
