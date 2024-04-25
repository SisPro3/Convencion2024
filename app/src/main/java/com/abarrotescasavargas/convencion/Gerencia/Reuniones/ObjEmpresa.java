package com.abarrotescasavargas.convencion.Gerencia.Reuniones;

public class ObjEmpresa {
    private String Proveedor;
    private String name;
    private String Status;

    public ObjEmpresa(String proveedor, String name, String status) {
        this.Proveedor = proveedor;
        this.name = name;
        this.Status =status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String proveedor) {
        this.Proveedor = proveedor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
