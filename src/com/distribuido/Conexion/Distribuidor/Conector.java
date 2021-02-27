package com.distribuido.Conexion.Distribuidor;

import com.distribuido.Conexion.Abstractos.Servidor;
import com.distribuido.Conexion.Comunicaciones;
import com.distribuido.Ventana.Mapa;

import java.io.IOException;
import java.util.Scanner;

public class Conector extends Servidor {

    //El proposito de este objeto es dar un orden
    // a todos los nodos es el NODO 0 , luego de dar la jerarquia muere
    public Conector() throws IOException
    {
        super();
    }
    public void Empezar()
    {

        Esperar();
        Scanner SC = new Scanner(System.in);
        System.out.println("Presionar una tecla para empezar");
        SC.next();
        DejarEsperar();
        //El conector genera el mapa agrega a los jugadores
        CrearEnviarMapa();
        System.out.println("Se envio el mapa");
        Iniciar();


        //Ahora tiene que morir este conector
        //Close();
    }
}
