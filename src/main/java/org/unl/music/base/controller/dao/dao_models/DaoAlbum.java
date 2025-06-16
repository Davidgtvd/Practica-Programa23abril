package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Album;

import java.util.Date;

import org.unl.music.base.controller.dao.AdapterDao;

public class DaoAlbum extends AdapterDao<Album> {
    private Album obj;

    public DaoAlbum() {
        super(Album.class);
        // TODO Auto-generated constructor stub
    }

    public Album getObj() {
        if (obj == null)
            this.obj = new Album();
        return this.obj;
    }

    public void setObj(Album obj) {
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

    public static void main(String[] args) {
        DaoAlbum da = new DaoAlbum();
        
        da.getObj().setNombre("Queens");
        da.getObj().setId_banda(1);
        da.getObj().setFecha(new Date());
        
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
        da.setObj(null);
        da.getObj().setNombre("El loco de tercero");
        da.getObj().setId_banda(3);
        da.getObj().setFecha(new Date());
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
    }

}
