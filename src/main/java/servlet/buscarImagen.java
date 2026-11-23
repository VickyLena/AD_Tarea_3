/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import rest.ClienteREST;
import modelos.Imagen;
import java.io.IOException;
import jakarta.servlet.RequestDispatcher;
import java.util.List;
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
@WebServlet(name = "buscarImagen", urlPatterns = {"/buscarImagen"})
public class buscarImagen extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String titulo = request.getParameter("titulo");
        String autor = request.getParameter("autor");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");

        try {

            List<Imagen> resultados =
                    ClienteREST.buscarImagenes(
                            titulo, autor, keywords, fechaCreacion);

            request.setAttribute("listaImagenes", resultados);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher(
                            "resultadoBusqueda.jsp");

            dispatcher.forward(request, response);

        } catch (IOException e) {

            log("Error buscando imágenes mediante REST", e);

            response.sendError(
                    HttpServletResponse.SC_BAD_GATEWAY,
                    "No se pudieron obtener las imágenes del REST"
            );
        }
    }
}
