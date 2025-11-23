package es.ucm.fdi.pad.swello.Location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

/**
 * Clase singleton encargada de obtener y almacenar la última ubicación del usuario.
 * Usa FusedLocationProviderClient de Google Play Services.
 */
public class UserLocation {

    private static final String TAG = "UserLocation";
    private static UserLocation instance;
    private static FusedLocationProviderClient fusedClient;

    private double latitud;
    private double longitud;
    private boolean locationAvailable = false;

    // Ubicación por defecto en caso de que no se obtenga ubicación real
    private static final double DEFAULT_LAT = 40.452747;
    private static final double DEFAULT_LON = -3.733186;

    // Listener opcional para notificar cuando la ubicación esté lista
    public interface LocationReadyListener {
        void onLocationReady(double lat, double lon);
    }

    private LocationReadyListener listener;

    private UserLocation(Context context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static synchronized UserLocation init(Context context) {
        if (instance == null) {
            instance = new UserLocation(context.getApplicationContext());
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public static UserLocation getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserLocation no ha sido inicializado. Llama a init(context) primero.");
        }
        return instance;
    }

    public void setLocationReadyListener(LocationReadyListener listener) {
        this.listener = listener;
        // Si ya tenemos ubicación disponible, avisamos inmediatamente
        if (locationAvailable) {
            listener.onLocationReady(latitud, longitud);
        }
    }

    @SuppressLint("MissingPermission")
    public void actualizarUbicacion() {
        Log.d(TAG, "Intentando obtener ubicación actual con getCurrentLocation()...");

        fusedClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null // CancellationToken, puedes poner null si no necesitas cancelar
        ).addOnSuccessListener(location -> {
            if (location != null) {
                Log.d(TAG, "Objeto Location recibido: " + location.toString());
                Log.d(TAG, "Latitud recibida: " + location.getLatitude() +
                        ", Longitud recibida: " + location.getLongitude() +
                        ", Precisión: " + location.getAccuracy() + "m" +
                        ", Tiempo: " + location.getTime());

                // Detectar ubicación fake de Google HQ
                if (location.getLatitude() == 37.4219983 && location.getLongitude() == -122.084) {
                    latitud = DEFAULT_LAT;
                    longitud = DEFAULT_LON;
                    Log.w(TAG, "Ubicación fake detectada (Google HQ), usando default: lat=" + latitud + ", lon=" + longitud);
                } else {
                    latitud = location.getLatitude();
                    longitud = location.getLongitude();
                    Log.d(TAG, "Ubicación real obtenida: lat=" + latitud + ", lon=" + longitud);
                }
            } else {
                latitud = DEFAULT_LAT;
                longitud = DEFAULT_LON;
                Log.w(TAG, "Ubicación nula, usando default: lat=" + latitud + ", lon=" + longitud);
            }
            locationAvailable = true;

            if (listener != null) {
                listener.onLocationReady(latitud, longitud);
            }

        }).addOnFailureListener(e -> {
            latitud = DEFAULT_LAT;
            longitud = DEFAULT_LON;
            locationAvailable = true;
            Log.e(TAG, "Error al obtener ubicación, usando default", e);

            if (listener != null) {
                listener.onLocationReady(latitud, longitud);
            }
        });
    }

    public boolean isLocationAvailable() {
        return locationAvailable;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
