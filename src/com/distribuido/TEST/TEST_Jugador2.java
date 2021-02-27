package com.distribuido.TEST;

import com.distribuido.Conexion.Nodo.Jugador;

import java.io.IOException;

public class TEST_Jugador2 {
    public static void main(String[] args) {
        try {
            Jugador N = new Jugador();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
