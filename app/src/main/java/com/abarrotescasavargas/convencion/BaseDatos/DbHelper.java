package com.abarrotescasavargas.convencion.BaseDatos;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.abarrotescasavargas.convencion.Gerencia.Sugeridos.Productos;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4   ;
    private static final String DATABASE_NOMBRE = "Convencion.db";
    Context context;

    static String DataUser = "CREATE TABLE " + ConvencionContract.DataUser.Table + " (" +
            ConvencionContract.DataUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ConvencionContract.DataUser.DU_CVESUC + " TEXT NOT NULL," +
            ConvencionContract.DataUser.DU_SUCURS + " TEXT NOT NULL," +
            ConvencionContract.DataUser.DU_PUESTO + " TEXT NOT NULL," +
            ConvencionContract.DataUser.DU_FOLIOS + " TEXT NOT NULL); ";
    static String SUGERIDOS = "CREATE TABLE " + ConvencionContract.SUGERIDOS.Table + " (" +
            ConvencionContract.SUGERIDOS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ConvencionContract.SUGERIDOS.SU_CVEPRO + " TEXT NOT NULL," +
            ConvencionContract.SUGERIDOS.SU_CVEART + " TEXT NOT NULL," +
            ConvencionContract.SUGERIDOS.SU_NOMART + " TEXT NOT NULL," +
            ConvencionContract.SUGERIDOS.SU_EXISTE + " TEXT NOT NULL," +
            ConvencionContract.SUGERIDOS.SU_SUGERI + " TEXT NOT NULL," +
            ConvencionContract.SUGERIDOS.SU_SUGMES + " TEXT NOT NULL); ";
    static String KREGISTRO = "CREATE TABLE " + ConvencionContract.KREGISTRO.Table + " (" +
            ConvencionContract.KREGISTRO._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            ConvencionContract.KREGISTRO.FOLIO + " TEXT NOT NULL," +
            ConvencionContract.KREGISTRO.CVE_PROVEEDOR + " TEXT NOT NULL);" ;
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataUser);
        db.execSQL(SUGERIDOS);
        db.execSQL(KREGISTRO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DbHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + ConvencionContract.DataUser.Table);
        onCreate(db);
    }
    public List<Productos> obtenerSugeridosDesdeDB(String cveArtFiltro) {
        List<Productos> sugeridos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "lower(" + ConvencionContract.SUGERIDOS.SU_CVEPRO + ") = ?";
        String[] selectionArgs = new String[] { cveArtFiltro.toLowerCase() };

        Cursor cursor = db.query(ConvencionContract.SUGERIDOS.Table,
                new String[]{ConvencionContract.SUGERIDOS.SU_CVEPRO,ConvencionContract.SUGERIDOS.SU_CVEART, ConvencionContract.SUGERIDOS.SU_NOMART, ConvencionContract.SUGERIDOS.SU_EXISTE, ConvencionContract.SUGERIDOS.SU_SUGERI, ConvencionContract.SUGERIDOS.SU_SUGMES},
                selection, selectionArgs, null, null, null);

        int idxCvePrp = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_CVEPRO);
        int idxCveArt = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_CVEART);
        int idxNomArt = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_NOMART);
        int idxExiste = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_EXISTE);
        int idxSugeri = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_SUGERI);
        int idxSugMes = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_SUGMES);
        int idxSugCom = cursor.getColumnIndex(ConvencionContract.SUGERIDOS.SU_COMENT);

        if (cursor.moveToFirst()) {
            do {
                if (idxCveArt != -1 && idxNomArt != -1 && idxExiste != -1 && idxSugeri != -1 && idxSugMes != -1) {
                    Productos sugerido = new Productos(
                            cursor.getString(idxCvePrp),
                            cursor.getString(idxCveArt),
                            cursor.getString(idxNomArt),
                            cursor.getString(idxExiste),
                            cursor.getString(idxSugeri),
                            cursor.getString(idxSugMes),
                            cursor.getString(idxSugCom)
                    );
                    sugeridos.add(sugerido);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return sugeridos;
    }


}
