<%-- 
    Document   : modificarImagen
    Created on : 23 sept 2026, 16:38:35
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Buscar Imagen</h1>
        
        <ul>
            <li>
                <a href="eliminarImagen.jsp">Eliminar Imagen</a>
            </li>

            <li>
                <a href="modificarImagen.jsp">Modificar Imagen</a>
            </li>
        </ul>
        
        <label> Id imagen: </label>
        <input type="text" name="idimagen" required>
        <br><br>
        <label> Título: </label>
        <input type="text" name="titimagen" required>
        <br><br>
        <label> Creador: </label>
        <input type="text" name="creaimagen" required>
        <br><br>
        <label> Fecha creación: </label>
        <input type="text" name="fechaimagen" required>
        <br><br>
    </body>
</html>
