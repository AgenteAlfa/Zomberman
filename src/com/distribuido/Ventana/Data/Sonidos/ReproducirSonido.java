package com.distribuido.Ventana.Data.Sonidos;

import javax.sound.sampled.Clip;

public class ReproducirSonido {
    private Clip clip;

    public ReproducirSonido(Clip c) {

        clip = c;
    }

    public void reproducir() {
        if (!clip.isActive())
        {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void parar() {
        clip.stop();
    }

}
