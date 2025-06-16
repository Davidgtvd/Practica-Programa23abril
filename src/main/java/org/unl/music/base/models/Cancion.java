package org.unl.music.base.models;

public class Cancion {
    private Integer id;
    private String nombre;
    private Integer duracion; 
    private String url;
    private TipoArchivoEnum tipo;
    private String genero;
    private String album;
    private String artistaBanda; 

    public Cancion() {}

    public Cancion(Integer id, String nombre, Integer duracion, String url, TipoArchivoEnum tipo, String genero, String album, String artistaBanda) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.url = url;
        this.tipo = tipo;
        this.genero = genero;
        this.album = album;
        this.artistaBanda = artistaBanda;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TipoArchivoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoArchivoEnum tipo) {
        this.tipo = tipo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtistaBanda() {
        return artistaBanda;
    }

    public void setArtistaBanda(String artistaBanda) {
        this.artistaBanda = artistaBanda;
    }


    /**
     * Formatea la duración en minutos:segundos (mm:ss)
     */
    public String getDuracionFormateada() {
        if (duracion == null) return "00:00";
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }

    /**
     * Valida si la canción tiene todos los campos obligatorios
     */
    public boolean isValid() {
        return nombre != null && !nombre.trim().isEmpty() &&
               duracion != null && duracion > 0 &&
               url != null && !url.trim().isEmpty() &&
               tipo != null &&
               genero != null && !genero.trim().isEmpty() &&
               album != null && !album.trim().isEmpty() &&
               artistaBanda != null && !artistaBanda.trim().isEmpty();
    }

    /**
     * Compara dos canciones por ID (si existe) o por todos los campos
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cancion cancion = (Cancion) o;

        if (id != null && cancion.id != null) {
            return id.equals(cancion.id); // Compara por ID si ambos tienen
        }

        if (duracion != null ? !duracion.equals(cancion.duracion) : cancion.duracion != null) return false;
        if (tipo != cancion.tipo) return false;
        if (nombre != null ? !nombre.equals(cancion.nombre) : cancion.nombre != null) return false;
        if (genero != null ? !genero.equals(cancion.genero) : cancion.genero != null) return false;
        if (album != null ? !album.equals(cancion.album) : cancion.album != null) return false;
        if (url != null ? !url.equals(cancion.url) : cancion.url != null) return false;
        return artistaBanda != null ? artistaBanda.equals(cancion.artistaBanda) : cancion.artistaBanda == null;
    }

    /**
     * Genera un código hash basado en los campos de la canción
     */
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (duracion != null ? duracion.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (genero != null ? genero.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (artistaBanda != null ? artistaBanda.hashCode() : 0);
        return result;
    }

    /**
     * Representación en texto de la canción (útil para debugging)
     */
    @Override
    public String toString() {
        return "Cancion{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", duracion=" + duracion +
                ", url='" + url + '\'' +
                ", tipo=" + tipo +
                ", genero='" + genero + '\'' +
                ", album='" + album + '\'' +
                ", artistaBanda='" + artistaBanda + '\'' +
                '}';
    }

    /**
     * Compara canciones por nombre (ignorando mayúsculas/minúsculas)
     */
    public int compareByNombre(Cancion other) {
        if (nombre == null && other.nombre == null) return 0;
        if (nombre == null) return -1;
        if (other.nombre == null) return 1;
        return nombre.compareToIgnoreCase(other.nombre);
    }

    /**
     * Compara canciones por duración
     */
    public int compareByDuracion(Cancion other) {
        if (duracion == null && other.duracion == null) return 0;
        if (duracion == null) return -1;
        if (other.duracion == null) return 1;
        return duracion.compareTo(other.duracion);
    }

    /**
     * Compara canciones por género (ignorando mayúsculas/minúsculas)
     */
    public int compareByGenero(Cancion other) {
        if (genero == null && other.genero == null) return 0;
        if (genero == null) return -1;
        if (other.genero == null) return 1;
        return genero.compareToIgnoreCase(other.genero);
    }

    /**
     * Compara canciones por álbum (ignorando mayúsculas/minúsculas)
     */
    public int compareByAlbum(Cancion other) {
        if (album == null && other.album == null) return 0;
        if (album == null) return -1;
        if (other.album == null) return 1;
        return album.compareToIgnoreCase(other.album);
    }

    /**
     * Compara canciones por artista/banda (ignorando mayúsculas/minúsculas)
     */
    public int compareByArtistaBanda(Cancion other) {
        if (artistaBanda == null && other.artistaBanda == null) return 0;
        if (artistaBanda == null) return -1;
        if (other.artistaBanda == null) return 1;
        return artistaBanda.compareToIgnoreCase(other.artistaBanda);
    }
}