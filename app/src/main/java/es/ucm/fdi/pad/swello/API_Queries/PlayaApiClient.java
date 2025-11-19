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
    private UserLocation location;

    public interface PlayaApiListener {
        void onSuccess(List<ItemPlaya> playas);

        void onError(Exception e);
    }

    public PlayaApiClient(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    // --- Construcción de la URL con filtros ---
    public String buildQueryUrl(String baseQuery, FiltroData filtros) {
        StringBuilder url = new StringBuilder(BASE_URL + "?");
        boolean hasParam = false;

        if (baseQuery != null && !baseQuery.trim().isEmpty()) {
            url.append("q=").append(baseQuery.trim().replace(" ", "%20"));
            hasParam = true;
        }

        if (!(filtros.distanciaMinima == 0f && filtros.distanciaMaxima == 120f)) {
            location = UserLocation.getInstance();
            if (hasParam) url.append("&");
            url.append("distanciaMin=").append(filtros.distanciaMinima)
                    .append("&distanciaMax=").append(filtros.distanciaMaxima)
                    .append("$lon=").append(location.getLongitud())
                    .append("$lat=").append(location.getLatitud());
            hasParam = true;
        }

        if (!(filtros.tamanoMinimo <= 0.5f && filtros.tamanoMaximo >= 5.0f)) {
            if (hasParam) url.append("&");
            url.append("olaMin=").append(filtros.tamanoMinimo)
                    .append("&olaMax=").append(filtros.tamanoMaximo);
            hasParam = true;
        }

        if (!(filtros.periodoMinimo <= 5f && filtros.periodoMaximo >= 12f)) {
            if (hasParam) url.append("&");
            url.append("periodoMin=").append(filtros.periodoMinimo)
                    .append("&periodoMax=").append(filtros.periodoMaximo);
            hasParam = true;
        }

        if (filtros.direccionOlas != null && !filtros.direccionOlas.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("direccion=").append(filtros.direccionOlas);
            hasParam = true;
        }

        if (filtros.tempAgua != null && !filtros.tempAgua.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("tempAgua=").append(filtros.tempAgua);
            hasParam = true;
        }

        if (filtros.nivelSurfista != null && !filtros.nivelSurfista.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("nivel=").append(filtros.nivelSurfista);
            hasParam = true;
        }

        if (filtros.servicios != null && !filtros.servicios.isEmpty()) {
            if (hasParam) url.append("&");
            url.append("servicios=");
            for (int i = 0; i < filtros.servicios.size(); i++) {
                url.append(filtros.servicios.get(i));
                if (i < filtros.servicios.size() - 1) url.append(",");
            }
        }

        String finalUrl = url.toString();
        Log.d(TAG, "URL generada: " + finalUrl);
        return finalUrl;
    }

    // --- Fetch lista de playas ---
    public void fetchPlayas(String baseQuery, FiltroData filtros, PlayaApiListener listener) {
        String url = buildQueryUrl(baseQuery, filtros);
        Log.d(TAG, "Realizando petición a: " + url);

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

    // --- Fetch playa individual (id=1) ---
    public void testConexionYBuscarPlaya(PlayaApiListener listener) {
        String url = BASE_URL + "/1";
        Log.d(TAG, "Solicitando playa con id=1 a: " + url);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        ItemPlaya playa = parsePlaya(response);
                        List<ItemPlaya> lista = new ArrayList<>();
                        lista.add(playa);
                        listener.onSuccess(lista);
                    } catch (JSONException e) {
                        listener.onError(e);
                    }
                },
                error -> {
                    Log.e(TAG, "Error al obtener la playa id=1: " + error.toString());
                    listener.onError(new Exception(error));
                }
        );

        requestQueue.add(request);
    }

    // --- Parse JSON array ---
    private List<ItemPlaya> parsePlayas(JSONArray array) throws JSONException {
        List<ItemPlaya> lista = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            lista.add(parsePlaya(obj));
        }
        return lista;
    }

    // --- Parse JSON objeto individual ---
    private ItemPlaya parsePlaya(JSONObject obj) throws JSONException {
        return new ItemPlaya(
                obj.optString("id"),
                obj.optString("nombre"),
                obj.optDouble("altura_ola", 0.0),
                obj.optString("direccion_ola", ""),
                obj.optDouble("distancia", 0.0),
                obj.optString("imagen_principal") // NUEVO
        );
    }


}
