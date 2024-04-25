package com.abarrotescasavargas.convencion.Staff.Comidas;

import com.google.gson.annotations.SerializedName;

public class ObjProveedor {
    @SerializedName("provider_id")
    private String providerId;

    @SerializedName("key_cve")
    private String keyCve;

    @SerializedName("name")
    private String name;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getKeyCve() {
        return keyCve;
    }

    public void setKeyCve(String keyCve) {
        this.keyCve = keyCve;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
