package com.abarrotescasavargas.convencion.BaseDatos;

import android.provider.BaseColumns;

public class ConvencionContract {

    public static class DataUser implements BaseColumns {
        public static final String Table = "DataUser";
        public static final String DU_CVESUC = "DU_CVESUC";
        public static final String DU_SUCURS = "DU_SUCURS";
        public static final String DU_FOLIOS = "DU_FOLIOS";
        public static final String DU_PUESTO = "DU_PUESTO";

    }
    public static class SUGERIDOS implements BaseColumns {
        public static final String Table = "SUGERIDOS";
        public static final String SU_CVEPRO = "SU_CVEPRO";
        public static final String SU_CVEART = "SU_CVEART";
        public static final String SU_NOMART = "SU_NOMART";
        public static final  String SU_EXISTE ="SU_EXISTE";
        public static final  String SU_SUGERI ="SU_SUGERI";
        public static final String SU_SUGMES = "SU_SUGMES";
        public static final String SU_COMENT = "SU_COMENT";
    }
    public static class KREGISTRO implements BaseColumns {
        public static final String Table = "KREGISTRO";
        public static final String FOLIO = "FOLIO";
        public static final String CVE_PROVEEDOR = "CVE_PROVEEDOR";

    }
}
