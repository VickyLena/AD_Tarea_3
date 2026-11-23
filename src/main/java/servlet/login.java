/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

//import servlet.bdd.ConexionBDD ;
import rest.ClienteREST;
        
import java.io.IOException;
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
@WebServlet("/login")
public class login extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        try {
            boolean esValido =
                    ClienteREST.validarUsuario(usuario, password);

            if (esValido) {

                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);

                response.sendRedirect("menu.jsp");

            } else {

                // Credenciales incorrectas
                response.sendRedirect("error.jsp");
            }

        } catch (IOException e) {
            e.printStackTrace();

            response.setContentType("text/plain;charset=UTF-8");
            response.setStatus(500);

            response.getWriter().println(
                "Error comunicando con REST:"
            );

            response.getWriter().println(e.toString());
        }
    }
}
