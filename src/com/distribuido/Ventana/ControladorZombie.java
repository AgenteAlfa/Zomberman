package com.distribuido.Ventana;


import java.io.IOException;
import java.util.Random;


public class ControladorZombie implements Controlador{

    private Mapa mMapa;
    private final int Foco;
    private int[] PosicionFoco;
    private Acciones mListener;

    public static final int ZOMBIE = -3;
    public static final int VACIO = 0;
    public static final int MARCA = -1;
    public static final int INPASABLE = -2;

    public ControladorZombie(Mapa mapa, int enfoque, Acciones listener) throws IOException, ClassNotFoundException {
        mMapa = mapa;
        Foco = enfoque;
        mListener = listener;

        //Buscar la posicion
        for (int i = 0; i < mMapa.X; i++) {
            for (int j = 0; j < mMapa.Y; j++) {
                if (mMapa.Obj[i][j] == enfoque)
                {
                    PosicionFoco = new int[] {i , j};
                    System.out.println("Estoy en " + i + ":" + j);
                }
            }
        }

    }

    public interface Acciones
    {
        void BuscarCerebros(int direccion);
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

        if(new Random().nextInt(100) < 40)    DetectarCerebros();
        RevisarBombas();
        BuscarAlrededor();
    }

    private void DetectarCerebros()
    {
        int [][] TempMapMov = new int[Constantes.VisionZX][Constantes.VisionZY];
        int [][] TempMapObj = new int[Constantes.VisionZX][Constantes.VisionZY];
        int deltaX = (Constantes.VisionZX - 1) / 2;
        int deltaY = (Constantes.VisionZY - 1) / 2;

        for (int j = -deltaY ; j <= deltaY ; j++)  {
            for (int i = -deltaX ; i <= deltaX ; i++){
                try
                {
                    TempMapObj[i + deltaX][j + deltaY] = mMapa.Obj[PosicionFoco[0] + i][PosicionFoco[1] + j];
                    TempMapMov[i + deltaX][j + deltaY] = mMapa.Mov[PosicionFoco[0] + i][PosicionFoco[1] + j];
                }
                catch (Exception E)
                {
                    //Que no es nulo
                    assert TempMapObj[i + deltaX] != null;
                    assert TempMapMov[i + deltaX] != null;
                    TempMapObj[i + deltaX][j + deltaY] = Sim_Obj.VACIO;
                    TempMapMov[i + deltaX][j + deltaY] = Sim_Mov.ARBOL;
                }
            }
        }

        //Ahora trabajar sobre TempMap Mov y Obj

        int [][] pesos = new int[Constantes.VisionZX][Constantes.VisionZY];





        boolean escribir = false;

        //Marcar puntos de jugadores
        for (int j = -deltaY ; j <= deltaY; j++) {
            for (int i = - deltaX ; i <= deltaX; i++) {
                //System.out.println("y = " + Constantes.VisionZY + j);
                if (esJugador(TempMapObj[deltaX + i][deltaY + j]))
                {
                    //System.out.println("Veo a "  +  TempMapObj[deltaX + i][deltaY + j]);
                    escribir = true;
                    pesos[deltaX + i][deltaY + j] = Math.abs(i) + Math.abs(j);
                }
                else if (TempMapMov[deltaX + i][deltaY + j] != Sim_Mov.PASABLE)
                    pesos[deltaX + i][deltaY + j] = INPASABLE;
                else pesos[deltaX + i][deltaY + j] = VACIO;
            }
        }
        pesos[deltaX][deltaY] = ZOMBIE;

        //Preparar mapa de pesos
        for (int p = 1; p <= deltaX + deltaY; p++) {
            //Repetir p veces 
            for (int n = 0; n < p - 1; n++) {
                //Marcar a los vecinos con -2
                for (int j = 0; j < Constantes.VisionZY; j++) {
                    for (int i = 0; i < Constantes.VisionZX; i++) {
                        if(pesos[i][j] == p)
                        {
                            //Marcar vecinos
                            try { pesos[i + 1][j] = pesos[i + 1][j] == VACIO || pesos[i + 1][j] > p ? MARCA : pesos[i + 1][j]; } catch (Exception ignored){}
                            try { pesos[i - 1][j] = pesos[i - 1][j] == VACIO || pesos[i - 1][j] > p ? MARCA : pesos[i - 1][j]; } catch (Exception ignored){}
                            try { pesos[i][j + 1] = pesos[i][j + 1] == VACIO || pesos[i][j + 1] > p ? MARCA : pesos[i][j + 1]; } catch (Exception ignored){}
                            try { pesos[i][j - 1] = pesos[i][j - 1] == VACIO || pesos[i][j - 1] > p ? MARCA : pesos[i][j - 1]; } catch (Exception ignored){}
                        }
                    }
                }
                //Remplaza a los vecinos -2 con p
                for (int j = 0; j < Constantes.VisionZY; j++) {
                    for (int i = 0; i < Constantes.VisionZX; i++) {
                        if(pesos[i][j] == MARCA )
                        {
                            pesos[i][j] = p;
                        }
                    }
                }
            }
        }

        //Imprimir

        if (escribir)
        {
            System.out.println("Pesos...");
            for (int j = 0; j < deltaY * 2 + 1; j++) {
                for (int i = 0; i < deltaX * 2 + 1; i++) {
                    System.out.print(pesos[i][j] + "\t");
                }
                System.out.println();
            }
        }


        //Ver a los vecinos y elegir a cual ir


        int min = 1000;

        //Hallar minimo
        try { if (min > pesos[deltaX + 1][deltaY] && pesos[deltaX + 1][deltaY] > VACIO) min = pesos[deltaX + 1][deltaY]; } catch (Exception ignored){}
        try { if (min > pesos[deltaX - 1][deltaY] && pesos[deltaX - 1][deltaY] > VACIO) min = pesos[deltaX - 1][deltaY]; } catch (Exception ignored){}
        try { if (min > pesos[deltaX][deltaY + 1] && pesos[deltaX][deltaY + 1] > VACIO) min = pesos[deltaX][deltaY + 1]; } catch (Exception ignored){}
        try { if (min > pesos[deltaX][deltaY - 1] && pesos[deltaX][deltaY - 1] > VACIO) min = pesos[deltaX][deltaY - 1]; } catch (Exception ignored){}

        if (min == 1000) return;

        try { if (pesos[deltaX + 1][deltaY] == min) { mListener.BuscarCerebros('d'); return; } } catch (Exception ignored){}
        try { if (pesos[deltaX - 1][deltaY] == min) { mListener.BuscarCerebros('a'); return; } } catch (Exception ignored){}
        try { if (pesos[deltaX][deltaY + 1] == min) { mListener.BuscarCerebros('s'); return; } } catch (Exception ignored){}
        try { if (pesos[deltaX][deltaY - 1] == min) { mListener.BuscarCerebros('w'); return; } } catch (Exception ignored){}


    }

    private boolean esJugador(int c)
    {
        return c >= Sim_Obj.JUGADOR_1 && c < Sim_Obj.ZOMBIE;
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
}
