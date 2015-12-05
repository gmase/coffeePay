package com.gms.coffeePay;

/**
 * Created by Guille2 on 02/11/2015.
 */
public class Recurso {
    public int pagos;
    public int prioridad;
    public String nombre;
    public int ausencias;
    public Recurso(int Iprioridad,String Inombre)
    {
        pagos=0;
        prioridad=Iprioridad;
        nombre=Inombre;
        ausencias=0;
    }
    public Recurso(int Ipagos,int Iprioridad,String Inombre,int Iausencias)
    {
        pagos=Ipagos;
        prioridad=Iprioridad;
        nombre=Inombre;
        ausencias=Iausencias;
    }

    public Recurso(Recurso recurso) {
        pagos=recurso.pagos;
        prioridad=recurso.prioridad;
        nombre=recurso.nombre;
        ausencias=recurso.ausencias;
    }


    public String toString()
    {
        return nombre+" (Pagos:"+pagos+", Ausencias:"+ausencias+")";
    }
    public float orden(int totalPagos)//El mas bajo paga
    {
        if (totalPagos-ausencias<=0) {
            return 1+(float)prioridad*(float)0.0001;
        }
        else {
            return (float) (((float) pagos / ((float) (totalPagos - ausencias))+ (float) prioridad * 0.0001));
        }

        }
    }
