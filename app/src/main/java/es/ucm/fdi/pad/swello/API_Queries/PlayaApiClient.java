package es.ucm.fdi.pad.swello.API_Queries;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.pad.swello.Filtros.FiltroData;
import es.ucm.fdi.pad.swello.Location.UserLocation;
import es.ucm.fdi.pad.swello.PlayaAdapter.ItemPlaya;

public class PlayaApiClient {

    private static final String TAG = "PlayaApiClient";
    private static final String BASE_URL = "http://10.0.2.2:3000/api/beaches";

    private final RequestQueue requestQueue;

    public interface PlayaApiListener {
        void onSuccess(List<ItemPlaya> playas);
        void onError(Exception e);
    }

    public PlayaApiClient(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // --------------------------------------------------------
    // Construcción de la URL
    // --------------------------------------------------------
    public String buildQueryUrl(String baseQuery, FiltroData filtros) {
        StringBuilder url = new StringBuilder(BASE_URL + "?");
        boolean hasParam = false;

        if (baseQuery != null && !baseQuery.trim().isEmpty()) {
            url.append("q=").append(baseQuery.trim().replace(" ", "%20"));
            hasParam = true;
        }

        if (!(filtros.distanciaMinima == 0f && filtros.distanciaMaxima == 200f)) {
            if (UserLocation.isInitialized()) {
                if (hasParam) url.append("&");
                url.append("distanciaMin=").append(filtros.distanciaMinima)
                        .append("&distanciaMax=").append(filtros.distanciaMaxima)
                        .append("&lon=").append(UserLocation.getInstance().getLongitud())
                        .append("&lat=").append(UserLocation.getInstance().getLatitud());
                hasParam = true;
            }
        }

        if (!(filtros.tamanoMinimo <= 0.5f && filtros.tamanoMaximo >= 5.0f)) {
            if (hasParam) url.append("&");
            url.append("olaMin=").append(filtros.tamanoMinimo)
                    .append("&olaMax=").append(filtros.tamanoMaximo);
            hasParam = true;
        }

        if (filtros.direccionOlas != null && !filtros.direccionOlas.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("direccion=").append(filtros.direccionOlas);
            hasParam = true;
        }

        if (filtros.tempAgua != null && !filtros.tempAgua.isEmpty()) {
            if (hasParam) url.append("&");
            String cleaned = filtros.tempAgua
                    .replace("°C", "")
                    .replace("ºC", "")
                    .replace("°", "")
                    .replace("º", "")
                    .replace("C", "")
                    .replace(" ", "")
                    .replace("+", "");

            cleaned = cleaned.replace("–", "-"); // guión largo
            cleaned = cleaned.replace("—", "-"); // guión extra largo

            Log.d("DEBUG", "Cleaned temp = " + cleaned);

            String[] s = cleaned.split("-");
            url.append("tempAguaMin=").append(s[0]);
            if (s.length > 1) url.append("&tempAguaMax=").append(s[1]);
            hasParam = true;
        }

        String finalUrl = url.toString();
        Log.d(TAG, "URL generada: " + finalUrl);
        return finalUrl;
    }

    public void getPlayaById(String id, PlayaByIdListener listener) {
        String url = BASE_URL + "/" + id;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        ItemPlaya playa = parsePlaya(response);
                        listener.onSuccess(playa);
                    } catch (JSONException e) {
                        listener.onError(e);
                    }
                },
                error -> listener.onError(new Exception(error))
        );

        requestQueue.add(request);
    }

    public interface PlayaByIdListener {
        void onSuccess(ItemPlaya playa);
        void onError(Exception e);
    }


    // --------------------------------------------------------
    // Fetch lista playas
    // --------------------------------------------------------
    public void fetchPlayas(String baseQuery, FiltroData filtros, PlayaApiListener listener) {
        String url = buildQueryUrl(baseQuery, filtros);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        List<ItemPlaya> playas = parsePlayas(response);
                        listener.onSuccess(playas);
                    } catch (JSONException e) {
                        listener.onError(e);
                    }
                },
                error -> listener.onError(new Exception(error))
        );

        requestQueue.add(request);
    }

    // --------------------------------------------------------
    // Parseo JSON
    // --------------------------------------------------------
    private List<ItemPlaya> parsePlayas(JSONArray array) throws JSONException {
        List<ItemPlaya> lista = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            lista.add(parsePlaya(obj));
        }
        return lista;
    }

    private ItemPlaya parsePlaya(JSONObject obj) throws JSONException {

        // Procesar IMÁGENES
        List<String> listaImgs = new ArrayList<>();

        if (obj.has("imagenes")) {
            Object raw = obj.get("imagenes");

            if (raw instanceof JSONArray) {  // ← ESTE ES EL CASO REAL
                JSONArray arr = (JSONArray) raw;
                for (int i = 0; i < arr.length(); i++) {
                    String url = arr.optString(i, "");
                    if (!url.isEmpty()) listaImgs.add(url);
                }
            }
            else if (raw instanceof String) { // por si algún endpoint viejo devolviera string
                String s = ((String) raw)
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .replace("\\/", "/");

                for (String part : s.split(",")) {
                    part = part.trim();
                    if (!part.isEmpty()) listaImgs.add(part);
                }
            }
        }

        String[] imagenesArray = listaImgs.isEmpty() ? new String[0] :
                listaImgs.toArray(new String[0]);


        // IMAGEN PRINCIPAL
        String imgPrincipal = obj.optString("imagen_principal", "");

        if (imgPrincipal == null || imgPrincipal.isEmpty()) {
            if (imagenesArray.length > 0) {
                imgPrincipal = imagenesArray[0];
            }
        }

        Log.d(TAG, "IMAGEN PRINCIPAL CALCULADA = " + imgPrincipal);


        return new ItemPlaya(
                obj.optString("id"),
                obj.optString("nombre"),
                obj.optString("descripcion", ""),
                obj.optDouble("valoracion", 0.0),
                obj.optDouble("latitud", 0.0),
                obj.optDouble("longitud", 0.0),
                obj.optDouble("altura_ola", 0.0),
                obj.optDouble("periodo_ola", 0.0),
                obj.optString("direccion_ola", ""),
                obj.optDouble("temp_agua", 0.0),
                obj.optDouble("ocean_current_velocity", 0.0),
                obj.optString("ocean_current_direction", ""),
                obj.optDouble("valoracion_media", 0.0),
                imagenesArray,
                obj.optDouble("distancia", 0.0),
                imgPrincipal
        );
    }
}
