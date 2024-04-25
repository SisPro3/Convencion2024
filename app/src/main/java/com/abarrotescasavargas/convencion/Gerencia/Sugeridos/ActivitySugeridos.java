package com.abarrotescasavargas.convencion.Gerencia.Sugeridos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abarrotescasavargas.convencion.BaseDatos.DbHelper;
import com.abarrotescasavargas.convencion.FunGenerales.DB;
import com.abarrotescasavargas.convencion.FunGenerales.NetworkUtil;
import com.abarrotescasavargas.convencion.Gerencia.ActivityGerencia;
import com.abarrotescasavargas.convencion.Gerencia.Reuniones.ActivityReuniones;
import com.abarrotescasavargas.convencion.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivitySugeridos extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextViewSugeridos;
    private TextView contadorSugeridos;
    private AdapterSugeridos adapterSugeridos;
    private RecyclerView recyclerSugeridos;
    private Context context;
    private String CVE_SUC, CVE_PRO = "";
    private ApiService apiService;
    private Handler handler = new Handler();
    private Runnable filterRunnable;
    private Spinner spinnerSugeridos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugeridos);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setup();
        events();
    }

    private void setup() {
        context=this;
        CVE_SUC = getIntent().getStringExtra("CVE_SUC");
        CVE_PRO = getIntent().getStringExtra("CVE_PRO");//Resultado del escaneo del QR

        autoCompleteTextViewSugeridos = findViewById(R.id.autoCompleteTextViewSugeridos);
        contadorSugeridos = findViewById(R.id.contadorSugeridos);
        recyclerSugeridos = findViewById(R.id.recyclerSugeridos);
        spinnerSugeridos = findViewById(R.id.spinnerSugeridos);
        recyclerSugeridos.setLayoutManager(new LinearLayoutManager(this));

        String[] meses = {"MARZO", "ABRIL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, meses);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSugeridos.setAdapter(adapter);

        if (!NetworkUtil.isNetworkAvailable(this)) {
            DbHelper dbHelper = new DbHelper(this);

            DB transferenciasRepository = new DB(context);
            String clave = transferenciasRepository.getCveProveedor(CVE_PRO);
            List<Productos> productos = dbHelper.obtenerSugeridosDesdeDB(clave);

            adapterSugeridos = new AdapterSugeridos(ActivitySugeridos.this, productos);
            recyclerSugeridos.setAdapter(adapterSugeridos);
            contadorSugeridos.setText(String.valueOf(adapterSugeridos.getItemCount())); // Convertir el contador a cadena
            Toast.makeText(ActivitySugeridos.this, "Mostrando sugeridos locales", Toast.LENGTH_SHORT).show();
        } else {
            apiService = getInstance();
            getSugeridos(CVE_SUC, CVE_PRO);
        }
    }



    private ApiService getInstance() {
        if (apiService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://abarrotescasavargas.mx/api/convencion/android/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

    private void getSugeridos(String CVE_SUC, String CVE_PRO) {
        Call<List<Productos>> call = apiService.obtenerSugeridos(CVE_SUC, CVE_PRO);

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful()) {
                    List<Productos> productos = response.body();

                    adapterSugeridos = new AdapterSugeridos(ActivitySugeridos.this, productos);
                    recyclerSugeridos.setAdapter(adapterSugeridos);
                    contadorSugeridos.setText(String.valueOf(adapterSugeridos.getItemCount()));
                } else {
                    ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                    Toast.makeText(ActivitySugeridos.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {

                try {
                    DbHelper dbHelper = new DbHelper(ActivitySugeridos.this);
                    DB transferenciasRepository = new DB(context);
                    String clave = transferenciasRepository.getCveProveedor(CVE_PRO);
                    List<Productos> productos = dbHelper.obtenerSugeridosDesdeDB(clave);

                    adapterSugeridos = new AdapterSugeridos(ActivitySugeridos.this, productos);
                    recyclerSugeridos.setAdapter(adapterSugeridos);
                    contadorSugeridos.setText(String.valueOf(adapterSugeridos.getItemCount())); // Convertir el contador a cadena
                    Toast.makeText(ActivitySugeridos.this, "Mostrando sugeridos locales", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    finish();
                    Toast.makeText(ActivitySugeridos.this, "No se encontró el valor "+CVE_PRO+" para el folio proporcionado.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void events() {
        spinnerSugeridos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String mesSeleccionado = (String) parentView.getItemAtPosition(position);
                if (adapterSugeridos != null) {
                    adapterSugeridos.filtrarPorMes(mesSeleccionado);
                    contadorSugeridos.setText(String.valueOf(adapterSugeridos.getItemCount()));
                } else {
                    Log.e("ActivitySugeridos", "El adaptador es nulo");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
        autoCompleteTextViewSugeridos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }

                filterRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String searchText = s.toString();
                        adapterSugeridos.filterBySucursal(searchText);
                        contadorSugeridos.setText("Total de registros: " + adapterSugeridos.getItemCount());
                    }
                };

                handler.postDelayed(filterRunnable, 100);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No es necesario implementar este método aquí
            }
        });
    }

}
