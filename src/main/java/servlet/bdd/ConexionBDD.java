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

/**
 *
 * @author alumne
 */

public class ConexionBDD {

    private static final String URL = "jdbc:derby://localhost:1527/pr2;user=pr2;password=pr2";

    // Método conexión gral
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Método login
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
    
    // Método para suba de Imagenes
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
}
