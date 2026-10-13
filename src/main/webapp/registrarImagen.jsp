<%-- 
    Document   : registrarImagen
    Created on : 23 sept 2026, 16:26:12
    Author     : alumne
--%>


<%@page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="jakarta.servlet.http.HttpSession" %>
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
<html lang="en">
    <head>
        <title>Registrar Imagen</title>
        <link rel="stylesheet" type="text/css" href="css/styles.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
     <header>
        <div>
            <a href="logout" style="font-weight: bold;">Cerrar Sesión</a>
        </div>
    </header>
    <body>
        <form method="POST" action="registrarImagen" enctype="multipart/form-data" >
            <div>
                <label for="titulo">Titulo:</label><br>
                <input type="text" id="titulo" name="titulo" required>
            </div>
            <br>

            <div>
                <label for="descripcion">Descripción:</label><br>
                <textarea id="descripcion" name="descripcion" rows="4" cols="50" required></textarea>
            </div>
            <br>

            <div>
                <label for="keywords">Palabras Clave:</label><br>
                <input type="text" id="keywords" name="keywords" placeholder="ej. paisaje, playa, verano" required>
            </div>
            <br>

            <div>
                <label for="fechaCreacion">Fecha de Creación:</label><br>
                <input type="date" id="fechaCreacion" name="fechaCreacion" required>
            </div>
            <br>

            <div>
                <label for="fichero">Seleccionar Archivo de Imagen:</label><br>
                <input type="file" id="fichero" name="fichero" accept="image/*" required>
            </div>
            <br>

            <div>
                <button type="submit">Registrar Imagen</button>
                <a href="menu.jsp">Volver al Menú</a>
            </div>
        </form>
    </body>
</html>