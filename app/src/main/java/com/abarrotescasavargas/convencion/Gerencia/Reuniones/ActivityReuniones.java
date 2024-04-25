package com.abarrotescasavargas.convencion.Gerencia.Reuniones;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.convencion.BaseDatos.ConvencionContract;
import com.abarrotescasavargas.convencion.BaseDatos.DbHelper;
import com.abarrotescasavargas.convencion.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.convencion.FunGenerales.NetworkUtil;
import com.abarrotescasavargas.convencion.Login.ApiService;
import com.abarrotescasavargas.convencion.MainActivity;
import com.abarrotescasavargas.convencion.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityReuniones extends AppCompatActivity {
    Spinner spinner;
    RecyclerView recyclerView;
    List<ObjEmpresa> listaDeReuniones;
    AdapterReuniones adapter;
    private TextView contador;
    SQLiteDatabase db;
    private CustomProgressBar customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reuniones);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setup();
        if (!NetworkUtil.isNetworkAvailable(this)) {
            finish();
            Toast.makeText(ActivityReuniones.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
            return;
        } else {
            getBitacora();
        }

        events();
    }

    private void setup() {
        customProgressBar = new CustomProgressBar();
        customProgressBar.showProgressBar(ActivityReuniones.this, "Consultando..");
        spinner = findViewById(R.id.selecionaSpinner);
        contador = findViewById(R.id.contador);

        recyclerView = findViewById(R.id.recyclerReuniones);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void events() {

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = spinner.getSelectedItem().toString();
                int parametroFiltrado = -1;

                switch (selectedOption) {
                    case "Pendientes":
                        parametroFiltrado = 0;
                        break;
                    case "Atendidos":
                        parametroFiltrado = 1;
                        break;
                    case "Todos":
                        parametroFiltrado = -1;
                        break;
                }
                if (adapter != null) {
                    adapter.setParametroFiltrado(parametroFiltrado);
                    contador.setText(String.valueOf("Conteo de registros: \n"+adapter.getItemCount()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    private String getDataUser() {
        String cveEmpleado = "";

        Cursor cursor = db.rawQuery("SELECT * FROM DataUser", null);

        if (cursor.moveToFirst()) {
            do {
                    cveEmpleado = cursor.getString(4);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cveEmpleado;
    }

    private void getBitacora() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InterfazReuniones apiService = retrofit.create(InterfazReuniones.class);

        Call<List<ObjEmpresa>> call = apiService.getBitacora(getDataUser());

        call.enqueue(new Callback<List<ObjEmpresa>>() {
            @Override
            public void onResponse(Call<List<ObjEmpresa>> call, Response<List<ObjEmpresa>> response) {
                if (response.isSuccessful()) {
                    List<ObjEmpresa> bitacoraList = response.body();

                    listaDeReuniones = new ArrayList<>();

                    if (bitacoraList != null) {
                        for (ObjEmpresa empresa : bitacoraList) {
                            listaDeReuniones.add(empresa);
                        }
                    }
                    adapter = new AdapterReuniones(listaDeReuniones, ActivityReuniones.this);
                    if (adapter != null) {
                        contador.setText(String.valueOf("Conteo de registros: \n"+adapter.getItemCount()));
                    }
                    recyclerView.setAdapter(adapter);
                    customProgressBar.dismissProgressBar(); // Mover aquí
                } else {
                    Log.e("Error", "Error en la respuesta del servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<ObjEmpresa>> call, Throwable t) {
                Log.e("Error", "Error en la solicitud: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }
}
