package com.example.eliabd.ayd2;

/**
 * Created by eliabd on 19/09/2016.
 */
public class Empleados {

    private String nombre;
    private String apellido;
    private int dpi;
    private String puesto;
    private double salario;

    public Empleados(String nombre, String apellido , int dpi, String puesto, double salario){

        this.nombre = nombre;
        this.apellido = apellido;
        this.dpi = dpi;
        this.puesto = puesto;
        this.salario = salario;


    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getDpi() {
        return dpi;
    }

    public void setDpi(int dpi) {
        this.dpi = dpi;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }
}
