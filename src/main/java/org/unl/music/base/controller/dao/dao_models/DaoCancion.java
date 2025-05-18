package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Cancion;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoCancion {
    private static final LinkedList<Cancion> canciones = new LinkedList<>();
    private static Integer lastId = 0;

    public LinkedList<Cancion> listAll() {
        return canciones;
    }

    public Cancion findById(Integer id) {
        if (id == null) return null;
        for (int i = 0; i < canciones.getLength(); i++) {
            Cancion cancion = canciones.get(i);
            if (cancion.getId().equals(id)) {
                return cancion;
            }
        }
        return null;
    }

    public Boolean save(Cancion cancion) {
        try {
            lastId++;
            cancion.setId(lastId);
            canciones.add(cancion);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Cancion cancion) {
        try {
            for (int i = 0; i < canciones.getLength(); i++) {
                if (canciones.get(i).getId().equals(cancion.getId())) {
                    canciones.update(cancion, i);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(Integer id) {
        try {
            for (int i = 0; i < canciones.getLength(); i++) {
                if (canciones.get(i).getId().equals(id)) {
                    canciones.delete(i);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}