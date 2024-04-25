package com.abarrotescasavargas.convencion.Gerencia.Reuniones;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InterfazReuniones {
    @GET("api/convencion/android/getBitacora.php")
    Call<List<ObjEmpresa>> getBitacora(@Query("folio") String parametro);

}