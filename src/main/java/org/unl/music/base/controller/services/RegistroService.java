package org.unl.music.base.controller.services;

import java.util.Arrays;
import java.util.List;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import org.unl.music.base.controller.dao.dao_models.DaoRegistro;
import org.unl.music.base.models.Registro;

@BrowserCallable
@AnonymousAllowed
public class RegistroService {
    private DaoRegistro db;

    public RegistroService() {
        db = new DaoRegistro();
    }

    // Métodos estilo Hilla (para llamadas desde el frontend)
    public void createRegistro(String nombre, Integer edad, String correo, String clave, Boolean estado) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre es requerido");
        }
        if (edad == null || edad <= 0) {
            throw new Exception("La edad debe ser mayor a 0");
        }
        if (correo == null || correo.trim().isEmpty()) {
            throw new Exception("El correo es requerido");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new Exception("La clave es requerida");
        }

        db.getObj().setNombre(nombre);
        db.getObj().setEdad(edad);
        db.getObj().setCorreo(correo);
        db.getObj().setClave(clave);
        db.getObj().setEstado(estado != null ? estado : true);

        if (!db.save()) {
            throw new Exception("No se pudo guardar el registro");
        }
    }

    public void updateRegistro(Integer id, String nombre, Integer edad, String clave, Boolean estado) throws Exception {
        if (id == null || id <= 0) {
            throw new Exception("El ID es requerido para modificar");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre es requerido");
        }
        if (edad == null || edad <= 0) {
            throw new Exception("La edad debe ser mayor a 0");
        }
        if (clave == null || clave.trim().isEmpty()) {
            throw new Exception("La clave es requerida");
        }

        db.setObj(db.listAll().get(id - 1));
        db.getObj().setNombre(nombre);
        db.getObj().setEdad(edad);
        // No se permite modificar el correo
        db.getObj().setClave(clave);
        db.getObj().setEstado(estado != null ? estado : true);

        if (!db.update(id - 1)) {
            throw new Exception("No se pudo modificar el registro");
        }
    }

    public List<Registro> listAllRegistro() {
        return Arrays.asList(db.listAll().toArray());
    }

    // Métodos wrapper para compatibilidad con Views existentes
    public void crear(Registro registro) throws Exception {
        createRegistro(
            registro.getNombre(),
            registro.getEdad(),
            registro.getCorreo(),
            registro.getClave(),
            registro.getEstado()
        );
    }

    public void modificar(Registro registro) throws Exception {
        updateRegistro(
            registro.getId(),
            registro.getNombre(),
            registro.getEdad(),
            registro.getClave(),
            registro.getEstado()
        );
    }

    public List<Registro> listar() {
        return listAllRegistro();
    }

    // Método auxiliar para buscar por ID (útil para validaciones)
    public Registro buscarPorId(Integer id) {
        if (id == null || id <= 0) return null;
        try {
            return db.listAll().get(id - 1);
        } catch (Exception e) {
            return null;
        }
    }
}