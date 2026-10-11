/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author alumne
 */

public class ConexionBDD {

    private static final String URL = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";

    // Metodo conexión gral
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Metodo login
    public static boolean validarUsuario(String usuario, String password) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ? AND password = ?";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                return result.next(); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Metodo para suba de Imagenes
    public static boolean registrarImagen(String titulo, String descripcion, String keywords,
            String autor, String fechaCreacion, String fechaAlta, String nombreFichero) {

        String sql = "INSERT INTO imagen (TITULO, DESCRIPCION, PALABRAS_CLAVE, AUTOR, FECHA_CREACION, FECHA_ALTA, FICHERO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, titulo);
            statement.setString(2, descripcion);
            statement.setString(3, keywords);
            statement.setString(4, autor);
            statement.setString(5, fechaCreacion);
            statement.setString(6, fechaAlta);
            statement.setString(7, nombreFichero);

            int filasInsertadas = statement.executeUpdate();
            return filasInsertadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Metodo de busqueda de imàgenes + objeto Imagen
    public static class Imagen {
        private int id;
        private String titulo;
        private String descripcion;
        private String keywords;
        private String autor;
        private String fechaCreacion;
        private String fechaAlta;
        private String fichero;

        public Imagen(int id, String titulo, String descripcion, String keywords, String autor, String fechaCreacion, String fechaAlta, String fichero) {
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
    
    public static List<Imagen> buscarImagenes(String titulo, String autor, String keywords, String fechaCreacion) {
        List<Imagen> lista = new ArrayList<>();
        
        // x si el param viene vacío
        titulo = (titulo == null) ? "" : titulo.trim();
        autor = (autor == null) ? "" : autor.trim();
        keywords = (keywords == null) ? "" : keywords.trim();
        fechaCreacion = (fechaCreacion == null) ? "" : fechaCreacion.trim();
        
        String sql = "SELECT * FROM imagen WHERE LOWER(TITULO) LIKE LOWER(?) "
                   + "AND LOWER(AUTOR) LIKE LOWER(?) "
                   + "AND LOWER(PALABRAS_CLAVE) LIKE LOWER(?)"
                   + "AND FECHA_CREACION LIKE ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + titulo + "%"); // % se usa para que no importe lo que esta antes o despues
            statement.setString(2, "%" + autor + "%");
            statement.setString(3, "%" + keywords + "%");
            statement.setString(4, "%" + fechaCreacion + "%");

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Imagen(
                        rs.getInt("ID"),
                        rs.getString("TITULO"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("PALABRAS_CLAVE"),
                        rs.getString("AUTOR"),
                        rs.getString("FECHA_CREACION"),
                        rs.getString("FECHA_ALTA"),
                        rs.getString("FICHERO")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
        // Obtener una imagen por su ID (para modificar/eliminar)
    public static Imagen obtenerImagenPorId(int id) {
        String sql = "SELECT * FROM imagen WHERE ID = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Imagen(
                        rs.getInt("ID"),
                        rs.getString("TITULO"),
                        rs.getString("DESCRIPCION"),
                        rs.getString("PALABRAS_CLAVE"),
                        rs.getString("AUTOR"),
                        rs.getString("FECHA_CREACION"),
                        rs.getString("FECHA_ALTA"),
                        rs.getString("FICHERO")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Modificar una imagen existente (solo si el autor coincide)
    public static boolean modificarImagen(int id, String titulo, String descripcion, String keywords,
            String fechaCreacion, String fichero, String autor) {

        String sql = "UPDATE imagen SET TITULO = ?, DESCRIPCION = ?, PALABRAS_CLAVE = ?, "
                   + "FECHA_CREACION = ?, FICHERO = ? WHERE ID = ? AND AUTOR = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, titulo);
            statement.setString(2, descripcion);
            statement.setString(3, keywords);
            statement.setString(4, fechaCreacion);
            statement.setString(5, fichero);
            statement.setInt(6, id);
            statement.setString(7, autor);

            int filasActualizadas = statement.executeUpdate();
            return filasActualizadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
