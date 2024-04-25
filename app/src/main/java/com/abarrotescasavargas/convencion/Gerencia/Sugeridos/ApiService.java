package com.abarrotescasavargas.convencion.Gerencia.Sugeridos;

import com.abarrotescasavargas.convencion.Login.ObjKRegistro;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("getSugeridos.php")
    Call<List<Productos>> obtenerSugeridos(
            @Query("cveSuc") String cveSuc,
            @Query("cvePro") String cvePro
    );
    @GET("getAllSugeridos.php")
    Call<List<Productos>> obtenerAllSugeridos(
            @Query("cveSuc") String cveSuc
    );
    @GET("getKREGISTRO.php")
    Call<List<ObjKRegistro>> getKRegistros();
}
