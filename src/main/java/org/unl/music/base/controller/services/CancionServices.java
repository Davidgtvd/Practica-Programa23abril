package org.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.unl.music.base.controller.dao.dao_models.DaoCancion;
import org.unl.music.base.controller.data_struct.list.LinkedList;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.TipoArchivoEnum;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class CancionServices {
    private DaoCancion db;

    public CancionServices() {
        db = new DaoCancion();
    }

    public void create(String nombre, String genero, Integer duracion, String url, String tipo, String album, String artistaBanda) throws Exception {
        validarCampos(nombre, genero, duracion, url, tipo, album, artistaBanda);

        Cancion cancion = new Cancion();
        cancion.setNombre(nombre.trim());
        cancion.setGenero(genero.trim());
        cancion.setDuracion(duracion);
        cancion.setUrl(url.trim());
        cancion.setAlbum(album.trim());
        cancion.setTipo(safeTipoArchivoEnum(tipo));
        cancion.setArtistaBanda(artistaBanda.trim());

        db.create(cancion);
    }

    public void update(Integer id, String nombre, String genero, Integer duracion, String url, String tipo, String album, String artistaBanda) throws Exception {
        if (id == null) throw new Exception("El ID es requerido para actualizar");
        Cancion cancionExistente = db.get(id);
        if (cancionExistente == null) throw new Exception("No se encontró la canción con ID: " + id);

        validarCampos(nombre, genero, duracion, url, tipo, album, artistaBanda);

        Cancion cancion = new Cancion();
        cancion.setId(id);
        cancion.setNombre(nombre.trim());
        cancion.setGenero(genero.trim());
        cancion.setDuracion(duracion);
        cancion.setUrl(url.trim());
        cancion.setAlbum(album.trim());
        cancion.setTipo(safeTipoArchivoEnum(tipo));
        cancion.setArtistaBanda(artistaBanda.trim());

        db.update(cancion);
    }

    public void delete(Integer id) throws Exception {
        if (id == null) throw new Exception("El ID es requerido para eliminar");
        Cancion cancionExistente = db.get(id);
        if (cancionExistente == null) throw new Exception("No se encontró la canción con ID: " + id);
        db.delete(id);
    }

    public Cancion getById(Integer id) throws Exception {
        if (id == null) throw new Exception("El ID es requerido");
        return db.get(id);
    }

   
    public List<HashMap> listAll() throws Exception {
        LinkedList<Cancion> canciones = db.listAll();
        LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
        for (int i = 0; i < canciones.getLength(); i++) {
            Cancion c = canciones.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nombre", c.getNombre());
            map.put("genero", c.getGenero());
            map.put("album", c.getAlbum());
            map.put("duracion", c.getDuracion());
            map.put("duracionFormateada", c.getDuracionFormateada());
            map.put("url", c.getUrl());
            map.put("tipo", c.getTipo() != null ? c.getTipo().toString() : "DESCONOCIDO");
            map.put("artistaBanda", c.getArtistaBanda());
            lista.add(map);
        }
        return Arrays.asList(lista.toArray());
    }

    public String[] listarTiposArchivo() {
        TipoArchivoEnum[] tipos = TipoArchivoEnum.values();
        String[] tiposString = new String[tipos.length];
        for (int i = 0; i < tipos.length; i++) {
            tiposString[i] = tipos[i].toString();
        }
        return tiposString;
    }

    // ORDENACIÓN Y BÚSQUEDA 

    public List<HashMap> order(String attribute, Integer type) throws Exception {
        LinkedList<Cancion> canciones = db.listAll();
        if (canciones.isEmpty()) return new ArrayList<>();

        if (!isValidAttribute(attribute)) throw new Exception("Atributo no válido para ordenación: " + attribute);

        boolean asc = (type == 1);
        canciones.ordenarPorAtributo(attribute, asc);

        // Convertir a lista de HashMap
        LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
        for (int i = 0; i < canciones.getLength(); i++) {
            Cancion c = canciones.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nombre", c.getNombre());
            map.put("genero", c.getGenero());
            map.put("album", c.getAlbum());
            map.put("duracion", c.getDuracion());
            map.put("duracionFormateada", c.getDuracionFormateada());
            map.put("url", c.getUrl());
            map.put("tipo", c.getTipo() != null ? c.getTipo().toString() : "DESCONOCIDO");
            map.put("artistaBanda", c.getArtistaBanda());
            lista.add(map);
        }
        return Arrays.asList(lista.toArray());
    }

    public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        if (text == null || text.trim().isEmpty()) throw new Exception("El texto de búsqueda no puede estar vacío");
        LinkedList<Cancion> canciones = db.listAll();
        if (canciones.isEmpty()) return new ArrayList<>();
        if (!isValidAttribute(attribute)) throw new Exception("Atributo no válido para búsqueda: " + attribute);

        LinkedList<Cancion> resultados = new LinkedList<>();

        if (type == 1) {
            canciones.ordenarPorAtributo(attribute, true);
            if (text.length() == 1) {
                int idx = busquedaBinariaPorLetra(canciones, attribute, text);
                if (idx >= 0) {
                    resultados.add(canciones.get(idx));
                }
            } else {
                int idx = busquedaBinaria(canciones, attribute, text);
                if (idx >= 0) {
                    resultados.add(canciones.get(idx));
                }
            }
        } else {
            // lineal
            resultados = canciones.buscar(attribute, text, 0);
        }

        LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
        for (int i = 0; i < resultados.getLength(); i++) {
            Cancion c = resultados.get(i);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", c.getId());
            map.put("nombre", c.getNombre());
            map.put("genero", c.getGenero());
            map.put("album", c.getAlbum());
            map.put("duracion", c.getDuracion());
            map.put("duracionFormateada", c.getDuracionFormateada());
            map.put("url", c.getUrl());
            map.put("tipo", c.getTipo() != null ? c.getTipo().toString() : "DESCONOCIDO");
            map.put("artistaBanda", c.getArtistaBanda());
            lista.add(map);
        }
        return Arrays.asList(lista.toArray());
    }

 
    private void validarCampos(String nombre, String genero, Integer duracion, String url, String tipo, String album, String artistaBanda) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) throw new Exception("El nombre de la canción es requerido");
        if (genero == null || genero.trim().isEmpty()) throw new Exception("El género es requerido");
        if (duracion == null || duracion <= 0) throw new Exception("La duración debe ser mayor a 0");
        if (url == null || url.trim().isEmpty()) throw new Exception("La URL es requerida");
        if (album == null || album.trim().isEmpty()) throw new Exception("El álbum es requerido");
        if (tipo == null || tipo.trim().isEmpty()) throw new Exception("El tipo de archivo es requerido");
        if (artistaBanda == null || artistaBanda.trim().isEmpty()) throw new Exception("El artista o banda es requerido");
        try {
            TipoArchivoEnum.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new Exception("Tipo de archivo no válido: " + tipo);
        }
    }

    private boolean isValidAttribute(String attribute) {
        return attribute != null && (
            attribute.equals("nombre") ||
            attribute.equals("genero") ||
            attribute.equals("album") ||
            attribute.equals("duracion") ||
            attribute.equals("tipo") ||
            attribute.equals("artistaBanda")
        );
    }

    private Object obtenerValorAtributo(Cancion cancion, String atributo) {
        switch (atributo) {
            case "id": return cancion.getId();
            case "nombre": return cancion.getNombre();
            case "genero": return cancion.getGenero();
            case "album": return cancion.getAlbum();
            case "duracion": return cancion.getDuracion();
            case "tipo": return cancion.getTipo();
            case "artistaBanda": return cancion.getArtistaBanda();
            default: return null;
        }
    }

    private TipoArchivoEnum safeTipoArchivoEnum(String tipo) {
        if (tipo == null) return TipoArchivoEnum.MP3;
        try {
            return TipoArchivoEnum.valueOf(tipo.trim().toUpperCase());
        } catch (Exception e) {
            return TipoArchivoEnum.MP3; // Valor por defecto
        }
    }

    private int busquedaBinaria(LinkedList<Cancion> canciones, String atributo, String valorBuscado) {
        int left = 0;
        int right = canciones.getLength() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            Cancion c = canciones.get(mid);
            Object valorObj = obtenerValorAtributo(c, atributo);
            if (valorObj == null) {
                left = mid + 1;
                continue;
            }
            String valor = valorObj.toString();
            int cmp = valor.compareToIgnoreCase(valorBuscado);
            if (cmp == 0) {
                return mid; 
            } else if (cmp < 0) {
                left = mid + 1; 
            } else {
                right = mid - 1; 
            }
        }
        return -1; 
    }

    private int busquedaBinariaPorLetra(LinkedList<Cancion> canciones, String atributo, String letra) {
        if (letra == null || letra.isEmpty()) return -1;
        char target = Character.toUpperCase(letra.charAt(0));
        int left = 0;
        int right = canciones.getLength() - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            Cancion c = canciones.get(mid);
            Object valorObj = obtenerValorAtributo(c, atributo);
            if (valorObj == null) {
                left = mid + 1;
                continue;
            }
            String valor = valorObj.toString();
            if (valor.isEmpty()) {
                left = mid + 1;
                continue;
            }
            char firstChar = Character.toUpperCase(valor.charAt(0));
            if (firstChar == target) {
                return mid;
            } else if (firstChar < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; 
    }
}