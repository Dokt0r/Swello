package es.ucm.fdi.pad.swello.Location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Clase que gestiona los permisos de ubicación en Android.
 * Permite verificar, solicitar y comprobar si el usuario concedió permisos.
 */
public class LocationPermissions {

    private static final int REQUEST_CODE_LOCATION = 1001;
    private final Activity activity;
    private LocationPermissionListener listener;

    public interface LocationPermissionListener {
        void onPermissionGranted();
        void onPermissionDenied();
    }

    public LocationPermissions(Activity activity) {
        this.activity = activity;
    }

    /**
     * Comprueba si el permiso ya está concedido.
     */
    public boolean isLocationGranted() {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Solicita permisos de ubicación al usuario.
     */
    public void requestLocationPermission(LocationPermissionListener listener) {
        this.listener = listener;

        if (isLocationGranted()) {
            if (listener != null) listener.onPermissionGranted();
            return;
        }

        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_CODE_LOCATION);
    }

    /**
     * Debe llamarse desde onRequestPermissionsResult del Activity o Fragment que usa esta clase.
     */
    public void handlePermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (listener != null) listener.onPermissionGranted();
            } else {
                if (listener != null) listener.onPermissionDenied();
            }
        }
    }

    public static int getRequestCode() {
        return REQUEST_CODE_LOCATION;
    }
}
