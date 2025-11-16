package es.ucm.fdi.pad.swello.API_Queries;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import es.ucm.fdi.pad.swello.Filtros.FiltroData;
import es.ucm.fdi.pad.swello.Location.UserLocation;

public class PlayaApiClient {

    private static final String TAG = "PlayaApiClient";
    private static final String BASE_URL = "http://127.0.0.1:3000/api/playas";

    private final RequestQueue requestQueue;

    private UserLocation location;

    public interface PlayaApiListener {
        void onSuccess(JSONArray response);
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

        if ((filtros.distanciaMinima > 0f || filtros.distanciaMaxima < 200f)) {
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

    // --- Llamada HTTP GET ---
    public void fetchPlayas(String baseQuery, FiltroData filtros, PlayaApiListener listener) {
        String url = buildQueryUrl(baseQuery, filtros);
        Log.d(TAG, "Realizando petición a: " + url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                listener::onSuccess,
                error -> listener.onError(new Exception(error))
        );

        requestQueue.add(request);
    }

    // --- Test de conexión y fetch de playa con ID 1 ---
    // --- Método de test: verifica conexión y luego busca playa con id=1 ---
    public void testConexionYBuscarPlaya(FiltroData filtros, PlayaApiListener listener) {
        String urlPing = "http://127.0.0.1:3000/api/playas"; // endpoint general para probar conexión

        Log.d(TAG, "Test de conexión a: " + urlPing);

        JsonArrayRequest pingRequest = new JsonArrayRequest(Request.Method.GET, urlPing, null,
                response -> {
                    Log.d(TAG, "Conexión OK, ahora buscando playa con id=1");
                    // Si el ping OK, hacemos fetch de la playa id=1
                    String urlPlaya1 = "http://127.0.0.1:3000/api/playas/1";
                    JsonArrayRequest requestPlaya1 = new JsonArrayRequest(Request.Method.GET, urlPlaya1, null,
                            listener::onSuccess,
                            error -> {
                                Log.e(TAG, "Error al obtener la playa id=1: " + error.toString());
                                listener.onError(new Exception(error));
                            }
                    );
                    requestQueue.add(requestPlaya1);
                },
                error -> {
                    Log.e(TAG, "No hay conexión con la API: " + error.toString());
                    listener.onError(new Exception(error));
                }
        );

        requestQueue.add(pingRequest);
    }

}