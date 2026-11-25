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
 * Registro de un usuario nuevo llamando a la operación REST "createUser".
 * El cliente NO accede a la base de datos.
 */
@WebServlet(name = "registrarUsuario", urlPatterns = {"/registrarUsuario"})
public class registrarUsuario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");

        usuario = (usuario == null) ? "" : usuario.trim();

        // Comprobaciones del formulario (antes de llamar al servicio)
        String error = null;
        if (usuario.isEmpty() || password == null || password.isEmpty()) {
            error = "Rellena todos los campos.";
        } else if (usuario.length() > 30) {
            error = "El usuario es demasiado largo (máximo 30 caracteres).";
        } else if (!password.equals(password2)) {
            error = "Las contraseñas no coinciden.";
        }

        // Llamada al servicio REST
        if (error == null) {
            try {
                int codigo = ClienteREST.crearUsuario(usuario, password);

                switch (codigo) {
                    case 201:
                        break;   // creado correctamente
                    case 409:
                        error = "Ese nombre de usuario ya existe.";
                        break;
                    case 400:
                        error = "Los datos del usuario no son válidos.";
                        break;
                    default:
                        error = "No se ha podido crear el usuario (código " + codigo + ").";
                }

            } catch (IOException e) {
                log("Error creando usuario mediante REST", e);
                error = "No se ha podido conectar con el servicio REST.";
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("registro.jsp").forward(request, response);
            return;
        }

        // Todo OK: iniciamos sesión directamente y vamos al menú
        HttpSession session = request.getSession();
        session.setAttribute("usuario", usuario);
        response.sendRedirect("menu.jsp");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("registro.jsp");
    }
}