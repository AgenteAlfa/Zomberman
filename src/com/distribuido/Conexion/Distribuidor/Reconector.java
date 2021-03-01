package com.distribuido.Conexion.Distribuidor;

import com.distribuido.Conexion.Abstractos.Servidor;
import com.distribuido.Conexion.Semilla;
import com.distribuido.Ventana.Mapa;

import java.io.IOException;
import java.util.Scanner;

public class Reconector extends Servidor {

    private Semilla Canon;

    public Reconector(Semilla canon ) throws IOException {
        Canon = canon;
    }

    public void Empezar()
    {
        System.out.println("Esperando Jugadores...");
        Esperar();

        System.out.println("Termine de esperar Jugadores...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        DejarEsperar(false);
        //El conector genera el mapa agrega a los jugadores
        EnviarMapa(Canon.mMapa);
        Iniciar();
    }

}
