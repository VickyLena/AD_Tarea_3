/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

/**
 *
 * @author alumne
 */

public class Imagen {

    private int id;
    private String titulo;
    private String descripcion;
    private String keywords;
    private String autor;
    private String fechaCreacion;
    private String fechaAlta;
    private String fichero;

    public Imagen(int id, String titulo, String descripcion,
                  String keywords, String autor, String fechaCreacion,
                  String fechaAlta, String fichero) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.keywords = keywords;
        this.autor = autor;
        this.fechaCreacion = fechaCreacion;
        this.fechaAlta = fechaAlta;
        this.fichero = fichero;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getKeywords() { return keywords; }
    public String getAutor() { return autor; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaAlta() { return fechaAlta; }
    public String getFichero() { return fichero; }
}
