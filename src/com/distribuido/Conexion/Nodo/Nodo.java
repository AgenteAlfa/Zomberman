package com.distribuido.Conexion.Nodo;

import com.distribuido.Conexion.Abstractos.Conexion;
import com.distribuido.Conexion.Abstractos.Hilo;
import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Ventana.Controlador;

import java.io.IOException;
import java.net.Socket;


public class Nodo extends Conexion {

    protected final Comunicaciones mComunicacion;
    protected Controlador mListener;
    private char Data;

    private HEscritor Escritor;
    private HLector Lector;


    public Nodo() throws IOException, ClassNotFoundException {
        super(
                new Socket(
                        Configuracion.SERVER_IP,
                        Configuracion.PUERTO_CONEXION),
                new Socket(
                        Configuracion.SERVER_IP,
                        Configuracion.PUERTO_JUEGO));
        System.out.println(".....Ejecutando codigo NODO");
        System.out.println("Cree mis socket");
        getOOSConexion().writeObject(Configuracion.MI_IP);
        System.out.println("Envie ip");

        mComunicacion = (Comunicaciones) getOISJuego().readObject();
        System.out.println("Recibi data");
        mComunicacion.Status();


        Data = 0;
        System.out.println(".....TERMINANDO ejecutar codigo NODO");
    }
    protected void Empezar()
    {

        Escritor = new HEscritor();
        Escritor.start();
        Lector = new HLector();
        Lector.start();
    }


    protected void EnviarOrden(char ord)
    {
        try {
            getOOSConexion().writeObject(ord);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected char[] EscucharStatus()
    {
        char[] temp = new char[0];
        try {
            temp = (char[]) getOISJuego().readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }


    private class HLector extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            mListener.EjecutarOrdenes(Nodo.this.EscucharStatus());
        }
    }

    private class HEscritor extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            Nodo.this.EnviarOrden(setData((char) 0));
        }
    }

    public synchronized char setData(char data) {
        char temp = Data;
        Data = data;
        return temp;
    }

}
