package org.unl.music.base.controller.dao.dao_models;

import java.util.HashMap;
import org.unl.music.base.controller.Utiles;
import org.unl.music.base.controller.dao.AdapterDao;
import org.unl.music.base.controller.data_struct.list.LinkedList;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.TipoArchivoEnum;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;

    public DaoCancion() {
        super(Cancion.class);
    }

    public Cancion getObj() {
        if (obj == null)
            this.obj = new Cancion();
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    //crud

    /**
     * Guarda una nueva cancion
     */
    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar canción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Crea una nueva cancion
     */
    public void create(Cancion cancion) throws Exception {
        if (cancion == null) {
            throw new Exception("La canción no puede ser null");
        }
        
        if (existeCancion(cancion.getNombre(), cancion.getAlbum(), cancion.getArtistaBanda())) {
            throw new Exception("Ya existe una canción con el nombre '" + cancion.getNombre() + 
                              "' en el álbum '" + cancion.getAlbum() + "' del artista/banda '" + cancion.getArtistaBanda() + "'");
        }

        this.obj = cancion;
        if (!save()) {
            throw new Exception("No se pudo guardar la canción");
        }
    }

    /**
     * Actualiza una canción por posicion
     */
    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar canción: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza una cancion 
     */
    public void update(Cancion cancion) throws Exception {
        if (cancion == null) {
            throw new Exception("La canción no puede ser null");
        }
        if (cancion.getId() == null) {
            throw new Exception("El ID de la canción es requerido para actualizar");
        }

        // Buscar pos canción
        LinkedList<Cancion> lista = listAll();
        Integer pos = null;
        for (int i = 0; i < lista.getLength(); i++) {
            if (lista.get(i).getId().equals(cancion.getId())) {
                pos = i;
                break;
            }
        }

        if (pos == null) {
            throw new Exception("No se encontró la canción con ID: " + cancion.getId());
        }

        this.obj = cancion;
        if (!update(pos)) {
            throw new Exception("No se pudo actualizar la canción");
        }
    }

    /**
     * Elimina una cancion 
     */
    public void delete(Integer id) throws Exception {
        if (id == null) {
            throw new Exception("El ID es requerido para eliminar");
        }

        LinkedList<Cancion> lista = listAll();
        Integer pos = null;
        for (int i = 0; i < lista.getLength(); i++) {
            if (lista.get(i).getId().equals(id)) {
                pos = i;
                break;
            }
        }

        if (pos == null) {
            throw new Exception("No se encontró la canción con ID: " + id);
        }

        try {
            this.delete(pos);
        } catch (Exception e) {
            throw new Exception("Error al eliminar la canción: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las canciones
     */
    public Cancion get(Integer id) throws Exception {
        if (id == null) {
            throw new Exception("El ID es requerido");
        }

        LinkedList<Cancion> lista = listAll();
        for (int i = 0; i < lista.getLength(); i++) {
            Cancion cancion = lista.get(i);
            if (cancion.getId().equals(id)) {
                return cancion;
            }
        }
        return null;
    }
    /**
     * Lista todas las canciones
     */

    
    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    /**
     * Convierte una Cancion a un HashMap
     */
    private HashMap<String, String> toDict(Cancion cancion) {
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", cancion.getId().toString());
        aux.put("nombre", cancion.getNombre());
        aux.put("genero", cancion.getGenero());
        aux.put("album", cancion.getAlbum());
        aux.put("duracion", cancion.getDuracion().toString());
        aux.put("duracionFormateada", cancion.getDuracionFormateada());
        aux.put("url", cancion.getUrl());
        aux.put("tipo", cancion.getTipo().toString());
        aux.put("artistaBanda", cancion.getArtistaBanda() != null ? cancion.getArtistaBanda() : "");
        return aux;
    }

    /**
     * Ordena las canciones por atributo
     *    */
    public LinkedList<HashMap<String, String>> orderByCancion(Integer type, String attribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap[] arr = lista.toArray();
            int n = arr.length;
            
            if (type == Utiles.ASCEDENTE) {
                             for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (compareValues(arr[j].get(attribute), arr[min_idx].get(attribute)) < 0) {
                            min_idx = j;
                        }
                    }
                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int max_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (compareValues(arr[j].get(attribute), arr[max_idx].get(attribute)) > 0) {
                            max_idx = j;
                        }
                    }
                    HashMap temp = arr[max_idx];
                    arr[max_idx] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return lista;
    }

    /**
     * Busca canciones por atributo
     */
    public LinkedList<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();
        
        if (!lista.isEmpty()) {
            HashMap<String, String>[] arr = lista.toArray();
            System.out.println(attribute + " " + text + " ** *** * * ** * * * *");
            
            switch (type) {
                case 1:
                    System.out.println(attribute + " " + text + " UNO - INICIA CON");
                    for (HashMap m : arr) {
                        if (m.get(attribute) != null && 
                            m.get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                case 2:
                    System.out.println(attribute + " " + text + " DOS - TERMINA CON");
                    for (HashMap m : arr) {
                        if (m.get(attribute) != null && 
                            m.get(attribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                default:
                    System.out.println(attribute + " " + text + " TRES - CONTIENE");
                    for (HashMap m : arr) {
                        System.out.println("***** " + m.get(attribute) + "   " + attribute);
                        if (m.get(attribute) != null && 
                            m.get(attribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
            }
        }
        return resp;
    }

   
    /**
     * Verifica si una canción ya existe
     */
    private boolean existeCancion(String nombre, String album, String artistaBanda) throws Exception {
        if (nombre == null || album == null || artistaBanda == null) return false;
        
        LinkedList<Cancion> lista = listAll();
        for (int i = 0; i < lista.getLength(); i++) {
            Cancion cancion = lista.get(i);
            if (cancion.getNombre() != null && cancion.getAlbum() != null && cancion.getArtistaBanda() != null &&
                cancion.getNombre().toLowerCase().equals(nombre.toLowerCase()) &&
                cancion.getAlbum().toLowerCase().equals(album.toLowerCase()) &&
                cancion.getArtistaBanda().toLowerCase().equals(artistaBanda.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Compara dos valores para ordenación
     */
    private int compareValues(Object a, Object b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        
        String strA = a.toString().toLowerCase();
        String strB = b.toString().toLowerCase();
        
        try {
            Integer numA = Integer.parseInt(strA);
            Integer numB = Integer.parseInt(strB);
            return numA.compareTo(numB);
        } catch (NumberFormatException e) {
                return strA.compareTo(strB);
        }
    }

    /**
     * Valida si el atributo es válido para búsqueda/ordenación
     */
    private boolean isValidAttribute(String attribute) {
        return attribute != null && (
            attribute.equals("nombre") ||
            attribute.equals("genero") ||
            attribute.equals("album") ||
            attribute.equals("duracion") ||
            attribute.equals("tipo") ||
            attribute.equals("url") ||
            attribute.equals("artistaBanda") ||
            attribute.equals("id")
        );
    }


    /**
     * Cuenta el total 
     */
    public int contarCanciones() throws Exception {
        return listAll().getLength();
    }

    /**
     * canciones por género
     */
    public LinkedList<Cancion> obtenerPorGenero(String genero) throws Exception {
        LinkedList<Cancion> resultado = new LinkedList<>();
        LinkedList<Cancion> todas = listAll();
        
        for (int i = 0; i < todas.getLength(); i++) {
            Cancion cancion = todas.get(i);
            if (cancion.getGenero() != null && 
                cancion.getGenero().toLowerCase().equals(genero.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    /**
     * canciones por álbum
     */
    public LinkedList<Cancion> obtenerPorAlbum(String album) throws Exception {
        LinkedList<Cancion> resultado = new LinkedList<>();
        LinkedList<Cancion> todas = listAll();
        
        for (int i = 0; i < todas.getLength(); i++) {
            Cancion cancion = todas.get(i);
            if (cancion.getAlbum() != null && 
                cancion.getAlbum().toLowerCase().equals(album.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    /**
     * canciones por artista/banda
     */
    public LinkedList<Cancion> obtenerPorArtistaBanda(String artistaBanda) throws Exception {
        LinkedList<Cancion> resultado = new LinkedList<>();
        LinkedList<Cancion> todas = listAll();
        
        for (int i = 0; i < todas.getLength(); i++) {
            Cancion cancion = todas.get(i);
            if (cancion.getArtistaBanda() != null && 
                cancion.getArtistaBanda().toLowerCase().equals(artistaBanda.toLowerCase())) {
                resultado.add(cancion);
            }
        }
        return resultado;
    }

    /**
     * uración total de todas las canciones
     */
    public int obtenerDuracionTotal() throws Exception {
        LinkedList<Cancion> todas = listAll();
        int total = 0;
        
        for (int i = 0; i < todas.getLength(); i++) {
            Cancion cancion = todas.get(i);
            if (cancion.getDuracion() != null) {
                total += cancion.getDuracion();
            }
        }
        return total;
    }

    
    public void generarDatosPrueba(int cantidad) throws Exception {
        String[] nombres = {
            "Bohemian Rhapsody", "Stairway to Heaven", "Hotel California", "Imagine", "Sweet Child O' Mine",
            "Smells Like Teen Spirit", "Billie Jean", "Like a Rolling Stone", "Purple Haze", "Hey Jude",
            "Thunderstruck", "Comfortably Numb", "Sweet Caroline", "Don't Stop Believin'", "Livin' on a Prayer"
        };
        
        String[] generos = {
            "Rock", "Pop", "Jazz", "Classical", "Electronic", "Blues", "Country", "Reggae", "Hip Hop", "Folk"
        };
        
        String[] albums = {
            "A Night at the Opera", "Led Zeppelin IV", "Hotel California", "Imagine", "Appetite for Destruction",
            "Nevermind", "Thriller", "Highway 61 Revisited", "Are You Experienced", "Abbey Road"
        };

        String[] artistas = {
            "Queen", "Led Zeppelin", "Eagles", "John Lennon", "Guns N' Roses",
            "Nirvana", "Michael Jackson", "Bob Dylan", "Jimi Hendrix", "The Beatles",
            "AC/DC", "Pink Floyd", "Neil Diamond", "Journey", "Bon Jovi"
        };
        
        TipoArchivoEnum[] tipos = TipoArchivoEnum.values();
        
        System.out.println("Generando " + cantidad + " canciones de prueba...");
        
        for (int i = 0; i < cantidad; i++) {
            Cancion cancion = new Cancion();
            cancion.setNombre(nombres[i % nombres.length] + " " + (i + 1));
            cancion.setGenero(generos[i % generos.length]);
            cancion.setAlbum(albums[i % albums.length] + " Vol." + ((i / albums.length) + 1));
            cancion.setArtistaBanda(artistas[i % artistas.length]);
            cancion.setTipo(tipos[i % tipos.length]);
            cancion.setDuracion(120 + (i % 360)); // Entre 2 y 8 minutos
            cancion.setUrl("https://ejemplo.com/cancion" + (i + 1) + "." + cancion.getTipo().toString().toLowerCase());
            
            this.obj = cancion;
            save();
        }
        
        System.out.println("✓ Datos de prueba generados exitosamente.");
    }
}