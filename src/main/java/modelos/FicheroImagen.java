/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author alumne
 */
public class FicheroImagen {

    private final byte[] datos;
    private final String tipo;
    private final String nombre;

    public FicheroImagen(byte[] datos, String tipo, String nombre) {
        this.datos = datos;
        this.tipo = tipo;
        this.nombre = nombre;
    }

    public byte[] getDatos() { return datos; }
    public String getTipo() { return tipo; }
    public String getNombre() { return nombre; }    
}
