package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Persona;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoPersona {
    private static final LinkedList<Persona> personas = new LinkedList<>();
    private static Integer lastId = 0;

    public LinkedList<Persona> listAll() {
        return personas;
    }

    public Persona findById(Integer id) {
        if (id == null) return null;
        
        for (int i = 0; i < personas.getLength(); i++) {
            Persona persona = personas.get(i);
            if (persona.getId().equals(id)) {
                return persona;
            }
        }
        return null;
    }

    public Boolean save(Persona persona) {
        try {
            lastId++;
            persona.setId(lastId);
            personas.add(persona);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Persona persona) {
        try {
            for (int i = 0; i < personas.getLength(); i++) {
                if (personas.get(i).getId().equals(persona.getId())) {
                    personas.update(persona, i);
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
            for (int i = 0; i < personas.getLength(); i++) {
                if (personas.get(i).getId().equals(id)) {
                    personas.delete(i);
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