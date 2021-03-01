package com.distribuido.Conexion.Abstractos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

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

    public Conexion(SocketAddress Aconexion, SocketAddress Ajuego)
    {
        Conexion = new Socket();
        Juego = new Socket();

        try {
            Conexion.connect(Aconexion,3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Juego.connect(Ajuego,3000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Conectar(Conexion,Juego);
    }

    public Conexion(Socket conexion, Socket juego)
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
