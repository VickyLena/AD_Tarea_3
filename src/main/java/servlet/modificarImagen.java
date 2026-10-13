package servlet;

import servlet.bdd.ConexionBDD;
import servlet.bdd.ConexionBDD.Imagen;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.RequestDispatcher;
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
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
@MultipartConfig
public class modificarImagen extends HttpServlet {

    private final static Logger LOGGER =
            Logger.getLogger(modificarImagen.class.getCanonicalName());

    private static final String UPLOAD_DIR = "/home/alumne/uploads";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Control de sesión
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String usuario = (String) session.getAttribute("usuario");

        // Parsear id
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
            return;
        }

        // Comprobar que la imagen existe y pertenece al usuario logueado
        Imagen imgActual = ConexionBDD.obtenerImagenPorId(id);
        if (imgActual == null || !imgActual.getAutor().equalsIgnoreCase(usuario)) {
            response.sendRedirect("error.jsp");
            return;
        }

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");

        // Fichero: si el usuario ha subido uno nuevo, se reemplaza; si no, se mantiene el actual
        String nombreFichero = imgActual.getFichero();
        final Part filePart = request.getPart("fichero");

        if (filePart != null && filePart.getSize() > 0) {
            final String fileName = getFileName(filePart);

            if (fileName != null && !fileName.trim().isEmpty()) {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                File fileToSave = new File(uploadDir, fileName);
                try (InputStream inputStream = filePart.getInputStream()) {
                    Files.copy(inputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("error.jsp");
                    return;
                }

                // Borrar el fichero antiguo si el nombre ha cambiado
                if (!fileName.equals(imgActual.getFichero())) {
                    File ficheroAntiguo = new File(uploadDir, imgActual.getFichero());
                    if (ficheroAntiguo.exists()) {
                        ficheroAntiguo.delete();
                    }
                }

                nombreFichero = fileName;
            }
        }

        // Actualizar en BDD
        boolean actualizado = ConexionBDD.modificarImagen(
                id, titulo, descripcion, keywords, fechaCreacion, nombreFichero, usuario
        );

        if (actualizado) {
            request.setAttribute("mensaje", "Imagen modificada correctamente.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("mensaje.jsp");
            dispatcher.forward(request, response);
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