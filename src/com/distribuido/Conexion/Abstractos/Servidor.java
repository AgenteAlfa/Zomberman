package com.distribuido.Conexion.Abstractos;

import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.DNodo;
import com.distribuido.Conexion.Nodo.Nodo;
import com.distribuido.Ventana.Mapa;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public abstract class Servidor {
    private ArrayList<DNodo> Nodos;
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
        Participantes = new ArrayList<>();
        Nodos = new ArrayList<>();
        Esperador = new HEsperador();
        Esperador.start();
    }
    protected void DejarEsperar()
    {
        Esperador.Apagar();
        System.out.println("Se dejo de esperar : " + Nodos.size() + " nodos");
        Data = new char[Nodos.size()];
        for (int i = 0; i < Nodos.size(); i++) {
            try {
                Nodos.get(i).getOOSJuego().writeObject(new Comunicaciones(Participantes,i,-1));
                System.out.println("Se envio data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Termine de enviar data");
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
    protected void CrearEnviarMapa()
    {
        Mapa mMapa = new Mapa(5,5)
                .Inclur_Jugadores(Nodos.size())
                .Inclur_Zombies(Nodos.size()*10);

        //mMapa.Imprimir();

        for (int i = 0; i < Nodos.size(); i++) {
            try {
                Nodos.get(i).getOOSJuego().writeObject(mMapa);
                System.out.println("Se envio data");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                    Nodos.set(i,null);
                    fuera++;
                }

            }
        }
        return fuera;
    }

    private class HEsperador extends Hilo
    {
        @Override
        protected void EjecucionBucle() {
            try {
                DNodo temp = new DNodo(SSConexion.accept() , SSJuego.accept());
                System.out.println("A ingresado el jugador Nro " + Nodos.size() + "!");
                Nodos.add(temp);
                Participantes.add((String) temp.getOISConexion().readObject());
                System.out.println("Se le comunico data al nodo " + Nodos.size());
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
                        Nodos.set(i,null);
                        Fuera++;
                        if (Fuera == Nodos.size()) Apagar();
                    }
                }
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
                Apagar();

            try { sleep(Configuracion.DELTA); } catch (InterruptedException e) { e.printStackTrace();}
        }
    }

}
