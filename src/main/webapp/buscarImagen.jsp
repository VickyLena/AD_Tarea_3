<%-- 
    Document   : buscarImagen
    Created on : 28 sept 2026, 12:55:14
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Segmento de manejo de sesion --%>
<%
    HttpSession sesion = request.getSession(false);

    if (sesion == null || sesion.getAttribute("usuario") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String usuario = (String) sesion.getAttribute("usuario");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Buscar Imagenes</title>
    </head>
    <header>
        <div>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </div>
    </header>
    <body>
        <h2>Buscar Imagenes</h2>
        
        <form action="buscarImagen" method="GET">
            <p>
                <label for="titulo">Título:</label><br>
                <input type="text" id="titulo" name="titulo">
            </p>
            <p>
                <label for="autor">Autor / Creador:</label><br>
                <input type="text" id="autor" name="autor">
            </p>
            <p>
                <label for="keywords">Palabras clave:</label><br>
                <input type="text" id="keywords" name="keywords">
            </p>
            <p>
                <input type="submit" value="Buscar">
            </p>
        </form>
        
        <p><a href="menu.jsp">Volver al menú</a></p>
    </body>
</html>
