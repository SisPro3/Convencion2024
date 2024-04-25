package com.abarrotescasavargas.convencion.Staff.Comidas;

import android.content.Context;

import com.abarrotescasavargas.convencion.FunGenerales.DB;
import com.abarrotescasavargas.convencion.Gerencia.ActivityGerencia;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "https://abarrotescasavargas.mx/";

    private ApiComidas apiService;
    private Context mContext;

    public ApiClient(Context context) {
        mContext = context;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiComidas.class);
    }

    public void getSaldo(String cveProveedor, int cantidad, final ApiCallback callback) {
        DB transferenciasRepository = new DB(mContext); // Utilizar el contexto aquí
        String valorUS_USUARI = transferenciasRepository.gerUser();
        Call<ResponseAddComidas> call = apiService.getSaldo(cveProveedor, cantidad,valorUS_USUARI);
        call.enqueue(new Callback<ResponseAddComidas>() {
            @Override
            public void onResponse(Call<ResponseAddComidas> call, Response<ResponseAddComidas> response) {
                if (response.isSuccessful()) {
                    ResponseAddComidas apiResponse = response.body();
                    callback.onSuccess(apiResponse);
                } else {
                    callback.onError("Error en la respuesta del servidor");
                }
            }

            @Override
            public void onFailure(Call<ResponseAddComidas> call, Throwable t) {
                callback.onError("Error en la solicitud: " + t.getMessage());
            }
        });
    }

    public interface ApiCallback {
        void onSuccess(ResponseAddComidas response);
        void onError(String message);
    }
}
