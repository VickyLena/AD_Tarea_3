<%-- 
    Document   : eliminarImagen
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="servlet.bdd.ConexionBDD"%>
<%@page import="servlet.bdd.ConexionBDD.Imagen"%>

<%-- Segmento de manejo de sesion --%>
<%
    HttpSession sesion = request.getSession(false);

    if (sesion == null || sesion.getAttribute("usuario") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String usuario = (String) sesion.getAttribute("usuario");

    // Recuperar la imagen; si no existe o no es del usuario, error
    Imagen img = null;
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        img = ConexionBDD.obtenerImagenPorId(id);
    } catch (Exception e) {
        img = null;
    }

    if (img == null || !img.getAutor().equalsIgnoreCase(usuario)) {
        response.sendRedirect("error.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/styles.css">
        <title>Eliminar Imagen</title>
    </head>
    <header>
        <div>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </div>
    </header>
    <body>
        <h2>¿Seguro que quieres eliminar esta imagen?</h2>
        <table border="1" cellpadding="5">
            <tr>
                <th>Vista previa</th>
                <td><img src="uploads/<%= img.getFichero() %>" width="150"/></td>
            </tr>
            <tr>
                <th>Título</th>
                <td><%= img.getTitulo() %></td>
            </tr>
            <tr>
                <th>Descripción</th>
                <td><%= img.getDescripcion() %></td>
            </tr>
        </table>
        <form action="eliminarImagen" method="POST">
            <input type="hidden" name="id" value="<%= img.getId() %>">
            <input type="submit" value="Sí, eliminar imagen">
            <a href="buscarImagen.jsp">Volver</a>
        </form>
    </body>
</html>