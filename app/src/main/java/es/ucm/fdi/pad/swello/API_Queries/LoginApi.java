package es.ucm.fdi.pad.swello.API_Queries;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import es.ucm.fdi.pad.swello.UsuarioAdapter.ItemUsuario;

public class LoginApi {

    private static final String TAG = "UsuarioApiClient";
    private static final String BASE_URL = "http://10.0.2.2:3000/api/users";
    private static final String USER_INFO_URL = "http://10.0.2.2:3000/api/users/me"; // Asumiendo que esta es la ruta para obtener los detalles del usuario

    private final RequestQueue requestQueue;

    public LoginApi(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // Función para hacer login
    public void loginUser(String email, String password, final LoginCallback callback) {
        String url = BASE_URL + "/login";  // Suponiendo que este es el endpoint de login

        JSONObject loginParams = new JSONObject();
        try {
            loginParams.put("email", email);
            loginParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crear el request para hacer login
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                loginParams,
                response -> {
                    try {
                        // Si la respuesta contiene el token JWT, pasamos el token a onSuccess
                        String token = response.optString("token");
                        if (token != null && !token.isEmpty()) {
                            callback.onSuccess(token);  // Llamar al callback de éxito con el token
                        } else {
                            callback.onError("Token no encontrado en la respuesta.");
                        }
                    } catch (Exception e) {
                        callback.onError("Error al procesar la respuesta: " + e.getMessage());
                    }
                },
                error -> callback.onError("Error de red: " + error.getMessage())  // Si hay un error en la solicitud
        );

        requestQueue.add(request);
    }

    // Interfaz LoginCallback
    public interface LoginCallback {
        void onSuccess(String token);  // Método cuando el login es exitoso
        void onError(String error);    // Método cuando hay un error en el login
    }

    // Función para obtener los datos del usuario
    public void getUsuarioData(String token, final UsuarioCallback callback) {
        String url = USER_INFO_URL;

        // Crear el header con el token JWT
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        ItemUsuario usuario = parseUsuario(response);
                        callback.onSuccess(usuario);  // Devolver el usuario a la actividad
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> callback.onError("Error de red: " + error.getMessage())
        ) {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "Bearer " + token);  // Autenticación con JWT
                return headers;
            }
        };

        requestQueue.add(request);
    }

    // Interfaz UsuarioCallback
    public interface UsuarioCallback {
        void onSuccess(ItemUsuario usuario);  // Método cuando se obtienen los datos del usuario
        void onError(String error);           // Método cuando hay un error al obtener los datos
    }

    private ItemUsuario parseUsuario(JSONObject obj) throws JSONException {
        return new ItemUsuario(
                obj.optString("id"),
                obj.optString("nombre"),
                obj.optString("email"),
                obj.optString("foto_perfil_url"),
                obj.optString("fecha_nacimiento")
        );
    }
}
