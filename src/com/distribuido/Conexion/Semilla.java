package com.distribuido.Conexion;

import com.distribuido.Ventana.Mapa;

public class Semilla {
    public Mapa mMapa;
    public Comunicaciones mComunicaciones;

    public Semilla(Mapa mMapa, Comunicaciones mComunicaciones) {
        this.mMapa = mMapa;
        this.mComunicaciones = mComunicaciones;
    }
}
