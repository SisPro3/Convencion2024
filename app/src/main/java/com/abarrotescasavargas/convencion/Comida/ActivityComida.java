package com.abarrotescasavargas.convencion.Comida;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.abarrotescasavargas.convencion.BaseDatos.DbHelper;
import com.abarrotescasavargas.convencion.FunGenerales.CaptureActivityPortrait;
import com.abarrotescasavargas.convencion.FunGenerales.NetworkUtil;
import com.abarrotescasavargas.convencion.R;
import com.bumptech.glide.Glide;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ActivityComida extends AppCompatActivity {
    ImageView escaner;
    SQLiteDatabase db;
    Retrofit retrofit;
    InterfaceComida apiService ;
    ImageButton roundButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comida);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setup();
        events();
    }
    private void setup()
    {
        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        escaner=findViewById(R.id.escaner);
        Glide.with(this).load(R.drawable.scanner).into(escaner);
        roundButton= findViewById(R.id.contador);

    }
    private void events()
    {
        roundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<RespuestaConteo> call1 = apiService.getDatos();
                call1.enqueue(new Callback<RespuestaConteo>() {
                    @Override
                    public void onResponse(Call<RespuestaConteo> call1, Response<RespuestaConteo> response) {
                        if (response.isSuccessful()) {
                            RespuestaConteo conteoResponse = response.body();
                            if (conteoResponse != null) {
                                int conteo = conteoResponse.getConteo();
                                Toast.makeText(ActivityComida.this,"Comidas registradas: "+conteo,Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Manejar errores de respuesta
                        }
                    }

                    @Override
                    public void onFailure(Call<RespuestaConteo> call1, Throwable t) {
                        // Manejar errores de conexión
                    }
                });
            }
        });
        escaner.setOnClickListener(new View.OnClickListener() {
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
                    if (NetworkUtil.isNetworkAvailable(getApplicationContext())) {

                        String escaneo = result.getContents();

                        retrofit = new Retrofit.Builder()
                                .baseUrl("https://abarrotescasavargas.mx/api/convencion/android/")
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        apiService = retrofit.create(InterfaceComida.class);

                        Call<RespuestaGetSaldo> call = apiService.getProveedorInfo(escaneo);

                        call.enqueue(new Callback<RespuestaGetSaldo>() {
                            @Override
                            public void onResponse(Call<RespuestaGetSaldo> call, Response<RespuestaGetSaldo> response) {
                                if (response.isSuccessful()) {
                                    RespuestaGetSaldo responseModel = response.body();
                                    if (responseModel != null) {
                                        String mensaje = responseModel.getMensaje();
                                        String codigo = responseModel.getCodigo();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityComida.this);
                                        builder.setTitle("Mensaje");

                                        if (codigo.equals("500")) {
                                            mensaje =  mensaje;
                                            SpannableString spannableString = new SpannableString(mensaje);
                                            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, mensaje.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                            builder.setMessage(spannableString);
                                        } else {
                                            builder.setMessage( mensaje );
                                        }

                                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // No hacer nada o realizar alguna acción si se hace clic en Aceptar
                                            }
                                        });

                                        // Mostrar el AlertDialog
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                } else {
                                    // Crear y mostrar el AlertDialog para mostrar el error de solicitud
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityComida.this);
                                    builder.setTitle("Error");
                                    builder.setMessage("Error en la solicitud: " + response.code());
                                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    // Mostrar el AlertDialog
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaGetSaldo> call, Throwable t) {
                                Log.v("Error al realizar la solicitud: " , t.getMessage());
                            }
                        });
                        Call<RespuestaConteo> call1 = apiService.getDatos();
                        call1.enqueue(new Callback<RespuestaConteo>() {
                            @Override
                            public void onResponse(Call<RespuestaConteo> call1, Response<RespuestaConteo> response) {
                                if (response.isSuccessful()) {
                                    RespuestaConteo conteoResponse = response.body();
                                    if (conteoResponse != null) {
                                        int conteo = conteoResponse.getConteo();
                                        Toast.makeText(ActivityComida.this,"Comidas registradas: "+conteo,Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Manejar errores de respuesta
                                }
                            }

                            @Override
                            public void onFailure(Call<RespuestaConteo> call1, Throwable t) {
                                // Manejar errores de conexión
                            }
                        });
                    } else {
                        Toast.makeText(ActivityComida.this, "No hay conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}
