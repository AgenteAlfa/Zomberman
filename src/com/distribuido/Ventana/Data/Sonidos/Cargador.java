package com.distribuido.Ventana.Data.Sonidos;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Cargador {

    public static Clip cargarSonido(String ruta) {        
        try {
            Clip clip = AudioSystem.getClip();
            //clip.open(AudioSystem.getAudioInputStream(Cargador.class.getResource(ruta)));
            clip.open(AudioSystem.getAudioInputStream(new File(ruta)));
            return clip;
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        return null;
    }

}
