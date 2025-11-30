package es.ucm.fdi.pad.swello.API_Queries;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

import es.ucm.fdi.pad.swello.UsuarioAdapter.ItemUsuario;

public class LoginApi {
    private static final String BASE_URL = "http://10.0.2.2:3000/api/users";
    private static final String USER_INFO_URL = "http://10.0.2.2:3000/api/users/me";

    private final RequestQueue requestQueue;

    public LoginApi(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // --- LOGIN ---
    public void loginUser(String email, String password, final LoginCallback callback) {
        String url = BASE_URL + "/login";

        JSONObject loginParams = new JSONObject();
        try {
            loginParams.put("email", email);
            loginParams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                loginParams,
                response -> {
                    try {
                        String token = response.optString("token");
                        if (!token.isEmpty()) {
                            callback.onSuccess(token);
                        } else {
                            callback.onError("Token no encontrado en la respuesta.");
                        }
                    } catch (Exception e) {
                        callback.onError("Error al procesar la respuesta: " + e.getMessage());
                    }
                },
                error -> callback.onError("Error de red: " + error.getMessage())
        );

        requestQueue.add(request);
    }

    // --- REGISTER ---
    public void registerUser(String nombre, String email, String password, String fechaNacimiento,
                             final RegisterCallback callback) {
        String url = BASE_URL + "/register";

        JSONObject registerParams = new JSONObject();
        try {
            registerParams.put("nombre", nombre);
            registerParams.put("email", email);
            registerParams.put("password", password);
            registerParams.put("fecha_nacimiento", fechaNacimiento);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                registerParams,
                response -> {
                    // Registro exitoso
                    callback.onSuccess(response.optString("message", "Usuario registrado con éxito"));
                },
                error -> {
                    String errMsg = "Error de red";
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errMsg = new String(error.networkResponse.data);
                    }
                    callback.onError(errMsg);
                }
        );

        requestQueue.add(request);
    }

    // --- GET USUARIO ---
    public void getUsuarioData(String token, final UsuarioCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                USER_INFO_URL,
                null,
                response -> {
                    try {
                        ItemUsuario usuario = parseUsuario(response);
                        callback.onSuccess(usuario);
                    } catch (JSONException e) {
                        callback.onError(e.getMessage());
                    }
                },
                error -> {
                    String err = (error.networkResponse != null)
                            ? "Status: " + error.networkResponse.statusCode
                            : error.getMessage();
                    Log.e("API_DEBUG", "Error en /me: " + err);
                    callback.onError("Error en /me: " + err);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    // --- CALLBACK INTERFACES ---
    public interface LoginCallback {
        void onSuccess(String token);
        void onError(String error);
    }

    public interface RegisterCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public interface UsuarioCallback {
        void onSuccess(ItemUsuario usuario);
        void onError(String error);
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
