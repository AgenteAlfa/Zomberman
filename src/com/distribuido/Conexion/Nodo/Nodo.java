package com.distribuido.Conexion.Nodo;

import com.distribuido.Conexion.Abstractos.Conexion;
import com.distribuido.Conexion.Abstractos.Hilo;
import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.Reconector;
import com.distribuido.Ventana.Controlador;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Sim_Obj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
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



    public Nodo() throws IOException, ClassNotFoundException {
        super(
                new Socket(
                        Configuracion.SERVER_IP,
                        Configuracion.PUERTO_CONEXION),
                new Socket(
                        Configuracion.SERVER_IP,
                        Configuracion.PUERTO_JUEGO));
        Jugando = true;

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
        System.out.println("Estoy en la partida");


        Escritor = new HEscritor();
        Escritor.start();
        Lector = new HLector();
        Lector.start();
    }
    protected abstract Mapa GetMapa();

    public void VerificarConexion()
    {
        while (Jugando)
        {
            if ( !(Escritor.isAlive() || Lector.isAlive()) )
            {
                //Si se a desconectado y sigue jugando
                System.out.println("Me desconecte , debo seguir jugando");
                Reconectar();

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
    }



    protected void EnviarOrden(char ord) throws IOException {
        if(ord != 0)
        System.out.println("Se envia : " + ord);
        getOOSConexion().writeObject(ord);
    }
    protected char[] EscucharStatus() throws IOException, ClassNotFoundException {
        char[] temp;
        temp = (char[]) getOISJuego().readObject();
        return temp;
    }


    private void Reconectar() {

        //Apagar los hilos que se conectan con el servidor


        try { getOISJuego().close();        }catch (Exception ignored) {}
        try { getOISConexion().close();     }catch (Exception ignored) {}
        try { getOOSConexion().close();     }catch (Exception ignored) {}
        try { getOOSJuego().close();        }catch (Exception ignored) {}


        mComunicacion.Actual ++;
        Configuracion.SERVER_IP = mComunicacion.get(mComunicacion.Actual);
        if (mComunicacion.Actual == mComunicacion.Posicion)
        {//Si me toca salvar la partida
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        new Reconector(GetMapa()).Reconectar();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        //Me conecto al nuevo servidor
        try {
            Conectar(
                    new Socket(
                            Configuracion.SERVER_IP,
                            Configuracion.PUERTO_CONEXION),
                    new Socket(
                            Configuracion.SERVER_IP,
                            Configuracion.PUERTO_JUEGO));
            //Enviar que nodo soy
            System.out.println("Digo que soy el nodo : " + mComunicacion.Posicion);
            getOOSConexion().writeObject(mComunicacion.Posicion);


        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Ahora leo el mapa");
        Mapa T = null;
        //Espera una cofirmacion

        try
        {
            T = (Mapa) getOISJuego().readObject();
            System.out.println("Lei el mapa que me enviaron");
        } catch (Exception e)
        {
             //e.printStackTrace();
             System.out.println("No se lee el mapa, usando anterior");
        }

        mListener.SetearMapa(T);

        Empezar();
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
                e.printStackTrace();
                try {
                    System.out.println("Reseteando OISJuego");
                    getJuego().getInputStream().reset();

                    setOISJuego(new ObjectInputStream(getJuego().getInputStream()));
                    getOISJuego().reset();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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
