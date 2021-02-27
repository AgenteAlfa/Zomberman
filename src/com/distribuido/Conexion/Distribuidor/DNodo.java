package com.distribuido.Conexion.Distribuidor;

import com.distribuido.Conexion.Abstractos.Conexion;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class DNodo extends Conexion {
    public DNodo(Socket Conexion, Socket Juego) {
        super(Conexion, Juego);
    }

    public char LeerOrden()
    {
        try {
            return (char) getOISConexion().readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ' ';
    }
    public void EnviarEstado(char[] Data)
    {
        try {
            getOOSJuego().writeObject(Data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
