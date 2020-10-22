package com.psAndSephirita.sportsspots.models;

public class Spots {

    private double latitude;
    private double longitude;
    private String descricao;
    private String foto;

    public Spots(){

    }

    public Spots(double latitude, double longitude, String descricao, String foto) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.descricao = descricao;
        this.foto = foto;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
