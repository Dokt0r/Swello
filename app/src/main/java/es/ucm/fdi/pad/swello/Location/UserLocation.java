package es.ucm.fdi.pad.swello.Location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
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

    private UserLocation(Context context) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
        actualizarUbicacion();
    }

    /**
     * Inicializa la instancia única con contexto.
     */
    public static synchronized UserLocation init(Context context) {
        if (instance == null) {
            instance = new UserLocation(context.getApplicationContext());
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    /**
     * Devuelve la instancia existente (debe haberse inicializado antes).
     */
    public static UserLocation getInstance() {
        if (instance == null) {
            throw new IllegalStateException("UserLocation no ha sido inicializado. Llama a init(context) primero.");
        }
        return instance;
    }

    /**
     * Intenta obtener la última ubicación conocida del dispositivo.
     */
    @SuppressLint("MissingPermission")
    public void actualizarUbicacion() {
        fusedClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        locationAvailable = true;
                        Log.d(TAG, "Ubicación actualizada: lat=" + latitud + ", lon=" + longitud);
                    } else {
                        Log.w(TAG, "No se pudo obtener ubicación (null)");
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error al obtener ubicación", e));
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
