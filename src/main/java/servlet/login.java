/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlet;

import servlet.bdd.ConexionBDD ;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String usuario = request.getParameter("usuario");
        String password = request.getParameter("password");

        // Llamada a ConexionBDD
        boolean esValido = ConexionBDD.validarUsuario(usuario, password);

        if (esValido) {
            // Usuario correcto
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            response.sendRedirect("menu.jsp");
        } else {
            // Usuario o contraseña incorrectos
            response.sendRedirect("error.jsp");
        }
    }
}
