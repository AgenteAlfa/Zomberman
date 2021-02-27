package com.distribuido.Ventana;

import java.io.Serializable;
import java.util.Random;

public class Mapa implements Serializable {
    int[][] Mov , Obj;
    int X,Y;



    public Mapa(int x , int y)
    {
        X = x; Y = y;
        Mov = new int[x][y]; Obj = new int[x][y];
        for (int i = 0; i < Y; i++) {
            for (int j = 0; j < X; j++) {
                Mov[i][j] = Sim_Mov.PASABLE;
                Obj[i][j] = Sim_Obj.VACIO;
            }
        }

        int [][] mArr = new int[X][Y];
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X ; i++) {
                mArr[i][j] = 4;
            }
        }
        Random R = new Random();
        for (int i = 0; i < X * Y; i++) {
            int k = R.nextInt(100);
            if (k < 70)
            {
                int a = R.nextInt(X) , b = R.nextInt(Y);
                if (mArr[a][b] == -1) continue;
                try
                {
                    if (mArr[a + 1][b + 1] > 1 &&
                            mArr[a + 1][b - 1] > 1 &&
                            mArr[a - 1][b + 1] > 1 &&
                            mArr[a - 1][b - 1] > 1)
                    {
                        mArr[a][b] = -1;
                        mArr[a + 1][b + 1] -- ;
                        mArr[a + 1][b - 1] -- ;
                        mArr[a - 1][b + 1] -- ;
                        mArr[a - 1][b - 1] -- ;
                    }


                }
                catch (Exception ignored){}
            }
        }

        for (int j = 0; j < Y;j++) {
            for (int i = 0; i < X; i++) {
                if (mArr[i][j] == -1)
                {
                    Mov[i][j] = R.nextInt(100) > 70? Sim_Mov.ARBOL : Sim_Mov.ROCA_DURA;
                }
            }
        }
    }
    public void Imprimir()
    {
        System.out.println("Movimientos");
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X; i++) {
                System.out.print(Mov[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println("Objetos");
        for (int j = 0; j < Y; j++) {
            for (int i = 0; i < X; i++) {
                System.out.print(Obj[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Mapa Inclur_Jugadores(int cantidad)
    {
        Random R = new Random();
        for (int i = 0; i < cantidad; i++) {
            int tx = R.nextInt(X) , ty = R.nextInt(Y);
            if (Obj[tx][ty] == Sim_Obj.VACIO && Mov[tx][ty] == Sim_Mov.PASABLE)
            {
                Obj[tx][ty] = i + 1;
            }
            else
            {
                i --;
            }

        }

        return this;
    }
    public Mapa Inclur_Zombies(int cantidad)
    {
        Random R = new Random();
        for (int i = 0; i < cantidad; i++) {
            int tx = R.nextInt(X) , ty = R.nextInt(Y);
            if (Obj[tx][ty] == Sim_Obj.VACIO  && Mov[tx][ty] == Sim_Mov.PASABLE)
            {
                Obj[tx][ty] = i + 1 + Sim_Obj.ZOMBIE;
            }
            else
            {
                i --;
            }

        }

        return this;
    }

}
