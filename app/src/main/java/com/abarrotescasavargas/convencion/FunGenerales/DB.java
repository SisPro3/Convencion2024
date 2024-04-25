package com.abarrotescasavargas.convencion.FunGenerales;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.abarrotescasavargas.convencion.BaseDatos.ConvencionContract;
import com.abarrotescasavargas.convencion.BaseDatos.DbHelper;
import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.Productos;

import java.util.ArrayList;
import java.util.List;

public class DB {
    private Context context;
    private DbHelper dbHelper;
    private SQLiteDatabase database;

    public DB(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public Cursor ejecutarConsulta(String query) {
        return database.rawQuery(query, null);
    }


    public String getUsuario() {
        String valorUS_USUARI = null;  // Variable para almacenar el valor de US_USUARI
        open();

        String consulta = "SELECT DU_CVESUC FROM DataUser LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }
    public String gerUser() {
        String valorUS_USUARI = null;  // Variable para almacenar el valor de US_USUARI
        open();

        String consulta = "SELECT DU_FOLIOS FROM DataUser LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            valorUS_USUARI = cursor.getString(0);
        }

        close();

        return valorUS_USUARI;
    }
    public String getCveProveedor(String folio) {
        String cveProveedor = null;  // Variable para almacenar el valor de US_USUARI
        open();

        String consulta = "SELECT CVE_PROVEEDOR FROM KREGISTRO WHERE FOLIO = '"+folio+"' LIMIT 1;";
        Cursor cursor = ejecutarConsulta(consulta);

        if (cursor.moveToFirst()) {
            cveProveedor = cursor.getString(0);
        }

        close();

        return cveProveedor;
    }
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
}
