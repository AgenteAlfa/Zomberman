package com.distribuido.Conexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Comunicaciones extends ArrayList<String> implements Serializable {

    public final int Posicion;
    public int Actual;
    public int Jugadores;

    public Comunicaciones(Collection<? extends String> c, int posicion, int actual, int jugadores) {
        super(c);
        Posicion = posicion;
        Actual = actual;
        Jugadores = jugadores;
    }

    public void Status()
    {
        System.out.println("Soy nodo " + Posicion + " de " + size() + " vamos " + Actual);
    }
}
