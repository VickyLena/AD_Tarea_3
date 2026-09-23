<%-- 
    Document   : login
    Created on : 16 sept 2026, 15:56:13
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login - Gestión de imágenes</title>
    </head>
    <body>
        <h1>Gestión de Imagenes</h1>
        <%out.println("<h2>Inicio de Sesión</h2>"); %>
        
        <from action ="login" method ="POST">
            <label> Usuario: </label>
            <input type="text" name="usuario" required>
            
            <br><br>
            <label> Contraseña: </label>
            <input type="password" name="password" required>
            <br><br>
            <input type="submit" value="Iniciar sesión">
            
        </from>
        
        
        
        
    </body>
</html>
