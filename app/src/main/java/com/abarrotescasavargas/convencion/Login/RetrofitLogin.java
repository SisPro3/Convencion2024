package com.abarrotescasavargas.convencion.Login;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitLogin {
    static final String BASE_URL = "https://abarrotescasavargas.mx/api/convencion/android/";
    Retrofit retrofit;
    public RetrofitLogin() { }
    public Retrofit getRuta() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        return retrofit;
    }
}
