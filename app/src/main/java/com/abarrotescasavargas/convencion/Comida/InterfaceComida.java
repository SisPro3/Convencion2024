package com.abarrotescasavargas.convencion.Comida;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface InterfaceComida {
    @GET("getSaldo.php")
    Call<RespuestaGetSaldo> getProveedorInfo(@Query("CVE_PROVEEDOR") String cveProveedor);
    @GET("getComidasXDia.php") // Reemplaza "ruta_al_endpoint.php" con la ruta de tu endpoint
    Call<RespuestaConteo> getDatos();
}