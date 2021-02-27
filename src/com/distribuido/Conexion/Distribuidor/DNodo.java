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

    public char LeerOrden() throws IOException, ClassNotFoundException {
        return (char) getOISConexion().readObject();
    }
    public void EnviarEstado(char[] Data) throws IOException {
        getOOSJuego().writeObject(Data);
    }

}
