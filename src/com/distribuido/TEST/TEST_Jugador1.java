package com.distribuido.TEST;

import com.distribuido.Conexion.Configuracion;
import com.distribuido.Conexion.Distribuidor.Reconector;
import com.distribuido.Conexion.Nodo.Jugador;
import com.distribuido.Conexion.Semilla;

import java.io.IOException;

public class TEST_Jugador1 {
    public static void main(String[] args) {
        try {
            Jugador N = new Jugador(-1);
            while (N.VerificarConexion())
            {

                Semilla S  = N.getSemilla();
                N.Cerrar();
                Configuracion.SERVER_IP = S.mComunicaciones.get(++S.mComunicaciones.Actual);
                if (S.mComunicaciones.Actual  == S.mComunicaciones.Posicion)
                {
                    //TENGO QUE CREAR UN SERVIDOR
                    System.out.println("Mi momento a llegado");
                    S.mMapa.Imprimir();

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
                System.out.println("ME UNI PIDIENDO : " + S.mComunicaciones.Posicion);
                N = new Jugador(S.mComunicaciones.Posicion);

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
