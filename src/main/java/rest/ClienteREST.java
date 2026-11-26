/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rest;

/**
 *
 * @author alumne
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.io.InputStream;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import modelos.Imagen;
import modelos.FicheroImagen;

public class ClienteREST {

    private static final String BASE_URL = "http://localhost:8080/Practica3ADREST/resources";

    public static boolean validarUsuario(
            String usuario, String password) throws IOException {

        URI uri = URI.create(BASE_URL + "/login");

        HttpURLConnection conexion =
                (HttpURLConnection) uri.toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            conexion.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8"
            );

            conexion.setRequestProperty(
                    "Accept", "application/json"
            );

            String datos =
                    "usuario=" + URLEncoder.encode(
                            usuario, StandardCharsets.UTF_8)
                    + "&password=" + URLEncoder.encode(
                            password, StandardCharsets.UTF_8);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(datos.getBytes(StandardCharsets.UTF_8));
            }

            int codigo = conexion.getResponseCode();

            if (codigo == 401) {
                return false;
            }

            if (codigo != 200) {
                throw new IOException(
                        "Error del REST. Código HTTP: " + codigo);
            }

            String respuesta;
            try (var entrada = conexion.getInputStream()) {
                respuesta = new String(
                        entrada.readAllBytes(),
                        StandardCharsets.UTF_8);
            }

            return Pattern.compile(
                    "\"success\"\\s*:\\s*true"
            ).matcher(respuesta).find();

        } finally {
            conexion.disconnect();
        }
    }
    
    public static List<Imagen> buscarImagenes(
        String titulo, String autor,
        String keywords, String fechaCreacion)
        throws IOException {

        String parametros =
                "title=" + encode(titulo)
                + "&creator=" + encode(autor)
                + "&keywords=" + encode(keywords)
                + "&date=" + encode(fechaCreacion);

        String url = BASE_URL + "/search?" + parametros;

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(url)
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            int codigo = conexion.getResponseCode();

            if (codigo != 200) {
                throw new IOException(
                        "Error del REST. Código HTTP: " + codigo);
            }

            List<Imagen> resultados = new ArrayList<>();

            try (InputStream entrada = conexion.getInputStream();
                 JsonReader reader = Json.createReader(entrada)) {

                JsonArray array = reader.readArray();

                for (JsonObject obj : array.getValuesAs(JsonObject.class)) {

                    Imagen imagen = new Imagen(
                            obj.getInt("id"),
                            obj.getString("titulo", ""),
                            obj.getString("descripcion", ""),
                            obj.getString("keywords", ""),
                            obj.getString("autor", ""),
                            obj.getString("fechaCreacion", ""),
                            obj.getString("fechaAlta", ""),
                            obj.getString("fichero", "")
                    );

                    resultados.add(imagen);
                }
            }

            return resultados;

        } finally {
            conexion.disconnect();
        }
    }

    private static String encode(String valor) {
        if (valor == null) {
            valor = "";
        }

        return URLEncoder.encode(
                valor, StandardCharsets.UTF_8);
    }
    
    public static Imagen obtenerImagenPorId(int id) throws IOException {

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(BASE_URL + "/searchID/" + id)
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            int codigo = conexion.getResponseCode();

            if (codigo == 404) {
                return null;
            }

            if (codigo != 200) {
                throw new IOException(
                        "Error del REST. Código HTTP: " + codigo);
            }

            try (InputStream entrada = conexion.getInputStream();
                 JsonReader reader = Json.createReader(entrada)) {

                return jsonToImagen(reader.readObject());
            }

        } finally {
            conexion.disconnect();
        }
    }

    public static int eliminarImagen(int id, String creator) throws IOException {

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(BASE_URL + "/delete")
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            conexion.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8"
            );
            conexion.setRequestProperty("Accept", "application/json");

            String datos = "id=" + id
                    + "&creator=" + encode(creator);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(datos.getBytes(StandardCharsets.UTF_8));
            }

            return conexion.getResponseCode();

        } finally {
            conexion.disconnect();
        }
    }

    public static FicheroImagen descargarImagen(int id) throws IOException {

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(BASE_URL + "/download/" + id)
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("GET");
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(10000);

            int codigo = conexion.getResponseCode();

            if (codigo == 404) {
                return null;
            }

            if (codigo != 200) {
                throw new IOException(
                        "Error del REST. Código HTTP: " + codigo);
            }

            byte[] datos;
            try (InputStream entrada = conexion.getInputStream()) {
                datos = entrada.readAllBytes();
            }

            String tipo = conexion.getContentType();
            if (tipo == null) {
                tipo = "application/octet-stream";
            }

            // Nombre del fichero: viene en la cabecera Content-Disposition
            String nombre = "imagen_" + id;
            String disposicion = conexion.getHeaderField("Content-Disposition");
            if (disposicion != null && disposicion.contains("filename=")) {
                nombre = disposicion.substring(disposicion.indexOf("filename=") + 9)
                                    .replace("\"", "").trim();
            }

            return new FicheroImagen(datos, tipo, nombre);

        } finally {
            conexion.disconnect();
        }
    }

    private static Imagen jsonToImagen(JsonObject obj) {
        return new Imagen(
                obj.getInt("id"),
                obj.getString("titulo", ""),
                obj.getString("descripcion", ""),
                obj.getString("keywords", ""),
                obj.getString("autor", ""),
                obj.getString("fechaCreacion", ""),
                obj.getString("fechaAlta", ""),
                obj.getString("fichero", "")
        );
    }
        /**
     * Crea un usuario nuevo (POST /createUser con username y password).
     * Devuelve el código HTTP de la respuesta (201 = creado).
     */
    public static int crearUsuario(String usuario, String password) throws IOException {

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(BASE_URL + "/createUser")
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            conexion.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8"
            );
            conexion.setRequestProperty("Accept", "application/json");

            String datos = "username=" + encode(usuario)
                    + "&password=" + encode(password);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(datos.getBytes(StandardCharsets.UTF_8));
            }

            return conexion.getResponseCode();

        } finally {
            conexion.disconnect();
        }
    }
    
    public static int modificarImagen(int id, String titulo, String descripcion,
            String keywords, String autor, String fechaCreacion) throws IOException {

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(BASE_URL + "/modify")
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(5000);

            conexion.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8"
            );
            conexion.setRequestProperty("Accept", "application/json");

            // Nombres de los parámetros: los del enunciado
            String datos = "id=" + id
                    + "&title=" + encode(titulo)
                    + "&description=" + encode(descripcion)
                    + "&keywords=" + encode(keywords)
                    + "&creator=" + encode(autor)
                    + "&capture=" + encode(fechaCreacion);

            try (OutputStream os = conexion.getOutputStream()) {
                os.write(datos.getBytes(StandardCharsets.UTF_8));
            }

            return conexion.getResponseCode();

        } finally {
            conexion.disconnect();
        }
    }

    public static String subirImagen(Part fichero) throws IOException {

        String url = BASE_URL + "/upload";

        HttpURLConnection conexion = (HttpURLConnection) URI.create(url).toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(15000);

            conexion.setRequestProperty(
                    "Content-Type", "application/octet-stream");

            conexion.setRequestProperty(
                    "Accept", "application/json");

            String nombre = fichero.getSubmittedFileName();

            conexion.setRequestProperty(
                    "X-Filename",
                    URLEncoder.encode(nombre, StandardCharsets.UTF_8)
            );

            try (InputStream entrada = fichero.getInputStream();
                 OutputStream salida = conexion.getOutputStream()) {

                entrada.transferTo(salida);
            }

            int codigo = conexion.getResponseCode();

            if (codigo != 201) {
                throw new IOException(
                        "Error subiendo fichero. HTTP: " + codigo);
            }

            try (InputStream entrada = conexion.getInputStream();
                 JsonReader reader = Json.createReader(entrada)) {

                JsonObject json = reader.readObject();

                return json.getString("fichero");
            }

        } finally {
            conexion.disconnect();
        }
    }
        

    public static boolean registrarImagen(String titulo,String descripcion,String keywords,String creador,String fechaCreacion,String nombreFichero) 
            throws IOException {

        String url = BASE_URL + "/register";

        HttpURLConnection conexion =
                (HttpURLConnection) URI.create(url)
                        .toURL().openConnection();

        try {
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setConnectTimeout(5000);
            conexion.setReadTimeout(15000);

            conexion.setRequestProperty(
                    "Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");

            conexion.setRequestProperty(
                    "Accept", "application/json");
            
            conexion.setRequestProperty("X-Filename", nombreFichero);

            String parametros =
                    "title=" + encode(titulo)
                    + "&description=" + encode(descripcion)
                    + "&keywords=" + encode(keywords)
                    + "&creator=" + encode(creador)
                    + "&capture=" + encode(fechaCreacion);

            try (OutputStream salida = conexion.getOutputStream()) {
                salida.write(
                        parametros.getBytes(StandardCharsets.UTF_8)
                );
            }

            int codigo = conexion.getResponseCode();

            if (codigo == 201) {
                return true;
            }

            throw new IOException(
                    "Error registrando imagen. HTTP: " + codigo);

        } finally {
            conexion.disconnect();
        }
    }
}