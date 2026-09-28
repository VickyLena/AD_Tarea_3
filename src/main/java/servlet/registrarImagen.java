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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final static Logger LOGGER =
            Logger.getLogger(registrarImagen.class.getCanonicalName());

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
 
        // Creador
        String creador = (String) session.getAttribute("usuario");
        // Fecha
        String fechaAlta = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");

        
        // Create path components to save the file
        final Part filePart = request.getPart("fichero");
        final String fileName = getFileName(filePart);

        if (fileName == null || fileName.trim().isEmpty()) {
            response.sendRedirect("error.jsp");
            return;
        }

        // directorio de destino porque no hay parametro "destination" como en el tutorial
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadFilePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Guardar el fichero sin read ni output stream, en vez uso .copy
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

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
}