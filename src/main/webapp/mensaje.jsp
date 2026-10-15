<%-- 
    Document   : mensaje
    Created on : 1 oct 2026, 16:24:58
    Author     : alumne
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession(false);
    if (sesion == null || sesion.getAttribute("usuario") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String mensaje = (String) request.getAttribute("mensaje");
    if (mensaje == null) mensaje = "Operación realizada correctamente.";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/styles.css">
        <title>Operación completada</title>
    </head>
    <body>
        <header>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </header>
        <h2><%= mensaje %></h2>
        <p>
            <a href="buscarImagen.jsp">Buscar imágenes</a> |
            <a href="registrarImagen.jsp">Registrar otra imagen</a> |
            <a href="menu.jsp">Volver al Menú</a>
        </p>
    </body>
</html>