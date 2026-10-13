<%-- 
    Document   : modifiacarImagen
    Created on : 30 sept 2026, 15:23:52
    Author     : alumne
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jakarta.servlet.http.HttpSession"%>
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
%>

<%-- Recuperar la imagen a modificar --%>
<%
    Imagen img = null;
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        img = ConexionBDD.obtenerImagenPorId(id);
    } catch (Exception e) {
        img = null;
    }

    // Si no existe la imagen, o no pertenece al usuario logueado, error
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
        <title>Modificar Imagen</title>
    </head>
    <header>
        <div>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </div>
    </header>
    <body>
        <h2>Modificar Imagen</h2>

        <img src="imagenes/<%= img.getFichero() %>" width="150"/><br><br>

        <form method="POST" action="modificarImagen" enctype="multipart/form-data">
            <input type="hidden" name="id" value="<%= img.getId() %>">

            <div>
                <label for="titulo">Título:</label><br>
                <input type="text" id="titulo" name="titulo" value="<%= img.getTitulo() %>" required>
            </div>
            <br>

            <div>
                <label for="descripcion">Descripción:</label><br>
                <textarea id="descripcion" name="descripcion" rows="4" cols="50" required><%= img.getDescripcion() %></textarea>
            </div>
            <br>

            <div>
                <label for="keywords">Palabras Clave:</label><br>
                <input type="text" id="keywords" name="keywords" value="<%= img.getKeywords() %>" required>
            </div>
            <br>

            <div>
                <label for="fechaCreacion">Fecha de Creación:</label><br>
                <input type="date" id="fechaCreacion" name="fechaCreacion" value="<%= img.getFechaCreacion() %>" required>
            </div>
            <br>

            <div>
                <label for="fichero">Reemplazar imagen (opcional):</label><br>
                <input type="file" id="fichero" name="fichero" accept="image/*">
            </div>
            <br>

            <div>
                <button type="submit">Guardar Cambios</button>
                <a href="menu.jsp">Cancelar</a>
            </div>
        </form>
    </body>
</html>