package org.unl.music.base.controller.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.unl.music.base.controller.dao.dao_models.DaoCuenta;
import org.unl.music.base.controller.dao.dao_models.DaoPersona;
import org.unl.music.base.models.Cuenta;
import org.unl.music.base.models.Persona;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@BrowserCallable
@AnonymousAllowed
public class PersonaService {
    private final DaoPersona db;
    private final DaoCuenta daoCuenta;

    public PersonaService() {
        this.db = new DaoPersona();
        this.daoCuenta = new DaoCuenta();
    }

    public HashMap<String, String> save(
            @NotEmpty String usuario,
            @NotEmpty @Email String correo,
            @NotEmpty String clave,
            @NotNull Integer edad
    ) throws Exception {
        if (usuario == null || usuario.trim().isEmpty()) 
            throw new Exception("El usuario es requerido");
        if (correo == null || correo.trim().isEmpty()) 
            throw new Exception("El correo es requerido");
        if (clave == null || clave.trim().isEmpty()) 
            throw new Exception("La clave es requerida");
        if (edad == null || edad <= 0) 
            throw new Exception("La edad debe ser mayor a 0");

        try {
            List<Cuenta> cuentas = daoCuenta.listAll().toJavaList();
            for (Cuenta c : cuentas) {
                if (c.getCorreo().equalsIgnoreCase(correo.trim())) {
                    throw new Exception("El correo ya está registrado");
                }
            }

            Persona persona = new Persona();
            persona.setUsuario(usuario.trim());
            persona.setEdad(edad);
            if (!db.save(persona))
                throw new Exception("No se pudo guardar los datos de la persona");

            Cuenta cuenta = new Cuenta();
            cuenta.setClave(clave.trim());
            cuenta.setCorreo(correo.trim());
            cuenta.setEstado(Boolean.TRUE);
            cuenta.setId_persona(persona.getId());
            if (!daoCuenta.save(cuenta))
                throw new Exception("No se pudo guardar la cuenta");

            HashMap<String, String> resultado = new HashMap<>();
            resultado.put("id", persona.getId().toString());
            resultado.put("usuario", persona.getUsuario());
            resultado.put("edad", persona.getEdad().toString());
            resultado.put("correo", cuenta.getCorreo());
            return resultado;

        } catch (Exception e) {
            throw new Exception("Error al guardar: " + e.getMessage());
        }
    }

    public HashMap<String, String> update(
            @NotNull Integer id,
            @NotEmpty String usuario,
            @NotEmpty @Email String correo,
            @NotEmpty String clave,
            @NotNull Integer edad
    ) throws Exception {
        if (id == null || id <= 0) 
            throw new Exception("ID inválido");
        if (usuario == null || usuario.trim().isEmpty()) 
            throw new Exception("El usuario es requerido");
        if (correo == null || correo.trim().isEmpty()) 
            throw new Exception("El correo es requerido");
        if (clave == null || clave.trim().isEmpty()) 
            throw new Exception("La clave es requerida");
        if (edad == null || edad <= 0) 
            throw new Exception("La edad debe ser mayor a 0");

        try {
            Persona persona = db.findById(id);
            if (persona == null) 
                throw new Exception("No existe la persona con ID: " + id);

            Cuenta cuenta = buscarCuentaPorIdPersona(id);
            if (cuenta == null) 
                throw new Exception("No se encontró la cuenta asociada");

            persona.setUsuario(usuario.trim());
            persona.setEdad(edad);
            if (!db.update(persona))
                throw new Exception("No se pudo actualizar los datos de la persona");

            cuenta.setClave(clave.trim());
            if (!daoCuenta.update(cuenta))
                throw new Exception("No se pudo actualizar la cuenta");

            HashMap<String, String> resultado = new HashMap<>();
            resultado.put("id", persona.getId().toString());
            resultado.put("usuario", persona.getUsuario());
            resultado.put("edad", persona.getEdad().toString());
            resultado.put("correo", cuenta.getCorreo());
            return resultado;

        } catch (Exception e) {
            throw new Exception("Error al actualizar: " + e.getMessage());
        }
    }

    public List<HashMap<String, String>> listaPersonas() {
        List<HashMap<String, String>> lista = new ArrayList<>();
        
        try {
            List<Persona> personas = db.listAll().toJavaList();
            for (Persona persona : personas) {
                HashMap<String, String> personaData = new HashMap<>();
                personaData.put("id", persona.getId().toString());
                personaData.put("usuario", persona.getUsuario());
                personaData.put("edad", persona.getEdad().toString());

                Cuenta cuenta = buscarCuentaPorIdPersona(persona.getId());
                personaData.put("correo", cuenta != null ? cuenta.getCorreo() : "");
                
                lista.add(personaData);
            }
        } catch (Exception e) {
            // Log el error si es necesario
        }
        
        return lista;
    }

    private Cuenta buscarCuentaPorIdPersona(Integer idPersona) {
        List<Cuenta> cuentas = daoCuenta.listAll().toJavaList();
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getId_persona().equals(idPersona)) {
                return cuenta;
            }
        }
        return null;
    }
}