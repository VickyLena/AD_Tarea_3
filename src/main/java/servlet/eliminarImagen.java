package servlet;

import servlet.bdd.ConexionBDD;
import servlet.bdd.ConexionBDD.Imagen;
import servlet.util.Rutas;
import java.io.File;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author alumne
 */
@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Control de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String usuarioLogueado = (String) session.getAttribute("usuario");

        // obtener id
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
            return;
        }

        // verificar que existe y que es del usuario logueado
        Imagen img = ConexionBDD.obtenerImagenPorId(id);
        if (img == null || !img.getAutor().equalsIgnoreCase(usuarioLogueado)) {
            response.sendRedirect("error.jsp");
            return;
        }

        // Eliminar primero en la BDD
        boolean deleteOK = ConexionBDD.eliminarImagen(id, usuarioLogueado);

        if (deleteOK) {
            // Solo si la BDD ha ido bien, eliminar el fichero del directorio externo
            String uploadFilePath = "/home/alumne/uploads";
            String filePath = uploadFilePath + File.separator + img.getFichero();
            File fileToDelete = new File(filePath);

            if (fileToDelete.exists()) {
                fileToDelete.delete();
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("eliminarOk.jsp");
            dispatcher.forward(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Para proteger de GETs por la url directo
        response.sendRedirect("buscarImagen.jsp");
    }
}