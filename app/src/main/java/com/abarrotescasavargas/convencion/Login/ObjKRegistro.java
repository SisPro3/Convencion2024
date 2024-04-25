package com.abarrotescasavargas.convencion.Login;

import com.google.gson.annotations.SerializedName;

public class ObjKRegistro {
    @SerializedName("FOLIO")
    private String folio;
    @SerializedName("CVE_PROVEEDOR")
    private String cveProveedor;

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCveProveedor() {
        return cveProveedor;
    }

    public void setCveProveedor(String cveProveedor) {
        this.cveProveedor = cveProveedor;
    }
}
