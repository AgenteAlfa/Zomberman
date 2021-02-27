package com.distribuido.Ventana;


import java.io.IOException;

public class ControladorZombie implements Controlador{

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
