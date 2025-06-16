package org.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.unl.music.base.controller.dao.dao_models.DaoArtista;
import org.unl.music.base.controller.dao.dao_models.DaoArtistaBanda;
import org.unl.music.base.controller.dao.dao_models.DaoBanda;
import org.unl.music.base.controller.data_struct.list.LinkedList;
import org.unl.music.base.models.Artista_Banda;
import org.unl.music.base.models.Banda;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
@AnonymousAllowed
public class ArtistaBandaServices {
    private DaoArtistaBanda db;
    public ArtistaBandaServices(){
        db = new DaoArtistaBanda();
    }
    public List<HashMap> listAll() throws Exception{
        
        return Arrays.asList(db.all().toArray());
    }

    public List<HashMap> order(String attribute, Integer type) throws Exception {
        return Arrays.asList(db.orderByArtist(type, attribute).toArray());
    } 

    public List<HashMap> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = db.search(attribute, text, type);
        if(!lista.isEmpty())
            return Arrays.asList(lista.toArray());
        else
            return new ArrayList<>();
    }
    
    
}
