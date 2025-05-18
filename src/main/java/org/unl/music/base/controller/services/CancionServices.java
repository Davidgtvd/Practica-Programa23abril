package org.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.List;

import org.unl.music.base.controller.dao.dao_models.DaoCancion;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.TipoArchivoEnum;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@BrowserCallable
@AnonymousAllowed
public class CancionServices {
    private final DaoCancion db;

    public CancionServices() {
        this.db = new DaoCancion();
    }

    // Crear canción
    public void create(
            @NotEmpty String nombre,
            @NotEmpty String genero,
            @NotNull Integer duracion,
            @NotEmpty String url,
            @NotEmpty String tipo,
            @NotEmpty String album) throws Exception {

        if (nombre == null || nombre.trim().isEmpty()) throw new Exception("El nombre es requerido");
        if (duracion == null || duracion <= 0) throw new Exception("La duración debe ser mayor a 0");
        if (genero == null || genero.trim().isEmpty()) throw new Exception("El género es requerido");
        if (album == null || album.trim().isEmpty()) throw new Exception("El álbum es requerido");
        if (url == null || url.trim().isEmpty()) throw new Exception("La URL es requerida");
        if (tipo == null || tipo.trim().isEmpty()) throw new Exception("El tipo de archivo es requerido");

        try {
            Cancion cancion = new Cancion();
            cancion.setNombre(nombre.trim());
            cancion.setGenero(genero.trim());
            cancion.setDuracion(duracion);
            cancion.setUrl(url.trim());
            cancion.setTipo(TipoArchivoEnum.valueOf(tipo.toUpperCase()));
            cancion.setAlbum(album.trim());

            if (!db.save(cancion)) throw new Exception("No se pudo guardar la canción");
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo de archivo no válido");
        }
    }

    // Editar canción
    public void update(
            @NotNull Integer id,
            @NotEmpty String nombre,
            @NotEmpty String genero,
            @NotNull Integer duracion,
            @NotEmpty String url,
            @NotEmpty String tipo,
            @NotEmpty String album) throws Exception {

        if (nombre == null || nombre.trim().isEmpty()) throw new Exception("El nombre es requerido");
        if (duracion == null || duracion <= 0) throw new Exception("La duración debe ser mayor a 0");
        if (genero == null || genero.trim().isEmpty()) throw new Exception("El género es requerido");
        if (album == null || album.trim().isEmpty()) throw new Exception("El álbum es requerido");
        if (url == null || url.trim().isEmpty()) throw new Exception("La URL es requerida");
        if (tipo == null || tipo.trim().isEmpty()) throw new Exception("El tipo de archivo es requerido");

        try {
            Cancion cancionExistente = db.findById(id);
            if (cancionExistente == null) throw new Exception("No se encontró la canción con ID: " + id);

            cancionExistente.setNombre(nombre.trim());
            cancionExistente.setGenero(genero.trim());
            cancionExistente.setDuracion(duracion);
            cancionExistente.setUrl(url.trim());
            cancionExistente.setTipo(TipoArchivoEnum.valueOf(tipo.toUpperCase()));
            cancionExistente.setAlbum(album.trim());

            if (!db.update(cancionExistente)) throw new Exception("No se pudo modificar la canción");
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo de archivo no válido");
        }
    }

    // Listar canciones (DTO)
    public List<CancionDTO> listarCanciones() {
        List<CancionDTO> lista = new ArrayList<>();
        for (Cancion cancion : db.listAll()) {
            CancionDTO dto = new CancionDTO(
                cancion.getId(),
                cancion.getNombre(),
                cancion.getGenero(),
                cancion.getAlbum(),
                cancion.getUrl(),
                cancion.getTipo().toString(),
                cancion.getDuracion()
            );
            lista.add(dto);
        }
        return lista;
    }

    // Listar tipos de archivo
    public List<String> listarTiposArchivo() {
        List<String> lista = new ArrayList<>();
        for (TipoArchivoEnum tipo : TipoArchivoEnum.values()) {
            lista.add(tipo.name());
        }
        return lista;
    }
}

// DTO para exponer solo los datos necesarios
class CancionDTO {
    private final Integer id;
    private final String nombre;
    private final String genero;
    private final String album;
    private final String url;
    private final String tipo;
    private final Integer duracion;

    public CancionDTO(
        Integer id,
        String nombre,
        String genero,
        String album,
        String url,
        String tipo,
        Integer duracion
    ) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.album = album;
        this.url = url;
        this.tipo = tipo;
        this.duracion = duracion;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getGenero() { return genero; }
    public String getAlbum() { return album; }
    public String getUrl() { return url; }
    public String getTipo() { return tipo; }
    public Integer getDuracion() { return duracion; }
}