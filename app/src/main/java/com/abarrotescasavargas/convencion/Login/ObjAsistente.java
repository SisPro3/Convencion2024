package com.abarrotescasavargas.convencion.Login;

import java.util.List;

public class ObjAsistente {
    public class ApiResponse {
        private boolean status;
        private String message;
        private List<Asistente> asistente;
        private String header;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Asistente> getAsistente() {
            return asistente;
        }

        public void setAsistente(List<Asistente> asistente) {
            this.asistente = asistente;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }
    }

    public class Asistente {
        private String cve_sucursal;
        private String desc_sucursal;
        private String nombre;
        private String cve_empleado;
        private String celular;
        private String correo;
        private String puesto;
        private String folio;

        public String getCve_sucursal() {
            return cve_sucursal;
        }

        public void setCve_sucursal(String cve_sucursal) {
            this.cve_sucursal = cve_sucursal;
        }

        public String getDesc_sucursal() {
            return desc_sucursal;
        }

        public void setDesc_sucursal(String desc_sucursal) {
            this.desc_sucursal = desc_sucursal;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getCve_empleado() {
            return cve_empleado;
        }

        public void setCve_empleado(String cve_empleado) {
            this.cve_empleado = cve_empleado;
        }

        public String getCelular() {
            return celular;
        }

        public void setCelular(String celular) {
            this.celular = celular;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getPuesto() {
            return puesto;
        }

        public void setPuesto(String puesto) {
            this.puesto = puesto;
        }

        public String getFolio() {
            return folio;
        }

        public void setFolio(String folio) {
            this.folio = folio;
        }
    }

}
