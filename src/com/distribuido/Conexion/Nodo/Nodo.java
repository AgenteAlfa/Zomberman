package com.distribuido.Conexion.Nodo;

import com.distribuido.Conexion.Abstractos.Conexion;
import com.distribuido.Conexion.Abstractos.Hilo;
import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Ventana.Controlador;
import com.distribuido.Ventana.Sim_Obj;

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
        Sim_Obj.ZOMBIE = mComunicacion.Jugadores + 1;
        System.out.println(Sim_Obj.ZOMBIE);
        Sim_Obj.JUGADOR_BOOM = mComunicacion.size() + 1;
        System.out.println(Sim_Obj.JUGADOR_BOOM);
        Sim_Obj.ZOMBIE_BOOM = mComunicacion.size() + 2;
        System.out.println(Sim_Obj.ZOMBIE_BOOM);

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


    protected void EnviarOrden(char ord) throws IOException {
        if(ord != 0)
        System.out.println("Se envia : " + ord);
        getOOSConexion().writeObject(ord);
    }
    protected char[] EscucharStatus() throws IOException, ClassNotFoundException {
        char[] temp = new char[0];
        temp = (char[]) getOISJuego().readObject();
        return temp;
    }


    private class HLector extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            try {
                mListener.EjecutarOrdenes(Nodo.this.EscucharStatus());
            } catch (IOException | ClassNotFoundException e) {
                //El servidor se cae
                Apagar();
                //TODO IMPLEMENTAR APRUEBA DE ERRORES
            }
        }
    }

    private class HEscritor extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            try {
                Nodo.this.EnviarOrden(setData((char) 0));
            } catch (IOException e) {
                //El servidor se cae
                Apagar();
                //TODO IMPLEMENTAR APRUEBA DE ERRORES

            }
        }
    }

    public synchronized char setData(char data) {
        char temp = Data;
        Data = data;
        return temp;
    }

}
