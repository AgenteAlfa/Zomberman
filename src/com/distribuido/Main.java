package com.distribuido;

import com.distribuido.Conexion.Distribuidor.Conector;
import com.distribuido.Conexion.Nodo.Jugador;
import com.distribuido.Conexion.Nodo.Nodo;
import com.distribuido.Ventana.Mapa;
import com.distribuido.Ventana.Ventana;

import java.io.IOException;

public class Main{
    static Ventana.AccionesJugador AJ = new Ventana.AccionesJugador() {
        @Override
        public void Moverse(int tecla) {

        }
    };

    public static void main(String[] args) {
        Mapa M = new Mapa(5,5).Inclur_Jugadores(1).Inclur_Zombies(15);
        Ventana V = new Ventana(M,1,AJ);



    }


}
