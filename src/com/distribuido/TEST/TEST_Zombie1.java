package com.distribuido.TEST;

import com.distribuido.Conexion.Nodo.Nodo;
import com.distribuido.Conexion.Nodo.Zombie;

import java.io.IOException;

public class TEST_Zombie1 {
    public static void main(String[] args) {
        try {
            Zombie Z = new Zombie();
            Z.VerificarConexion();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
