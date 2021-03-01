package com.distribuido.Conexion.Distribuidor;

import com.distribuido.Conexion.Abstractos.Servidor;
import com.distribuido.Ventana.Mapa;

import java.io.IOException;

public class Reconector extends Servidor {

    private Mapa Canon;

    public Reconector(Mapa canon) throws IOException {
        Canon = canon;
    }

    public void Reconectar()
    {
        Esperar(false);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DejarEsperar(false);
        //EnviarMapa(Canon);
        Iniciar();
    }

}
