package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Persona;

import org.unl.music.base.controller.dao.AdapterDao;

public class DaoPersona extends AdapterDao<Persona> {
    private Persona obj;

    public DaoPersona() {
        super(Persona.class);
        // TODO Auto-generated constructor stub
    }

    public Persona getObj() {
        if (obj == null)
            this.obj = new Persona();
        return this.obj;
    }

    public void setObj(Persona obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength()+1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
            // TODO: handle exception
        }
    }

    

}
