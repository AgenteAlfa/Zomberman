package com.distribuido.Conexion.Abstractos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Conexion {
    protected ObjectInputStream getOISConexion() {
        return OISConexion;
    }

    protected ObjectInputStream getOISJuego() {
        return OISJuego;
    }

    protected ObjectOutputStream getOOSConexion() {
        return OOSConexion;
    }

    protected ObjectOutputStream getOOSJuego() {
        return OOSJuego;
    }

    private ObjectInputStream OISConexion , OISJuego;
    private ObjectOutputStream OOSConexion , OOSJuego;

    public Conexion(Socket Conexion , Socket Juego)
    {
        try {
            OOSConexion = new ObjectOutputStream(Conexion.getOutputStream());
            OOSJuego = new ObjectOutputStream(Juego.getOutputStream());
            OISConexion = new ObjectInputStream(Conexion.getInputStream());
            OISJuego = new ObjectInputStream(Juego.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
