<%-- 
    Document   : registro
    Created on : 13 oct 2026, 19:34:23
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registro - Gestión de imágenes</title>
        <link rel="stylesheet" href="css/styles.css">
    </head>
    <body>
        <h1>Gestión de Imágenes</h1>
        <h2>Crear cuenta</h2>

        <% if (error != null) { %>
            <p style="color:#b71c1c; font-weight:600;"><%= error %></p>
        <% } %>

        <form action="registrarUsuario" method="POST">
            <div>
                <label for="usuario">Usuario:</label>
                <input type="text" id="usuario" name="usuario" maxlength="30" required>
            </div>

            <div>
                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>
            </div>

            <div>
                <label for="password2">Repite la contraseña:</label>
                <input type="password" id="password2" name="password2" required>
            </div>

            <div>
                <button type="submit">Registrarme</button>
                <a href="login.jsp">Ya tengo cuenta</a>
            </div>
        </form>
    </body>
</html>