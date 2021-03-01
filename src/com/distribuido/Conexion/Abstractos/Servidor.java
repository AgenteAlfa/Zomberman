package com.distribuido.Conexion.Abstractos;

import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.DNodo;
import com.distribuido.Conexion.Nodo.Nodo;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Sim_Obj;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public abstract class Servidor {
    private ArrayList<DNodo> Nodos;
    private int Jugadores;
    private char[] Data;
    private ArrayList<String> Participantes;

    private ServerSocket SSConexion, SSJuego;

    private HEsperador Esperador;
    private HOidor Oidor;
    private HEjecutor Ejecutor;

    public Servidor() throws IOException {
        SSConexion = new ServerSocket(Configuracion.PUERTO_CONEXION);
        SSJuego = new ServerSocket(Configuracion.PUERTO_JUEGO);
        SSConexion.setSoTimeout(300); SSJuego.setSoTimeout(300);
    }

    protected void Esperar()
    {
        Esperar(true);
    }
    protected void Esperar(boolean opt)
    {
        Participantes = new ArrayList<>();
        Nodos = new ArrayList<>();
        Esperador = new HEsperador(opt);
        Esperador.start();
    }
    protected void EsperarZombies()
    {
        Jugadores = Nodos.size();
        Sim_Obj.ZOMBIE = Jugadores + 1;
        System.out.println("Hay " + Jugadores + " jugadores");
    }

    protected void DejarEsperar(boolean opt)
    {
        Esperador.Apagar();

        Data = new char[Nodos.size()];
        if (opt)
        {
            System.out.println("Hay " + (Nodos.size() - Jugadores) + " Zombies");
            for (int i = 0; i < Nodos.size(); i++) {
                try {
                    Nodos.get(i).getOOSJuego().writeObject(new Comunicaciones(Participantes,i,-1,Jugadores));
                    System.out.println("Se envio comunicaciones");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Termine de enviar comunicaciones");

    }
    protected void DejarEsperar()
    {
        DejarEsperar(true );
    }
    protected void Iniciar()
    {
        Oidor = new HOidor();
        Oidor.start();

        Ejecutor = new HEjecutor();
        Ejecutor.start();
    }



    protected void Close()
    {
        try {
            SSJuego.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SSConexion.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void EnviarMapa(Mapa mMapa)
    {
        System.out.println("Enviando mapa a " + Nodos.size() + " nodos ");
        for (DNodo nodo : Nodos) {
            try {
                nodo.getOOSJuego().writeObject(mMapa);
                System.out.println("Se envio mapa");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Termine de enviar el mapa");
    }

    protected void CrearEnviarMapa()
    {
        Mapa mMapa = new Mapa(10,10)
                .Inclur_Jugadores(Jugadores)
                .Inclur_Zombies(Nodos.size() - Jugadores);

        mMapa.Imprimir();

        EnviarMapa(mMapa);
    }


    protected int EnviarStatusTodos(char[] temp)
    {
        int fuera = 0;
        for (int i = 0; i < Nodos.size(); i++) {
            DNodo N = Nodos.get(i);
            if (N != null) {
                try {
                    N.EnviarEstado(temp);
                } catch (Exception E) {
                    //Nodos.set(i,null);

                    fuera++;
                }

            }else
            fuera++;
        }
        return fuera;
    }

    private class HEsperador extends Hilo
    {
        public HEsperador(boolean empezar) {
            super(empezar);
        }

        @Override
        protected void EjecucionBucle() {
            try {
                DNodo temp = new DNodo(SSConexion.accept() , SSJuego.accept());

                if (Empezar)
                {
                    System.out.println("A ingresado el jugador Nro " + Nodos.size() + "!");
                    Nodos.add(temp);
                    Participantes.add((String) temp.getOISConexion().readObject());
                    System.out.println("Se le comunico data al nodo ");
                }
                else
                {
                    int t = (int) temp.getOISConexion().readObject();
                    System.out.println("Se a reconectado un jugador! , Dice ser " + t);
                    Nodos.add(t,temp);
                }


            } catch (Exception ignored) {}
        }
    }




    private class HOidor extends Hilo
    {

        @Override
        protected void EjecucionBucle() {
            //Este hilo escucha las ordenes de cada nodo en cierto momento y las guarda para que
            //un hilo ejecutor pueda hacer las operaciones
            int Fuera = 0;
            for (int i = 0; i < Nodos.size(); i++) {
                DNodo N = Nodos.get(i);
                if (N != null)
                {
                    try
                    {
                        char recibido = N.LeerOrden();
                        if (recibido != 0)
                        {
                            Data[i] = recibido ;
                        }
                    }
                    catch (Exception E) {
                        //Nodos.set(i,null);
                        Fuera++;
                        if (Fuera == Nodos.size()){
                            Apagar();
                            System.out.println("Apagando 2...");
                            Close();
                        }
                    }
                }else
                    Fuera++;
            }
        }
    }

    private class HEjecutor extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            //Instante T en el que se cuenta un turno

            char[] TesimaData = Data.clone();
            Data = new char[Data.length];
            //Enviar data a todos los nodos para que actualizen su mapa

            int fuera = EnviarStatusTodos(TesimaData);
            if (fuera == Nodos.size())
            {
                Apagar();
                System.out.println("Apagando 1...");
            }

            try { sleep(Configuracion.DELTA); } catch (InterruptedException e) { e.printStackTrace();}
        }
    }

}
