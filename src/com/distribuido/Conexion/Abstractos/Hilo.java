package com.distribuido.Conexion.Abstractos;

public abstract class Hilo extends Thread{
    private boolean encendido = true;
    public void Apagar()
    {
        encendido = false;
    }
    protected abstract void EjecucionBucle();

    @Override
    public void run() {
        while (encendido)
            EjecucionBucle();
    }
}
