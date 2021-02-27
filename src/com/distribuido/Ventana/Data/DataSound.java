package com.distribuido.Ventana.Data;

import com.distribuido.Ventana.Data.Sonidos.EfectosDeSonido;
import com.distribuido.Ventana.Data.Sonidos.ReproducirSonido;

public class DataSound {
    public ReproducirSonido Inicio,Bomba1,Bomba2,Dead,End,Explosion,Caminar1,Caminar2;
    public DataSound ()
    {
        Inicio = new ReproducirSonido(EfectosDeSonido.title);
        Bomba1 = new ReproducirSonido(EfectosDeSonido.bomb);
        Bomba2 = new ReproducirSonido(EfectosDeSonido.bomb2);
        Dead = new ReproducirSonido(EfectosDeSonido.dead);
        End = new ReproducirSonido(EfectosDeSonido.endgame);
        Explosion = new ReproducirSonido(EfectosDeSonido.explosion);
        Caminar1 = new ReproducirSonido(EfectosDeSonido.walk1);
        Caminar2 = new ReproducirSonido(EfectosDeSonido.walk2);


    }

}
