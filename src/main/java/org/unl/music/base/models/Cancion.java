package org.unl.music.base.models;

public class Cancion {
    private Integer id;
    private String nombre;
    private Integer duracion;
    private String url;
    private TipoArchivoEnum tipo;

    // Nuevos campos para nombre de género y álbum
    private String genero;
    private String album;

    public Cancion() {}

    public Integer getId() {
        return this.id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return this.duracion;
    }
    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public TipoArchivoEnum getTipo() {
        return this.tipo;
    }
    public void setTipo(TipoArchivoEnum tipo) {
        this.tipo = tipo;
    }

    // Getters y setters para los nuevos campos
    public String getGenero() {
        return this.genero;
    }
    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getAlbum() {
        return this.album;
    }
    public void setAlbum(String album) {
        this.album = album;
    }
}