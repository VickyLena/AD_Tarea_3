<%-- 
    Document   : modificarImagen
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelos.Imagen"%>
<%@page import="rest.ClienteREST"%>

<%-- Segmento de manejo de sesion --%>
<%
    HttpSession sesion = request.getSession(false);

    if (sesion == null || sesion.getAttribute("usuario") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String usuario = (String) sesion.getAttribute("usuario");

    // Recuperar la imagen desde el servicio REST; si no existe o no es del usuario, error
    Imagen img = null;
    try {
        int id = Integer.parseInt(request.getParameter("id"));
        img = ClienteREST.obtenerImagenPorId(id);
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
        <title>Modificar Imagen</title>
    </head>
    <header>
        <div>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </div>
    </header>
    <body>
        <h2>Modificar Imagen</h2>

        <img src="uploads/<%= img.getFichero() %>" width="150"/><br><br>

        <form method="POST" action="modificarImagen">
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
                <button type="submit">Guardar Cambios</button>
                <a href="menu.jsp">Cancelar</a>
            </div>
        </form>
    </body>
</html>