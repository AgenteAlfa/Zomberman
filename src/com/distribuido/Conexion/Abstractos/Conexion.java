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

    private Socket Conexion,Juego;

    public Socket getConexion() {
        return Conexion;
    }

    public Socket getJuego() {
        return Juego;
    }

    public void setOISConexion(ObjectInputStream OISConexion) {
        this.OISConexion = OISConexion;
    }

    public void setOISJuego(ObjectInputStream OISJuego) {
        this.OISJuego = OISJuego;
    }

    public void setOOSConexion(ObjectOutputStream OOSConexion) {
        this.OOSConexion = OOSConexion;
    }

    public void setOOSJuego(ObjectOutputStream OOSJuego) {
        this.OOSJuego = OOSJuego;
    }

    public Conexion(Socket conexion , Socket juego)
    {
        Conexion = conexion;
        Juego = juego;
        Conectar(Conexion,Juego);
    }
    protected void Conectar(Socket Conexion , Socket Juego)
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
