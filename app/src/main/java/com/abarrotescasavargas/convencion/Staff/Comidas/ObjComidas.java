package com.abarrotescasavargas.convencion.Staff.Comidas;

public class ObjComidas {
    private String CVE_PROVEEDOR;
    private String CANTIDAD;

    public ObjComidas(String cve_proveedor, String cantidad) {
    this.CVE_PROVEEDOR=cve_proveedor;
    this.CANTIDAD=cantidad;
    }

    public String getCVE_PROVEEDOR() {
        return CVE_PROVEEDOR;
    }

    public void setCVE_PROVEEDOR(String CVE_PROVEEDOR) {
        this.CVE_PROVEEDOR = CVE_PROVEEDOR;
    }

    public String getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(String CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }
}
