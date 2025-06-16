package org.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unl.music.base.controller.dao.dao_models.DaoAlbum;
import org.unl.music.base.controller.dao.dao_models.DaoArtistaBanda;
import org.unl.music.base.controller.dao.dao_models.DaoCuenta;
import org.unl.music.base.controller.dao.dao_models.DaoGenero;
import org.unl.music.base.controller.dao.dao_models.DaoPersona;
import org.unl.music.base.models.Cancion;
import org.unl.music.base.models.Cuenta;
import org.unl.music.base.models.Persona;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class PersonaService {
    private DaoPersona db;

    public PersonaService() {
        db = new DaoPersona();
    }

    public void save( @NotEmpty String usuario,  @NotEmpty @Email String correo,  @NotEmpty String clave, Integer edad) throws Exception {
        if(usuario.trim().length() > 0 && correo.trim().length() > 0 && clave.trim().length() > 0 && edad > 0){
            db.getObj().setUsuario(usuario);
            db.getObj().setEdad(edad);
            if(!db.save())
                throw new  Exception("No se pudo guardar los datos de la persona");
            else {
                DaoCuenta dc = new DaoCuenta();
                dc.getObj().setClave(clave);
                dc.getObj().setCorreo(correo);
                dc.getObj().setEstado(Boolean.TRUE);
                dc.getObj().setId_persona(db.getObj().getId());
                if(!dc.save())
                    throw new  Exception("No se pudo guardar los datos de la cuenta");
            }
        } else {
            throw new  Exception("No se pudo guardar los datos de persona");
        }
    }

    public List<HashMap> listaPersonas() {
        List<HashMap> lista = new ArrayList<>();
        if (!db.listAll().isEmpty()) {
            Persona[] arreglo = db.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {

                try {
                    HashMap<String, String> aux = new HashMap<>();
                    aux.put("id", arreglo[i].getId().toString(i));
                    aux.put("usuario", arreglo[i].getUsuario());
                    aux.put("edad", arreglo[i].getEdad().toString());
                    Cuenta c = new DaoCuenta().listAll().get(arreglo[i].getId() - 1);// ya que todos deben tener cuenta asi que
                                                                           // cuando se guardar, se guardaran tanto
                                                                           // cuenta como persona con el mismo ID
                    aux.put("correo", c.getCorreo());

                    lista.add(aux);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }
        return lista;
    }
}
