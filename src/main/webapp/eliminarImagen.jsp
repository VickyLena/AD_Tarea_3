<%-- 
    Document   : eliminarImagen
    Created on : 30 sept 2026, 15:20:07
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

    String usuario = (String) sesion.getAttribute("usuario")
    
    // Comprobar q haya id de imagen 
    String idParam = request.getParameter("id");
    if (idParam == null || idParam.trim().isEmpty()) {
        response.sendRedirect("buscarImagen.jsp");
        return;
    }

    // Si la imagen no es del usuario te devuelve a busqueda
    int id = Integer.parseInt(idParam);
    Imagen img = ConexionBDD.obtenerImagenPorId(id);
    if (img == null || !img.getAutor().equalsIgnoreCase(usuario)) {
        response.sendRedirect("error.jsp");
        return;
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
        <h2>Seguro que quieres eliminar esta imàgen?</h2>
        <table border="1" cellpadding="5">
            <tr>
                <th>Vista previa</th>
                <td><img src="imagenes/<%= img.getFichero() %>" width="150"/></td>
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
            <input type="hidden" name="id" value="<%= img.getId() %>"> <%-- para no pasar el id por la url--%>    
            <input type="submit" value="Sí, eliminar Imagen">
            <a href="buscarImagen.jsp">Volver</a>
        </form>
    </body>
</html>
