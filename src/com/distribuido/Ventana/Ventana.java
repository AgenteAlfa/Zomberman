package com.distribuido.Ventana;

import com.distribuido.Ventana.Data.DataImg;
import com.distribuido.Ventana.Data.DataSound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.Date;
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

    private Long Ini;
    private int Seg = 0;
    private int Fot = 0;
    private int FPS = 0;

    public Ventana(Mapa mapa, int enfoque, AccionesJugador listener) throws HeadlessException {
        mMapa = mapa;
        mListener = listener;
        Foco = enfoque;

        setSize(Constantes.ANCHO + 15, Constantes.ALTO+ 39);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        Ini = System.nanoTime();

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

    private void BuscarAlrededor()
    {
        if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1]] != Foco)
        {

            try{ if (mMapa.Obj[PosicionFoco[0] + 1] [PosicionFoco[1]] == Foco)   PosicionFoco[0] ++; } catch (Exception ignored){}
            try{ if (mMapa.Obj[PosicionFoco[0] - 1] [PosicionFoco[1]] == Foco)   PosicionFoco[0] --; } catch (Exception ignored){}
            try{ if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1] + 1] == Foco)   PosicionFoco[1] ++; } catch (Exception ignored){}
            try{ if (mMapa.Obj[PosicionFoco[0]] [PosicionFoco[1] - 1] == Foco)   PosicionFoco[1] --; } catch (Exception ignored){}
        }
    }

    private void DibujarCuadricula()
    {
        int Dx = (Constantes.ANCHO)/(Constantes.VisionX);
        int Dy = (Constantes.ALTO)/(Constantes.VisionY);
        BuscarAlrededor();

        int [][] TempMapMov = new int[Constantes.VisionX][Constantes.VisionY];
        int [][] TempMapObj = new int[Constantes.VisionX][Constantes.VisionY];


        for (int j = -3; j < 4; j++) {
            for (int i = -3; i < 4; i++) {
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

                final int BoomJ = Sim_Obj.JUGADOR_BOOM;

                switch (TempMapObj[i][j])
                {
                    case Sim_Obj.VACIO:
                        //Graph.drawImage(DataImg.Suelo,i*Dx,j*Dy,Dx,Dy,null);
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
                        // 1 -> N
                        if (TempMapObj[i][j] >= Sim_Obj.JUGADOR_1 && TempMapObj[i][j] < Sim_Obj.ZOMBIE)
                        {
                            switch (TempMapObj[i][j])
                            {

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
                            }
                        }
                        else if (TempMapObj[i][j] >= Sim_Obj.ZOMBIE && TempMapObj[i][j] < Sim_Obj.JUGADOR_BOOM)
                        {
                            Graph.drawImage(DataImg.Zombie,i*Dx,j*Dy,Dx,Dy,null);
                        }
                        else if (TempMapObj[i][j] == Sim_Obj.JUGADOR_BOOM)
                        {
                            Graph.drawImage(DataImg.JBoom,i*Dx,j*Dy,Dx,Dy,null);
                        }
                        else if (TempMapObj[i][j] == Sim_Obj.ZOMBIE_BOOM)
                        {
                            Graph.drawImage(DataImg.ZBoom, i * Dx, j * Dy, Dx, Dy, null);
                        }


                        if (TempMapObj[i][j] < Sim_Obj.BOMBA)
                            Graph.drawImage(DataImg.Bomba,i*Dx,j*Dy,Dx,Dy,null);

                        if (TempMapObj[i][j] < Sim_Obj.EXPLOSION)
                            Graph.drawImage(DataImg.Explosion,i*Dx,j*Dy,Dx,Dy,null);
                        break;
                }


            }
        }
        Graph.setColor(Color.WHITE);

        Graph.drawString("FPS : " + FPS, 15,25);
        FPS = (int) (1000000000 / (System.nanoTime() - Ini));
        Ini = System.nanoTime();


    }

    @Override
    public void EjecutarOrdenes(char[] data) {
        //Se asume el 0 -> 1 , 1 -> 2 ....etc
        //System.out.println("Estoy ejecutando ordenes de tam " + data.length);
        for (int i = 0; i < data.length; i++) {
            try {
                if (data[i] != 0)
                    System.out.println((i + 1) + "\t : \t" + data[i] );
                Ejecutar(i + 1, data[i]);
            }catch (Exception ignored){}
        }

        RevisarBombas();
        Actualizar();
    }

    private int[] Buscar(int t)
    {

        for (int i = 0; i < mMapa.X; i++) {
            for (int j = 0; j < mMapa.Y; j++) {
                if (mMapa.Obj[i][j] == t)
                {
                    return new int[] {i , j};
                }
            }
        }
        return null;
    }

    public void Ejecutar(int tag, char orden)
    {
        int[] posicion = Buscar(tag); if (posicion == null) return;
        int[] objetivo = posicion.clone();
        switch (orden)
        {
            case 'W':
            case 'w':
                objetivo[1] --;
                break;
            case 'S':
            case 's':
                objetivo[1] ++;
                break;
            case 'D':
            case 'd':
                objetivo[0] ++;
                break;
            case 'A':
            case 'a':
                objetivo[0] --;
                break;
            case 'B':
            case 'b':
                mMapa.Bombas[tag - 1] = true;
                break;
        }

        if ( mMapa.Mov[objetivo[0]][objetivo[1]] == Sim_Mov.PASABLE  && mMapa.Obj[objetivo[0]][objetivo[1]] == Sim_Obj.VACIO )
        {
            //Mover Objeto
            mMapa.Obj[posicion[0]][posicion[1]] = Sim_Obj.VACIO;
            mMapa.Obj[objetivo[0]][objetivo[1]] = tag;


            if (mMapa.Bombas[tag - 1])
            {
                mMapa.Obj[posicion[0]][posicion[1]] = Sim_Obj.BOMBA;
                mMapa.Bombas[tag - 1] = false;
            }
        }
    }

    public void RevisarBombas()
    {
        for (int i = 0; i < mMapa.X; i++) {
            for (int j = 0; j < mMapa.Y; j++) {
                int c = mMapa.Obj[i][j];
                if(c < 0)
                {
                    if (c > Sim_Obj.DISIPA_EXPLOSION)   //Si c es una bomba
                        mMapa.Obj[i][j] = c - 1;
                    else
                    {
                        mMapa.Obj[i][j] = Sim_Obj.VACIO;
                    }
                }

            }
        }

        int[] PosExp;
        while ( (PosExp = Buscar(Sim_Obj.EXPLOSION + 1)) != null )
        {
            ExplotarBomba(PosExp[0], PosExp[1] );
        }

    }

    private void ExplotarBomba(int x ,  int y)
    {

        //EQ
        try{ Explotar(x,y,true);           } catch (Exception ignored){}
        //Der
        try{ Explotar(x + 1,y,false);   } catch (Exception ignored){}
        //Izq
        try{ Explotar(x - 1,y,false);   } catch (Exception ignored){}
        //Arr
        try{ Explotar(x,y + 1,false);   } catch (Exception ignored){}
        //Aba
        try{ Explotar(x,y - 1,false);   } catch (Exception ignored){}
    }

    private void Explotar(int x ,  int y, boolean EQ)
    {
        if (mMapa.Mov[x][y] == Sim_Mov.PASABLE)
        {

            if (!EQ && mMapa.Obj[x][y] < 0 && mMapa.Obj[x][y] > Sim_Obj.EXPLOSION)
                ExplotarBomba(x,y);
            mMapa.Obj[x][y] = Sim_Obj.EXPLOSION;
        }
        else if (mMapa.Mov[x][y] == Sim_Mov.ARBOL)
        {
            mMapa.Mov[x][y] = Sim_Mov.PASABLE;
            mMapa.Obj[x][y] = Sim_Obj.EXPLOSION;
        }
        else if (mMapa.Mov[x][y] == Sim_Mov.ROCA_BLANDA)
        {
            mMapa.Mov[x][y] = Sim_Mov.PASABLE;
            mMapa.Obj[x][y] = Sim_Obj.EXPLOSION;
        }
        else if (mMapa.Mov[x][y] == Sim_Mov.ROCA_DURA)
            mMapa.Mov[x][y] = Sim_Mov.ROCA_BLANDA;
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
