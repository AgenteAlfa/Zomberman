package com.distribuido.TEST;

import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.Reconector;
import com.distribuido.Conexion.Nodo.Zombie;
import com.distribuido.Conexion.Semilla;

import java.io.IOException;

public class TEST_Zombie3 {
    public static void main(String[] args) {
        try {
            Zombie N = new Zombie(-1);
            while (N.VerificarConexion())
            {
                Semilla S  = N.getSemilla();
                Configuracion.SERVER_IP = S.mComunicaciones.get(++S.mComunicaciones.Actual);
                if (S.mComunicaciones.Actual  == S.mComunicaciones.Posicion)
                {
                    //TENGO QUE CREAR UN SERVIDOR
                    System.out.println("Mi momento a llegado");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Reconector R = new Reconector(S);
                                R.Empezar();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                //Unirse al servidor
                N = new Zombie(S.mComunicaciones.Posicion);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
