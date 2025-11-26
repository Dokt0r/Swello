package es.ucm.fdi.pad.swello.UsuarioAdapter;

import java.io.Serializable;

public class ItemUsuario implements Serializable {

    private String id;
    private String nombre;
    private String email;
    private String fotoPerfilUrl;
    private String fechaNacimiento;

    public ItemUsuario(String id, String nombre, String email,
                       String fotoPerfilUrl, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.fotoPerfilUrl = fotoPerfilUrl;
        this.fechaNacimiento = fechaNacimiento;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public String getFotoPerfilUrl() { return fotoPerfilUrl; }
    public String getFechaNacimiento() { return fechaNacimiento; }

    // --- Setters ---
    public void setId(String id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEmail(String email) { this.email = email; }
    public void setFotoPerfilUrl(String fotoPerfilUrl) { this.fotoPerfilUrl = fotoPerfilUrl; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    @Override
    public String toString() {
        return "ItemUsuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fotoPerfilUrl='" + fotoPerfilUrl + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }
}
