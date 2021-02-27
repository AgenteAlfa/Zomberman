package com.distribuido.Ventana;

import com.distribuido.Ventana.Data.DataImg;
import com.distribuido.Ventana.Data.DataSound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Random;


public class Ventana extends JFrame implements KeyListener, Controlador {

    private Mapa mMapa;
    private final int Foco;
    private AccionesJugador mListener;
    private int[] PosicionFoco;


    private final Canvas canvas;
    private Graphics Graph;
    private BufferStrategy BuffStrg;

    private DataSound mDS;

    public Ventana(Mapa mapa, int enfoque, AccionesJugador listener) throws HeadlessException {
        mMapa = mapa;
        mListener = listener;
        Foco = enfoque;

        setSize(Constantes.ANCHO, Constantes.ALTO);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(Constantes.ANCHO, Constantes.ALTO));
        canvas.setMaximumSize(new Dimension(Constantes.ANCHO, Constantes.ALTO));
        canvas.setMinimumSize(new Dimension(Constantes.ANCHO, Constantes.ALTO));

        add(canvas);
        setVisible(true);
        DataImg.Inicializar();
        //Buscar la posicion
        for (int i = 0; i < mMapa.X; i++) {
            for (int j = 0; j < mMapa.Y; j++) {
                if (mMapa.Obj[i][j] == enfoque)
                {
                    PosicionFoco = new int[] {i , j};
                }
            }
        }

        Actualizar();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {

                    Actualizar();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }



    public void Actualizar()
    {
        //System.out.println("Se actualiza el mapa");
        BuffStrg = canvas.getBufferStrategy();
        if (BuffStrg == null) {
            canvas.createBufferStrategy(3);

            return;
        }
        Graph = BuffStrg.getDrawGraphics();
        /*------------ Dibujar Gráficos ------------*/
        Graph.setColor(Color.black);
        Graph.fillRect(0, 0, Constantes.ANCHO, Constantes.ALTO);

        DibujarCuadricula();
        Graph.dispose();
        BuffStrg.show();
    }

    public Mapa getmMapa() {
        return mMapa;
    }

    private void Buscar()
    {
        if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1]] != Foco)
        {
            if (mMapa.Obj[PosicionFoco[0] + 1] [PosicionFoco[1]] == Foco)   PosicionFoco[0] ++;
            if (mMapa.Obj[PosicionFoco[0] - 1] [PosicionFoco[1]] == Foco)   PosicionFoco[0] --;
            if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1] + 1] == Foco)   PosicionFoco[1] ++;
            if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1] - 1] == Foco)   PosicionFoco[1] --;
        }
    }

    private void DibujarCuadricula()
    {
        int Dx = (Constantes.ANCHO)/(Constantes.VisionX);
        int Dy = (Constantes.ALTO)/(Constantes.VisionY);
        Buscar();

        int [][] TempMapMov = new int[Constantes.VisionX][Constantes.VisionY];
        int [][] TempMapObj = new int[Constantes.VisionX][Constantes.VisionY];


        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                try
                {
                    TempMapObj[i + 3][j + 3] = mMapa.Obj[PosicionFoco[0] + i][PosicionFoco[1] + j];
                    TempMapMov[i + 3][j + 3] = mMapa.Mov[PosicionFoco[0] + i][PosicionFoco[1] + j];
                }
                catch (Exception E)
                {
                    //Que no es nulo
                    assert TempMapObj[i + 3] != null;
                    assert TempMapMov[i + 3] != null;
                    TempMapObj[i + 3][j + 3] = Sim_Obj.VACIO;
                    TempMapMov[i + 3][j + 3] = Sim_Mov.ARBOL;
                }
            }
        }

        //Dibujar Escenario
        for (int j = 0 ; j  < Constantes.VisionY ; j++ ) {
            for (int i = 0; i < Constantes.VisionX; i++) {
                Graph.setColor(Color.gray);
                //Graph.fillRect(i * Dx, j * Dy, Dx - 1, Dy - 1);
                Graph.drawImage(DataImg.Suelo, i * Dx, j * Dy, Dx, Dy, null);

                switch (TempMapMov[i][j])
                {
                    case Sim_Mov.VACIO:
                        Graph.drawImage(DataImg.Vacio,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Mov.ARBOL:
                        Graph.drawImage(DataImg.Arbol,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Mov.ROCA_DURA:
                        Graph.drawImage(DataImg.RocaD,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Mov.ROCA_BLANDA:
                        Graph.drawImage(DataImg.RocaB,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Mov.LAVA:
                        Graph.drawImage(DataImg.Lava,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                }



                switch (TempMapObj[i][j])
                {
                    case Sim_Obj.VACIO:
                        //Graph.drawImage(DataImg.Suelo,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.JUGADOR_1:
                        //numJug++;
                        Graph.drawImage(DataImg.J1,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.JUGADOR_2:
                        //numJug++;
                        Graph.drawImage(DataImg.J2,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.JUGADOR_3:
                        //numJug++;
                        Graph.drawImage(DataImg.J3,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.JUGADOR_4:
                        //numJug++;
                        Graph.drawImage(DataImg.J4,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.JUGADOR_5:
                        //numJug++;
                        Graph.drawImage(DataImg.J5,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.EXPLOSION:
                        //mDS.Explosion.reproducir();
                        Graph.drawImage(DataImg.Explosion,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                    case Sim_Obj.BOMBA:
                        Graph.drawImage(DataImg.Bomba,i*Dx,j*Dy,Dx,Dy,null);
                        Random R = new Random();
                        /*
                        if(R.nextInt(100) < 50)
                            mDS.Bomba1.reproducir();
                        else
                            mDS.Bomba2.reproducir();
                        */
                        break;
                    default:
                        if (TempMapObj[i][j] < Sim_Obj.BOMBA)
                            Graph.drawImage(DataImg.Bomba,i*Dx,j*Dy,Dx,Dy,null);
                        if (TempMapObj[i][j] > Sim_Obj.JUGADOR_5)
                            Graph.drawImage(DataImg.Zombie,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                }


            }
        }
    }

    @Override
    public void EjecutarOrdenes(char[] data) {
        System.out.println("Hijole ejecuto ordenes");
    }


    public interface AccionesJugador
    {
        void Moverse(int tecla);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        mListener.Moverse(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
