package com.distribuido.Ventana.Data;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DataImg {
    public static BufferedImage Suelo,Vacio,Vacio1,Vacio2,Vacio3,Vacio4,Arbol,RocaD,RocaB;

    public static BufferedImage J1,J2,J3,J4,J5,Bomba,Explosion,Nada,Zombie, Lava;
    public static void Inicializar()
    {
        try {
            Suelo = ImageIO.read(new File("src\\data\\graphics\\ground1.png"));
            Arbol = ImageIO.read(new File("src\\data\\graphics\\tree2.png"));

            Lava = ImageIO.read(new File("src\\data\\graphics\\lava1.png"));
            Vacio1 = ImageIO.read(new File("src\\data\\graphics\\lava1.png"));
            Vacio2 = ImageIO.read(new File("src\\data\\graphics\\wallU.png"));
            Vacio3 = ImageIO.read(new File("src\\data\\graphics\\wallL.png"));
            Vacio4 = ImageIO.read(new File("src\\data\\graphics\\wallD.png"));

            RocaD = ImageIO.read(new File("src\\data\\graphics\\Rock2.png"));
            RocaB = ImageIO.read(new File("src\\data\\graphics\\Rock1.png"));

            J1 = ImageIO.read(new File("src\\data\\graphics\\P1stand.png"));
            J2 = ImageIO.read(new File("src\\data\\graphics\\P2stand.png"));
            J3 = ImageIO.read(new File("src\\data\\graphics\\P3stand.png"));
            J4 = ImageIO.read(new File("src\\data\\graphics\\P4stand.png"));
            J5 = ImageIO.read(new File("src\\data\\graphics\\P5stand.png"));

            Zombie = ImageIO.read(new File("src\\data\\graphics\\NPC.png"));

            Bomba = ImageIO.read(new File("src\\data\\graphics\\boom1.png"));
            Explosion = ImageIO.read(new File("src\\data\\graphics\\explot1.png"));
            Nada = null;



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
