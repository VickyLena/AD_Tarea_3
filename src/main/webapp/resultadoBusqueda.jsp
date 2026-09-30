<%-- 
    Document   : resultadoBusqueda
    Created on : 28 sept 2026, 13:22:06
    Author     : alumne
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
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

<%-- Recuperar las imagenes buscadas --%>
<%
    List<Imagen> resultados = (List<Imagen>) request.getAttribute("listaImagenes");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/styles.css">
        <title>Resultado de la Bùsqueda</title>
    </head>
    <body>
        <h1>Resultados de la Búsqueda</h1>
        <% if (resultados == null || resultados.isEmpty()) { %>
            
            <h3>No se han encontrado imágenes con los criterios especificados.</h3>
            <p>
                <a href="buscarImagen.jsp">Realizar otra búsqueda</a> | 
                <a href="menu.jsp">Volver al menú</a>
            </p>

        <% } else { %>

            <table border="1" cellpadding="5">
                <tr>
                    <th>Vista previa</th>
                    <th>Título</th>
                    <th>Descripción</th>
                    <th>Palabras clave</th>
                    <th>Autor</th>
                    <th>Fecha Creación</th>
                    <th>Fecha Alta</th>
                    <th>Acciones</th>
                </tr>

                <% for (Imagen img : resultados) { %>
                    <tr>
                        <td><img src="imagenes/<%= img.getFichero() %>" width="100"/></td>
                        <td><%= img.getTitulo() %></td>
                        <td><%= img.getDescripcion() %></td>
                        <td><%= img.getKeywords() %></td>
                        <td><%= img.getAutor() %></td>
                        <td><%= img.getFechaCreacion() %></td>
                        <td><%= img.getFechaAlta() %></td>
                        <td>
                            <%-- Solo mostrar modificar/eliminar si la imagen es del usuario logueado --%>
                            <% if (img.getAutor().equalsIgnoreCase(usuario)) { %>
                                <a href="modificarImagen.jsp?id=<%= img.getId() %>">Modificar</a> | 
                                <a href="eliminarImagen.jsp?id=<%= img.getId() %>">Eliminar</a>
                            <% } else { %>
                                -
                            <% } %>
                        </td>
                    </tr>
                <% } %>
            </table>

            <br>
            <p>
                <a href="buscarImagen.jsp">Nueva búsqueda</a> | 
                <a href="menu.jsp">Volver al menú</a>
            </p>

        <% } %>
    </body>
</html>
