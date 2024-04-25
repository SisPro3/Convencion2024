package com.abarrotescasavargas.convencion.Login;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({
            "user:ACVprogrammer",
            "pass:V=.a{Xf}e0CR"
    })
    @GET("asistente.php")
    Call<ObjAsistente.ApiResponse> obtenerDatos(@Query("folio") String folio);

}
