package com.abarrotescasavargas.convencion;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.abarrotescasavargas.convencion.BaseDatos.ConvencionContract;
import com.abarrotescasavargas.convencion.BaseDatos.DbHelper;
import com.abarrotescasavargas.convencion.Comida.ActivityComida;
import com.abarrotescasavargas.convencion.FunGenerales.CaptureActivityPortrait;
import com.abarrotescasavargas.convencion.FunGenerales.CustomProgressBar;
import com.abarrotescasavargas.convencion.FunGenerales.DB;
import com.abarrotescasavargas.convencion.FunGenerales.NetworkUtil;
import com.abarrotescasavargas.convencion.Gerencia.ActivityGerencia;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ActivitySugeridos;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.AdapterSugeridos;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ErrorResponse;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.Productos;
import com.abarrotescasavargas.convencion.Login.ApiService;
import com.abarrotescasavargas.convencion.Login.ObjAsistente;
import com.abarrotescasavargas.convencion.Login.ObjKRegistro;
import com.abarrotescasavargas.convencion.Login.RetrofitLogin;

import retrofit2.Callback;
import retrofit2.converter.gson.GsonConverterFactory;

import com.abarrotescasavargas.convencion.Staff.ActivityStaff;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {
    ImageView escaner;
    SQLiteDatabase db;
    private CustomProgressBar customProgressBar;
    private com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ApiService apiService1;
    Retrofit retrofitURL = new RetrofitLogin().getRuta();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setup();

    }
    private void setup()
    {
        escaner=findViewById(R.id.escaner);
        Glide.with(this).load(R.drawable.scanner).into(escaner);
        customProgressBar = new CustomProgressBar();
        DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();
        apiService1 = getInstance();

        Cursor cursor = db.query(ConvencionContract.DataUser.Table, new String[]{ConvencionContract.DataUser.DU_PUESTO}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idxCvePrp = cursor.getColumnIndex(ConvencionContract.DataUser.DU_PUESTO);
            String puesto = cursor.getString(idxCvePrp);
            cursor.close();
            abreVentana(puesto);
        } else {
            events();
        }
    }

    private com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ApiService getInstance() {
        if (apiService1 == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://abarrotescasavargas.mx/api/convencion/android/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiService1 = retrofit.create(com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ApiService.class);
        }
        return apiService1;
    }

    private void events()
    {
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

                    String escaneo = result.getContents();

                    getAsistenteData(escaneo, new AsistenteDataCallback() {
                        @Override
                        public void onDataReceived(Map<String, Object> data) {
                            List<Map<String, Object>> asistentesList = (List<Map<String, Object>>) data.get("asistentes");
                            StringBuilder folios = new StringBuilder();
                            for (Map<String, Object> asistente : asistentesList) {
                                db.delete(ConvencionContract.DataUser.Table,null,null);
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(ConvencionContract.DataUser.DU_FOLIOS, escaneo);
                                contentValues.put(ConvencionContract.DataUser.DU_CVESUC, (String) asistente.get("cve_sucursal"));
                                contentValues.put(ConvencionContract.DataUser.DU_SUCURS, (String) asistente.get("desc_sucursal"));
                                contentValues.put(ConvencionContract.DataUser.DU_PUESTO, (String) asistente.get("puesto"));
                                db.insertOrThrow(ConvencionContract.DataUser.Table, null, contentValues);
                                abreVentana((String) asistente.get("puesto"));
                            }
                        }
                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {

                }
            });
        });
    }

    private void abreVentana(String perfil)
    {
        if (!NetworkUtil.isNetworkAvailable(this)) {
            Toast.makeText(MainActivity.this, "No hay conexión a Internet", Toast.LENGTH_SHORT).show();
        }
        DB transferenciasRepository = new DB(MainActivity.this);
        String valorUS_USUARI = transferenciasRepository.getUsuario();


        Intent intent;
        customProgressBar.showProgressBar(MainActivity.this, "Consultando..");
        switch (perfil) {

            case "COMIDAS":
                intent = new Intent(this, ActivityComida.class);
                startActivity(intent);
                finish();

                break;
            case "GERENTE DE SUCURSAL":
            case "SUBGERENTE DE SUCURSAL":
            case "RESPONSABLE DE CREMERIA":
            case "SUPERVISOR DE VENTAS":
                getSugeridos(valorUS_USUARI);
                getKREGISTROS();
                intent= new Intent(this, ActivityGerencia.class);
                startActivity(intent);
                finish();

                break;
            case "STAFF":
                intent = new Intent(this, ActivityStaff.class);
                startActivity(intent);
                finish();

                break;
            default:
                Toast.makeText(MainActivity.this, "Permisos no validos", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public interface AsistenteDataCallback {
        void onDataReceived(Map<String, Object> data);
        void onError(String errorMessage);
    }
    private void getAsistenteData(String folio, AsistenteDataCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(retrofitURL.baseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ObjAsistente.ApiResponse> call = apiService.obtenerDatos(folio);
        call.enqueue(new Callback<ObjAsistente.ApiResponse>() {
            @Override
            public void onResponse(Call<ObjAsistente.ApiResponse> call, Response<ObjAsistente.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ObjAsistente.ApiResponse apiResponse = response.body();
                    List<ObjAsistente.Asistente> asistentes = apiResponse.getAsistente();

                    List<Map<String, Object>> asistentesData = new ArrayList<>();
                    for (ObjAsistente.Asistente asistente : asistentes) {
                        Map<String, Object> asistenteMap = new HashMap<>();
                        asistenteMap.put("cve_sucursal", asistente.getCve_sucursal());
                        asistenteMap.put("desc_sucursal", asistente.getDesc_sucursal());
                        asistenteMap.put("nombre", asistente.getNombre());
                        asistenteMap.put("cve_empleado", asistente.getCve_empleado());
                        asistenteMap.put("celular", asistente.getCelular());
                        asistenteMap.put("correo", asistente.getCorreo());
                        asistenteMap.put("puesto", asistente.getPuesto());
                        asistenteMap.put("folio", asistente.getFolio());
                        asistentesData.add(asistenteMap);
                    }

                    callback.onDataReceived(Collections.singletonMap("asistentes", asistentesData));
                } else {
                    String errorResponse = "Error: Response unsuccessful";
                    callback.onError(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<ObjAsistente.ApiResponse> call, Throwable t) {
                callback.onError("Error: " + t.getMessage());
            }
        });
    }
    private void insertarSugeridosEnDb(List<Productos> sugeridos) {
        DbHelper dbHelper = new DbHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + ConvencionContract.SUGERIDOS.Table);
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Productos sugerido : sugeridos) {
                values.clear();
                values.put(ConvencionContract.SUGERIDOS.SU_CVEPRO, sugerido.getClavePro());
                values.put(ConvencionContract.SUGERIDOS.SU_CVEART, sugerido.getClave());
                values.put(ConvencionContract.SUGERIDOS.SU_NOMART, sugerido.getNombre());
                values.put(ConvencionContract.SUGERIDOS.SU_EXISTE, sugerido.getExistencia());
                values.put(ConvencionContract.SUGERIDOS.SU_SUGERI, sugerido.getSugerido());
                values.put(ConvencionContract.SUGERIDOS.SU_SUGMES, sugerido.getMes());
                db.insertOrThrow(ConvencionContract.SUGERIDOS.Table, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }
    private void getSugeridos(String CVE_SUC) {
        Call<List<Productos>> call = apiService1.obtenerAllSugeridos(CVE_SUC);

        call.enqueue(new Callback<List<Productos>>() {
            @Override
            public void onResponse(Call<List<Productos>> call, Response<List<Productos>> response) {
                if (response.isSuccessful()) {
                    List<Productos> productos = response.body();
                    insertarSugeridosEnDb(productos);

                } else {
                    ErrorResponse errorResponse = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                    Toast.makeText(MainActivity.this, errorResponse.getError(), Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<Productos>> call, Throwable t) {
                Intent intent = new Intent(MainActivity.this, ActivityGerencia.class);
                startActivity(intent);
                finish();
                Toast.makeText(MainActivity.this, "Algo salio mal.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getKREGISTROS(){
        //Implementacion de ultima hora, corregir siguiendo estandares para el siguiente año
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://abarrotescasavargas.mx/api/convencion/android/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService1 = retrofit.create(com.abarrotescasavargas.convencion.Gerencia.Sugeridos.ApiService.class);

        // Realizar la llamada al endpoint
        Call<List<ObjKRegistro>> call = apiService1.getKRegistros();
        call.enqueue(new Callback<List<ObjKRegistro>>() {
            @Override
            public void onResponse(Call<List<ObjKRegistro>> call, Response<List<ObjKRegistro>> response) {
                if (response.isSuccessful()) {
                    List<ObjKRegistro> registros = response.body();
                    // Insertar los datos en la base de datos SQLite
                    insertarRegistrosEnSQLite(registros);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<ObjKRegistro>> call, Throwable t) {
                Log.v("Error",t.toString());
            }
        });
    }
    private void insertarRegistrosEnSQLite(List<ObjKRegistro> registros) {
        // Inicializar la base de datos SQLite
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ObjKRegistro registro : registros) {
                ContentValues values = new ContentValues();
                values.put(ConvencionContract.KREGISTRO.FOLIO, registro.getFolio());
                values.put(ConvencionContract.KREGISTRO.CVE_PROVEEDOR, registro.getCveProveedor());
                db.insertOrThrow(ConvencionContract.KREGISTRO.Table, null, values);
            }
            // Marcar la transacción como exitosa
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("Error", "Error al insertar registros en la base de datos SQLite: " + e.getMessage());
        } finally {
            // Finalizar la transacción
            db.endTransaction();
            // Cerrar la conexión a la base de datos
            db.close();
        }
    }


}