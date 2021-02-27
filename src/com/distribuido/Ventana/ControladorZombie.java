package com.distribuido.Ventana;

import com.distribuido.Conexion.Nodo.Nodo;

import java.io.IOException;

public class ControladorZombie extends Nodo implements Controlador{

    private Mapa mMapa;
    private final int Foco;
    private int[] PosicionFoco;

    public ControladorZombie(Mapa mapa, int enfoque) throws IOException, ClassNotFoundException {
        mMapa = mapa;
        Foco = enfoque;
    }

    @Override
    public void EjecutarOrdenes(char[] data) {

    }
}
