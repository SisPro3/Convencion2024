package com.abarrotescasavargas.convencion.Staff.Comidas;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.convencion.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.convencion.R;

import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityAddComidas extends AppCompatActivity {
    private Spinner selectUser;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText inputPasaje;
    private Button btnEnviar;
    private CustomProgressBar customProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.antyvity_add_comidas);

        setup();
        events();
        getProveedores();
    }

    private void setup() {
        btnEnviar = findViewById(R.id.button1);
        inputPasaje = findViewById(R.id.inputPasaje);
        selectUser = findViewById(R.id.spinnerComidas);
        autoCompleteTextView = findViewById(R.id.autoCompleteComidas);
        customProgressBar = new CustomProgressBar();

    }

    private void events() {
        btnEnviar.setOnClickListener(v -> {
            String cveProveedorStr = autoCompleteTextView.getText().toString();
            String cantidadStr = inputPasaje.getText().toString();

            if (!cveProveedorStr.isEmpty() && !cantidadStr.isEmpty()) {
                String[] partes = cveProveedorStr.split(" - ");
                String claveProveedor = partes[0];

                ApiClient apiClient = new ApiClient(ActivityAddComidas.this);
                apiClient.getSaldo(claveProveedor, Integer.parseInt(cantidadStr), new ApiClient.ApiCallback() {
                    @Override
                    public void onSuccess(ResponseAddComidas response) {
                        autoCompleteTextView.setText("");
                        inputPasaje.setText("");
                        Toast mensaje = Toast.makeText(ActivityAddComidas.this, response.getMensaje().toString(), Toast.LENGTH_SHORT);
                        mensaje.show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(ActivityAddComidas.this, message, Toast.LENGTH_SHORT).show();
                        Log.v("Errror 72", message);
                    }
                });
            } else {
                Toast.makeText(ActivityAddComidas.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getProveedores() {
        customProgressBar.showProgressBar(ActivityAddComidas.this, "Consultando..");
        ApiComidas apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiComidas.class);
        Call<List<ObjProveedor>> call = apiService.obtenerProveedores();
        call.enqueue(new Callback<List<ObjProveedor>>() {
            @Override
            public void onResponse(Call<List<ObjProveedor>> call, Response<List<ObjProveedor>> response) {
                customProgressBar.dismissProgressBar();
                if (response.isSuccessful()) {
                    List<ObjProveedor> proveedores = response.body();
                    List<String> nombresProveedores = proveedores.stream()
                            .map(proveedor -> proveedor.getKeyCve() + " - " + proveedor.getName())
                            .collect(Collectors.toList());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ActivityAddComidas.this, android.R.layout.simple_dropdown_item_1line, nombresProveedores);
                    autoCompleteTextView.setAdapter(adapter);
                } else {
                    Toast.makeText(ActivityAddComidas.this, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ObjProveedor>> call, Throwable t) {
                customProgressBar.dismissProgressBar();
                Toast.makeText(ActivityAddComidas.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
