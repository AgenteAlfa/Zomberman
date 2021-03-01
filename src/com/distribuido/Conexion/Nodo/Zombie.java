package com.distribuido.Conexion.Nodo;

import com.distribuido.Ventana.ControladorZombie;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Ventana;

import java.io.IOException;

public class Zombie extends Nodo implements ControladorZombie.Acciones{
    ControladorZombie mControlador;

    public Zombie() throws IOException, ClassNotFoundException {
        super();
        System.out.println("EJECUTANDO CODIGO ZOMIBIE");
        System.out.println("Debo enfocar :  " + (mComunicacion.Posicion + 1));

        mControlador = new ControladorZombie((Mapa) getOISJuego().readObject() , mComunicacion.Posicion + 1, this);
        System.out.println("CONTROLADOR ZOMBIECREADO");
        mListener = mControlador;
        System.out.println("Se recibio mapa");
        Empezar();
        System.out.println("TERMINANDO CODIGO ZOMIBIE");
    }

    @Override
    protected Mapa GetMapa() {
        return mControlador.getmMapa();
    }

    @Override
    public void BuscarCerebros(int direccion) {
        //Se envia la tecla al escenario
        System.out.println("Tecla " + direccion);
        setData((char) direccion);

    }
}
