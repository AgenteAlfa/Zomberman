package com.distribuido.Conexion.Nodo;

import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Ventana;

import java.io.IOException;

public class Jugador extends Nodo implements Ventana.AccionesJugador {

    private final Ventana mVentana;
    public Jugador(int n) throws IOException, ClassNotFoundException {
        super(n);
        //Preparar ventana
        System.out.println("Debo enfocar :  " + (mComunicacion.Posicion + 1));

        mVentana = new Ventana((Mapa) getOISJuego().readObject(),mComunicacion.Posicion + 1,this);
        mListener = mVentana;
        System.out.println("Se recibio mapa");
        Empezar();
    }

    public void Cerrar(){
       mVentana.Cerrar();
    }

    @Override
    protected Mapa GetMapa() {
        return mVentana.getmMapa();
    }

    @Override
    public void Moverse(int tecla) {
        //Se envia la tecla al escenario
        //System.out.println("Tecla " + tecla);
        setData((char) tecla);
        //mVentana.getmMapa().Imprimir();
    }


}
