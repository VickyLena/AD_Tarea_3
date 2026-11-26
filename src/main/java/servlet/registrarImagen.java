/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import rest.ClienteREST;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 *
 * @author alumne
 */

@WebServlet(name = "registrarImagen", urlPatterns = {"/registrarImagen"})
@MultipartConfig
public class registrarImagen extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // Control de sesión
        HttpSession session = request.getSession(false);

        if (session == null ||
                session.getAttribute("usuario") == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");
        String creador = (String) session.getAttribute("usuario");

        Part fichero = request.getPart("fichero");

        if (fichero == null || fichero.getSize() == 0) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Debes seleccionar una imagen."
            );
            return;
        }

        if (titulo == null || titulo.isBlank()
                || fechaCreacion == null || fechaCreacion.isBlank()) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Faltan datos obligatorios."
            );
            return;
        }

        try {
            String uploadId = ClienteREST.subirImagen(fichero);

            if (uploadId == null || uploadId.isBlank()) {
                throw new IOException(
                        "El REST no devolvió un uploadId válido."
                );
            }

            boolean registrado = ClienteREST.registrarImagen(titulo,descripcion,keywords,creador,fechaCreacion,uploadId);

            if (registrado) {
                request.setAttribute(
                        "mensaje",
                        "Imagen registrada correctamente."
                );

                request.getRequestDispatcher("mensaje.jsp").forward(request, response);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_GATEWAY,
                        "No se pudo registrar la imagen."
                );
            }

        } catch (IOException e) {

            log("Error registrando imagen mediante REST", e);

            response.sendError(
                    HttpServletResponse.SC_BAD_GATEWAY,
                    "Error comunicando con el servicio REST."
            );
        }
    }
}
