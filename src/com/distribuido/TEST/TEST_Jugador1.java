package com.distribuido.TEST;

import com.distribuido.Conexion.Distribuidor.Conector;
import com.distribuido.Conexion.Nodo.Jugador;
import com.distribuido.Conexion.Nodo.Nodo;

import java.io.IOException;

public class TEST_Jugador1 {
    public static void main(String[] args) {
        try {
            Jugador N = new Jugador();
            N.VerificarConexion();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
