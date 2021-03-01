package com.distribuido.Conexion.Abstractos;

public abstract class Hilo extends Thread{
    protected boolean Empezar;
    private boolean encendido = true;
    public void Apagar()
    {
        encendido = false;
    }
    protected abstract void EjecucionBucle();

    public Hilo(boolean empezar) {
        Empezar = empezar;
    }

    public Hilo() {
        Empezar = false;
    }

    @Override
    public void run() {
        while (encendido)
            EjecucionBucle();
    }
}
