package com.abarrotescasavargas.convencion.Staff.Comidas;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiComidas {
        @FormUrlEncoded
        @POST("api/convencion/android/getSaldo.php")
        Call<ResponseAddComidas> getSaldo(
                @Field("CVE_PROVEEDOR") String cveProveedor,
                @Field("CANTIDAD") int cantidad,
                @Field("AUTORIZO") String user
        );
        @GET("getProveedores.php")
        Call<List<ObjProveedor>> obtenerProveedores();
}
