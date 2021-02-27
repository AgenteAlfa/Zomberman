package com.distribuido.Ventana;


import com.distribuido.Conexion.Configuracion;

public class Sim_Obj {

    public static final int VACIO = 0;
    public static final int JUGADOR_1 = 1;
    public static final int JUGADOR_2 = 2;
    public static final int JUGADOR_3 = 3;
    public static final int JUGADOR_4 = 4;
    public static final int JUGADOR_5 = 5;
    public static int ZOMBIE = 6; //Los nodos que controlen zombies manejaran el codigo 6 o mayor

    public static final int BOMBA = -1;
    public static final int EXPLOSION = Configuracion.TIEMPO_BOMBA / Configuracion.DELTA;


}
