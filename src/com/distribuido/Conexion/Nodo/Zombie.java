package com.distribuido.Conexion.Nodo;

import com.distribuido.Ventana.ControladorZombie;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Ventana;

import java.io.IOException;

public class Zombie extends Nodo{
    ControladorZombie mControlador;

    public Zombie() throws IOException, ClassNotFoundException {
        super();
        System.out.println("EJECUTANDO CODIGO ZOMIBIE");
        System.out.println("Debo enfocar :  " + (mComunicacion.Posicion + 1));

        mControlador = new ControladorZombie((Mapa) getOISJuego().readObject() , mComunicacion.Posicion);
        System.out.println("CONTROLADOR ZOMBIECREADO");
        mListener = mControlador;
        System.out.println("Se recibio mapa");
        Empezar();
        System.out.println("TERMINANDO CODIGO ZOMIBIE");
    }
}
