<%-- 
    Document   : error
    Created on : 23 sept 2026, 16:22:22
    Author     : alumne
--%>

<%
    HttpSession sesion = request.getSession(false);
    boolean logueado = (sesion != null && sesion.getAttribute("usuario") != null);
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Error</title>
    </head>
    <body>
        <h1>Ha habido un Error</h1>
        <h2>Lo sentimos!</h2>
        <% if (logueado){ %>
            <a href="menu.jsp">Volver al Menú</a>
        
        <%} else {%> 
        <a href="login.jsp"> Volver a Inicio de Sesión </a>
        
        <%}%>
        
        <p>
        <% 
            String errorMsg = (String) request.getAttribute("errorMessage");
            if (errorMsg != null) {
                out.print(errorMsg);
            } else {
                out.print("Ha ocurrido un error inesperado en la aplicación.");
            }
        %>
    </p>
    </body>
</html>
