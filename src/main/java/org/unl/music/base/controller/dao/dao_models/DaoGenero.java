package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Genero;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoGenero {
    private static final LinkedList<Genero> generos = new LinkedList<>();
    private static Integer lastId = 0;

    public LinkedList<Genero> listAll() {
        return generos;
    }

    public Genero findById(Integer id) {
        if (id == null) return null;
        for (int i = 0; i < generos.getLength(); i++) {
            Genero genero = generos.get(i);
            if (genero.getId().equals(id)) {
                return genero;
            }
        }
        return null;
    }

    public Boolean save(Genero genero) {
        try {
            lastId++;
            genero.setId(lastId);
            generos.add(genero);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Genero[] toArray() {
        return generos.toArray();
    }
}