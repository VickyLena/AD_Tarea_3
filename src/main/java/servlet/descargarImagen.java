/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servlet;

import modelos.FicheroImagen;
import rest.ClienteREST;
import java.io.IOException;
import java.io.OutputStream;
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
@WebServlet(name = "descargarImagen", urlPatterns = {"/imagen"})
public class descargarImagen extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Control de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "id no válido");
            return;
        }

        try {
            // Llamada al servicio REST
            FicheroImagen fichero = ClienteREST.descargarImagen(id);

            if (fichero == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imagen no encontrada");
                return;
            }

            // Mostrar en el navegador o forzar la descarga
            boolean descargar = request.getParameter("descargar") != null;
            String modo = descargar ? "attachment" : "inline";

            response.setContentType(fichero.getTipo());
            response.setContentLength(fichero.getDatos().length);
            response.setHeader("Content-Disposition",
                    modo + "; filename=\"" + fichero.getNombre() + "\"");

            try (OutputStream salida = response.getOutputStream()) {
                salida.write(fichero.getDatos());
            }

        } catch (IOException e) {
            log("Error descargando imagen mediante REST", e);
            response.sendError(HttpServletResponse.SC_BAD_GATEWAY,
                    "No se ha podido obtener la imagen del servicio REST");
        }
    }
}
