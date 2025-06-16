package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Artista;
import org.unl.music.base.models.Artista_Banda;
import org.unl.music.base.models.Banda;
import org.unl.music.base.models.RolArtistaEnum;

import java.util.Date;
import java.util.HashMap;

import org.checkerframework.checker.units.qual.t;
import org.unl.music.base.controller.Utiles;
import org.unl.music.base.controller.dao.AdapterDao;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoArtistaBanda extends AdapterDao<Artista_Banda> {
    private Artista_Banda obj;

    public DaoArtistaBanda() {
        super(Artista_Banda.class);
        // TODO Auto-generated constructor stub
    }

    public Artista_Banda getObj() {
        if (obj == null)
            this.obj = new Artista_Banda();
        return this.obj;
    }

    public void setObj(Artista_Banda obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            // TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            // TODO
            return false;
            // TODO: handle exception
        }
    }

    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Artista_Banda[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    private HashMap<String, String> toDict(Artista_Banda arreglo) throws Exception {
        DaoArtista da = new DaoArtista();
        DaoBanda db = new DaoBanda();
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", arreglo.getId().toString());
        aux.put("rol", arreglo.getRol().toString());
        aux.put("artista", da.get(arreglo.getId_artista()).getNombres());
        aux.put("banda", db.get(arreglo.getId_banda()).getNombre());
        return aux;
    }

    public LinkedList<HashMap<String, String>> orderByArtist(Integer type, String attribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap arr[] = lista.toArray();
            int n = arr.length;
            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(attribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(attribute).toString().toLowerCase()) < 0) {
                            min_idx = j;
                        }

                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(attribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(attribute).toString().toLowerCase()) > 0) {
                            min_idx = j;
                        }

                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }
        }
        return lista;
    }

    public LinkedList<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();
        
        if (!lista.isEmpty()) {
            HashMap<String, String>[] arr = lista.toArray();
            System.out.println(attribute+" "+text+" ** *** * * ** * * * *");
            switch (type) {
                case 1:
                System.out.println(attribute+" "+text+" UNO");
                    for (HashMap m : arr) {
                        if (m.get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                case 2:
                System.out.println(attribute+" "+text+" DOS");
                    for (HashMap m : arr) {
                        if (m.get(attribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
                default:
                System.out.println(attribute+" "+text+" TRES");
                    for (HashMap m : arr) {
                        System.out.println("***** "+m.get(attribute)+"   "+attribute);
                        if (m.get(attribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(m);
                        }
                    }
                    break;
            }
        }
        return resp;
    }

}
