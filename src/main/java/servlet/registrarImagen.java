/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import servlet.bdd.ConexionBDD;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class registrarImagen extends HttpServlet {

    // Directorio para guardar las imagenes 
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

        // Obtener el creador 
        String creador = (String) session.getAttribute("usuario");

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");

        // Fecha de alta automática 
        String fechaAlta = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Procesar el fichero enviado con Part
        Part filePart = request.getPart("fichero");
        String fileName = getFileName(filePart);

        if (fileName == null || fileName.trim().isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        // Configurar el directorio de destino
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Guardar el fichero 
        File fileToSave = new File(uploadDir, fileName);
        try (InputStream inputStream = filePart.getInputStream()) {
            Files.copy(inputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
            return;
        }

        // Guardar en la BDD
        boolean insertOK = ConexionBDD.registrarImagen(
                titulo, descripcion, keywords, creador, fechaCreacion, fechaAlta, fileName
        );

        if (insertOK) {
            // Respuesta exitosa
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h3>Imagen registrada correctamente.</h3>");
            response.getWriter().println("<a href='menu.jsp'>Volver al menú</a> | ");
            response.getWriter().println("<a href='registrarImagen.jsp'>Registrar otra imagen</a>");
            response.getWriter().println("</body></html>");
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    // Método auxiliar para extraer el nombre del fichero
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}