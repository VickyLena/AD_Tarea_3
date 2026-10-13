<%-- 
    Document   : eliminarOk
    Created on : 11 oct 2026, 19:50:00
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
        <title>Imagen Eliminada</title>
        <link rel="stylesheet" type="text/css" href="css/styles.css">
    </head>
    <body>
        <h2>Éxito</h2>
        <p>La imagen ha sido eliminada correctamente del sistema y de la base de datos.</p>
        
        <p>
            <a href="buscarImagen.jsp">Volver a la búsqueda</a> | 
            <a href="menu.jsp">Volver al menú</a>
        </p>
    </body>
</html>