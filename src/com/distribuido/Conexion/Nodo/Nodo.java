package com.distribuido.Conexion.Nodo;

import com.distribuido.Conexion.Abstractos.Conexion;
import com.distribuido.Conexion.Abstractos.Hilo;
import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.Reconector;
import com.distribuido.Conexion.Semilla;
import com.distribuido.Ventana.Controlador;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Sim_Obj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Map;


public abstract class Nodo extends Conexion {

    protected final Comunicaciones mComunicacion;
    protected Controlador mListener;
    private char Data;
    public boolean Jugando;

    private HEscritor Escritor;
    private HLector Lector;


    private boolean Reconectable ;
    //Dado que hay dos hilos que pueden reconectar el servidor
    //se tiene que poner un seguro para que no existan 2 servidores a la vez
    //True = bloquear coneccion
    //False = Perminir reconectar




    public Nodo(int n) throws IOException, ClassNotFoundException {

        super(
                new InetSocketAddress(Configuracion.SERVER_IP,Configuracion.PUERTO_CONEXION),
                new InetSocketAddress(Configuracion.SERVER_IP,Configuracion.PUERTO_JUEGO));

        Jugando = true;
        System.out.println(".....Ejecutando codigo NODO");
        System.out.println("Cree mis socket");
        getOOSConexion().writeObject(Configuracion.MI_IP);
        getOOSConexion().writeObject(n);
        System.out.println("Envie ip");

        mComunicacion = (Comunicaciones) getOISJuego().readObject();
        if (Sim_Obj.ZOMBIE == 666)
            Sim_Obj.ZOMBIE = mComunicacion.Jugadores + 1;
        System.out.println(Sim_Obj.ZOMBIE);
        if (Sim_Obj.JUGADOR_BOOM == 667)
            Sim_Obj.JUGADOR_BOOM = mComunicacion.size() + 1;
        System.out.println(Sim_Obj.JUGADOR_BOOM);
        if (Sim_Obj.ZOMBIE_BOOM == 668)
            Sim_Obj.ZOMBIE_BOOM = mComunicacion.size() + 2;
        System.out.println(Sim_Obj.ZOMBIE_BOOM);

        System.out.println("Recibi data");
        mComunicacion.Status();


        Data = 0;
        System.out.println(".....TERMINANDO ejecutar codigo NODO");
    }
    protected void Empezar()
    {
        System.out.println("Estoy en la partida");


        Escritor = new HEscritor();
        Escritor.start();
        Lector = new HLector();
        Lector.start();
    }
    protected abstract Mapa GetMapa();

    public boolean VerificarConexion()
    {
        while (Jugando)
        {
            if ( !(Escritor.isAlive() || Lector.isAlive()) )
            {
                return true;

            }
            try
            {
                Thread.sleep(250);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

        }
        return false;
    }


    public Semilla getSemilla()
    {
        return new Semilla( GetMapa(), mComunicacion);

    }

    protected void EnviarOrden(char ord) throws IOException {
        //if(ord != 0)
        //System.out.println("Se envia : " + ord);
        getOOSConexion().writeObject(ord);
    }
    protected char[] EscucharStatus() throws IOException, ClassNotFoundException {
        char[] temp;

        temp = (char[]) getOISJuego().readObject();

        return temp;
    }



    private class HLector extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            try {
                mListener.EjecutarOrdenes(Nodo.this.EscucharStatus());
            }
            catch (SocketException e)
            {
                if (e.getMessage().equals("Connection reset"))
                {
                    Apagar();
                }
            }
            catch (IOException | ClassNotFoundException e) {
                //e.printStackTrace();
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
                e.printStackTrace();

                Apagar();
            }
        }
    }

    public synchronized char setData(char data) {
        char temp = Data;
        Data = data;
        return temp;
    }



}
