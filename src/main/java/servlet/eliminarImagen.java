/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import servlet.bdd.ConexionBDD;
import servlet.bdd.ConexionBDD.Imagen;
import java.io.File;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.RequestDispatcher;

/**
 *
 * @author alumne
 */
@WebServlet(name = "eliminarImagen", urlPatterns = {"/eliminarImagen"})
public class eliminarImagen extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private static final String UPLOAD_DIR = "imagenes";
    
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
        String idParam = request.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        int id = Integer.parseInt(idParam);

        // verificar autor y nombre de fichero
        Imagen img = ConexionBDD.getById(id);

        if (img == null || !img.getAutor().equalsIgnoreCase(usuarioLogueado)) {
            response.sendRedirect("error.jsp");
            return;
        }

        //  Eliminar archivo
        String applicationPath = request.getServletContext().getRealPath("");
        String filePath = applicationPath + File.separator + UPLOAD_DIR + File.separator + img.getFichero();
        File fileToDelete = new File(filePath);

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        // Eliminar en la BDD
        boolean deleteOK = ConexionBDD.eliminar(id);

        if (deleteOK) {
            // Redirigir al JSP de éxito (Sin HTML en el servlet)
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
