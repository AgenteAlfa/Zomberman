package com.distribuido.TEST;

import com.distribuido.Conexion.Distribuidor.Conector;
import com.distribuido.Conexion.Nodo.Nodo;

import java.io.IOException;

public class TEST_Conector {

    public static void main(String[] args) {
        Conector C = null;
        try {
            C = new Conector();
        } catch (IOException e) {
            e.printStackTrace();
        }
        C.Empezar();
    }
}
