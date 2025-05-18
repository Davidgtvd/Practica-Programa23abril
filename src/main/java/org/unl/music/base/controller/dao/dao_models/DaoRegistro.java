package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Registro;
import org.unl.music.base.controller.dao.AdapterDao;

public class DaoRegistro extends AdapterDao<Registro> {
    private Registro obj;

    public DaoRegistro() {
        super(Registro.class);
    }

    public Registro getObj() {
        if (obj == null)
            this.obj = new Registro();
        return this.obj;
    }

    public void setObj(Registro obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            // Asigna un ID incremental
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}