//package servlet;
//
//import servlet.bdd.ConexionBDD;
//import java.io.IOException;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//@WebServlet(name = "registrarUsuario", urlPatterns = {"/registrarUsuario"})
//public class registrarUsuario extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        request.setCharacterEncoding("UTF-8");
//
//        String usuario = request.getParameter("usuario");
//        String password = request.getParameter("password");
//        String password2 = request.getParameter("password2");
//
//        usuario = (usuario == null) ? "" : usuario.trim();
//
//        String error = null;
//        if (usuario.isEmpty() || password == null || password.isEmpty()) {
//            error = "Rellena todos los campos.";
//        } else if (usuario.length() > 30) {
//            error = "El usuario es demasiado largo (máximo 30 caracteres).";
//        } else if (!password.equals(password2)) {
//            error = "Las contraseñas no coinciden.";
//        } else if (ConexionBDD.existeUsuario(usuario)) {
//            error = "Ese nombre de usuario ya existe.";
//        } else if (!ConexionBDD.registrarUsuario(usuario, password)) {
//            error = "No se ha podido crear el usuario. Inténtalo de nuevo.";
//        }
//
//        if (error != null) {
//            request.setAttribute("error", error);
//            request.getRequestDispatcher("registro.jsp").forward(request, response);
//            return;
//        }
//
//        // Todo OK: iniciamos sesión directamente y vamos al menú
//        HttpSession session = request.getSession();
//        session.setAttribute("usuario", usuario);
//        response.sendRedirect("menu.jsp");
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.sendRedirect("registro.jsp");
//    }
//}
