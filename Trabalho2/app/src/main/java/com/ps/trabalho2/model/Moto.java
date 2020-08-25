package com.ps.trabalho2.model;

import java.io.Serializable;

public class Moto implements Serializable {
    static int cont = 1;
    private int id;
    private String fabricante;
    private String modelo;
    private int cilindradas;

    public Moto(String fabricante, String modelo, int cilindradas) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.cilindradas = cilindradas;
        this.id = cont;
        cont++;
    }

    public static int getCont() {
        return cont;
    }

    public static void setCont(int cont) {
        Moto.cont = cont;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCilindradas() {
        return cilindradas;
    }

    public void setCilindradas(int cilindradas) {
        this.cilindradas = cilindradas;
    }
}
