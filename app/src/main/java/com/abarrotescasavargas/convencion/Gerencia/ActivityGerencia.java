package com.abarrotescasavargas.convencion.Gerencia;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.convencion.BaseDatos.ConvencionContract;
import com.abarrotescasavargas.convencion.FunGenerales.CaptureActivityPortrait;
import com.abarrotescasavargas.convencion.FunGenerales.DB;
import com.abarrotescasavargas.convencion.FunGenerales.NetworkUtil;
import com.abarrotescasavargas.convencion.Gerencia.Reuniones.ActivityReuniones;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ActivitySugeridos;

import com.abarrotescasavargas.convencion.MainActivity;
import com.abarrotescasavargas.convencion.R;
import com.abarrotescasavargas.convencion.Staff.ActivityStaff;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityGerencia extends AppCompatActivity {
    Button reuniones, sugeridos;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerencia);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setup();
        events();
    }

    private void setup() {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(ActivityGerencia.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();

        }
        reuniones = findViewById(R.id.reuniones);
        sugeridos = findViewById(R.id.sugeridos);
    }

    private void events() {

        reuniones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ActivityGerencia.this, ActivityReuniones.class);
                startActivity(intent);
            }
        });

        sugeridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES);
                options.setPrompt("Escanear QR");
                options.setCameraId(0);
                options.setOrientationLocked(false);
                options.setBeepEnabled(true);
                options.setCaptureActivity(CaptureActivityPortrait.class);
                options.setBarcodeImageEnabled(false);
                BarcodeLauncher.launch(options);
            }
            private final ActivityResultLauncher<ScanOptions> BarcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
                if (result.getContents() != null) {
                    String escaneo = result.getContents();
                    DB transferenciasRepository = new DB(ActivityGerencia.this);
                    String valorUS_USUARI = transferenciasRepository.getUsuario();
                    intent = new Intent(ActivityGerencia.this, ActivitySugeridos.class);
                    intent.putExtra("CVE_SUC",valorUS_USUARI);
                    intent.putExtra("CVE_PRO",escaneo);
                    startActivity(intent);
                }
            });
        });
    }
}
