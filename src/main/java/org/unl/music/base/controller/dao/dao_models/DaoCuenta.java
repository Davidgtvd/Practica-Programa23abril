package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Cuenta;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoCuenta {
    private static final LinkedList<Cuenta> cuentas = new LinkedList<>();
    private static Integer lastId = 0;

    public LinkedList<Cuenta> listAll() {
        return cuentas;
    }

    public Cuenta findById(Integer id) {
        if (id == null) return null;
        for (int i = 0; i < cuentas.getLength(); i++) {
            Cuenta cuenta = cuentas.get(i);
            if (cuenta.getId().equals(id)) {
                return cuenta;
            }
        }
        return null;
    }

    public Cuenta findByCorreo(String correo) {
        if (correo == null) return null;
        for (int i = 0; i < cuentas.getLength(); i++) {
            Cuenta cuenta = cuentas.get(i);
            if (cuenta.getCorreo() != null && cuenta.getCorreo().equals(correo)) {
                return cuenta;
            }
        }
        return null;
    }

    public Boolean save(Cuenta cuenta) {
        try {
            // Verificar si ya existe una cuenta con ese correo
            if (findByCorreo(cuenta.getCorreo()) != null) {
                return false;
            }
            lastId++;
            cuenta.setId(lastId);
            cuentas.add(cuenta);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Cuenta cuenta) {
        try {
            // Verificar que no exista otra cuenta con el mismo correo
            Cuenta existente = findByCorreo(cuenta.getCorreo());
            if (existente != null && !existente.getId().equals(cuenta.getId())) {
                return false;
            }
            for (int i = 0; i < cuentas.getLength(); i++) {
                if (cuentas.get(i).getId().equals(cuenta.getId())) {
                    cuentas.update(cuenta, i);
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
            for (int i = 0; i < cuentas.getLength(); i++) {
                if (cuentas.get(i).getId().equals(id)) {
                    cuentas.delete(i);
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