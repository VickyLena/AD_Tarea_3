package servlet;

import rest.ClienteREST;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author alumne
 */
@WebServlet(name = "modificarImagen", urlPatterns = {"/modificarImagen"})
public class modificarImagen extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

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
            mostrarError(request, response, "El id de la imagen no es válido.");
            return;
        }

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String keywords = request.getParameter("keywords");
        String fechaCreacion = request.getParameter("fechaCreacion");

        try {
            // Llamada al servicio REST
            int codigo = ClienteREST.modificarImagen(
                    id, titulo, descripcion, keywords, usuario, fechaCreacion);

            switch (codigo) {
                case 200:
                    request.setAttribute("mensaje", "Imagen modificada correctamente.");
                    request.getRequestDispatcher("mensaje.jsp").forward(request, response);
                    break;
                case 400:
                    mostrarError(request, response, "Los datos de la imagen no son válidos.");
                    break;
                case 403:
                    mostrarError(request, response, "No puedes modificar una imagen que no es tuya.");
                    break;
                case 404:
                    mostrarError(request, response, "La imagen no existe.");
                    break;
                default:
                    mostrarError(request, response,
                            "El servicio REST ha devuelto un error (código " + codigo + ").");
            }

        } catch (IOException e) {
            log("Error modificando imagen mediante REST", e);
            mostrarError(request, response, "No se ha podido conectar con el servicio REST.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("buscarImagen.jsp");
    }

    // error.jsp muestra el atributo "errorMessage"
    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("errorMessage", mensaje);
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }
}