package com.abarrotescasavargas.convencion.Staff.Registrados;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.convencion.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityRegistrados extends AppCompatActivity {
    private ProgressBar progressBar1, progressBar2, progressBar3, progressBar4;
    private TextView textViewProgress1, textViewProgress2, textViewProgress3, textViewProgress4;
    Retrofit retrofit ;

    ApiRegistrados apiService ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrados);

        setup();

        getComidasAgrupadas();
        getReuniones();
        getRegistrados();
    }
    private void setup()
    {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/api/convencion/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiRegistrados.class);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar2 = findViewById(R.id.progressBar2);
        progressBar3 = findViewById(R.id.progressBar3);
        progressBar4 = findViewById(R.id.progressBar4);


        textViewProgress1 = findViewById(R.id.textViewProgress1);
        textViewProgress2 = findViewById(R.id.textViewProgress2);
        textViewProgress3 = findViewById(R.id.textViewProgress3);
        textViewProgress4 = findViewById(R.id.textViewProgress4);

    }
    private void getComidasAgrupadas() {
        Call<List<ObjComidasAgrupadas>> call = apiService.obtenerDatos();
        call.enqueue(new Callback<List<ObjComidasAgrupadas>>() {
            @Override
            public void onResponse(Call<List<ObjComidasAgrupadas>> call, Response<List<ObjComidasAgrupadas>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ObjComidasAgrupadas> datosList = response.body();

                    if (datosList.size() >= 2) {
                        ObjComidasAgrupadas datos1 = datosList.get(0);
                        ObjComidasAgrupadas datos2 = datosList.get(1);

                        textViewProgress1.setText("Comidas \n Grupo: "+datos1.getIdentificador()+"\n"+datos1.getTotalSaldo() + " de " + datos1.getTotalComidas());
                        progressBar1.setMax(Integer.parseInt(datos1.getTotalComidas()));
                        progressBar1.setProgress(Integer.parseInt(datos1.getTotalSaldo()));

                        textViewProgress2.setText("Comidas \n Grupo: "+datos2.getIdentificador()+"\n"+datos2.getTotalSaldo() + " de " + datos2.getTotalComidas());
                        progressBar2.setMax(Integer.parseInt(datos2.getTotalComidas()));
                        progressBar2.setProgress(Integer.parseInt(datos2.getTotalSaldo()));
                    }
                } else {
                    Log.e("ERROR", "Error en la respuesta del servidor: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ObjComidasAgrupadas>> call, Throwable t) {
                Log.e("ERROR", "Error al realizar la llamada: " + t.getMessage());
            }
        });
    }
    private void getRegistrados()
    {
        Call<ObjEsperadosRegistrados> call = apiService.getRegistrados();
        call.enqueue(new Callback<ObjEsperadosRegistrados>() {
            @Override
            public void onResponse(Call<ObjEsperadosRegistrados> call, Response<ObjEsperadosRegistrados> response) {
                if (response.isSuccessful()) {
                    ObjEsperadosRegistrados datosResponse = response.body();
                    if (datosResponse != null) {
                        int esperados = datosResponse.getEsperados();
                        int registrados = datosResponse.getRegistrados();


                        textViewProgress4.setText("Registrados: \n"+registrados + " de " + esperados);
                        progressBar4.setMax(esperados);
                        progressBar4.setProgress(registrados);
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ObjEsperadosRegistrados> call, Throwable t) {
                // Manejar errores de conexión
            }
        });
    }
    private void getReuniones()
    {
        Call<ObjRegistrados> call = apiService.getMeetingStats();
        call.enqueue(new Callback<ObjRegistrados>() {
            @Override
            public void onResponse(Call<ObjRegistrados> call, Response<ObjRegistrados> response) {
                if (response.isSuccessful()) {
                    ObjRegistrados meetingStats = response.body();
                    if (meetingStats != null) {
                        int totalMeetings = meetingStats.getTotal_reuniones();
                        int completedMeetings = meetingStats.getReuniones_completadas();


                            textViewProgress3.setText("Reuniones: \n"+completedMeetings + " de " + totalMeetings);
                        progressBar3.setMax(totalMeetings);
                        progressBar3.setProgress(completedMeetings);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ObjRegistrados> call, Throwable t) {

            }
        });
    }
}
