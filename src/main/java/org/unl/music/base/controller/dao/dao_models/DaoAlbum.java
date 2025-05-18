package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Album;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoAlbum {
    private static final LinkedList<Album> albums = new LinkedList<>();
    private static Integer lastId = 0;

    public LinkedList<Album> listAll() {
        return albums;
    }

    public Album findById(Integer id) {
        if (id == null) return null;
        for (int i = 0; i < albums.getLength(); i++) {
            Album album = albums.get(i);
            if (album.getId().equals(id)) {
                return album;
            }
        }
        return null;
    }

    public Boolean save(Album album) {
        try {
            lastId++;
            album.setId(lastId);
            albums.add(album);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Album[] toArray() {
        return albums.toArray();
    }
    
}