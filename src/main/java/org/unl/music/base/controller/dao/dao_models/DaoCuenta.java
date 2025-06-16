package org.unl.music.base.controller.dao.dao_models;

import org.unl.music.base.models.Artista;
import org.unl.music.base.models.Cuenta;

import java.util.HashMap;

import org.unl.music.base.controller.Utiles;
import org.unl.music.base.controller.dao.AdapterDao;
import org.unl.music.base.controller.data_struct.list.LinkedList;

public class DaoCuenta extends AdapterDao<Cuenta> {
    private Cuenta obj;

    public DaoCuenta() {
        super(Cuenta.class);
        // TODO Auto-generated constructor stub
    }

    public Cuenta getObj() {
        if (obj == null)
            this.obj = new Cuenta();
        return this.obj;
    }

    public void setObj(Cuenta obj) {
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

    public HashMap<String, Object> toDict(Cuenta c) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        DaoPersona dp = new DaoPersona();
        dp.setObj(dp.get(c.getId_persona()));
        map.put("correo", c.getCorreo());
        map.put("id", c.getId());
        map.put("estado", c.getEstado());
        map.put("usuario", dp.getObj().getUsuario());
        return map;
    }

    private HashMap<String, Object> toDictPassword(Cuenta c) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        DaoPersona dp = new DaoPersona();

        dp.setObj(dp.get(c.getId_persona()));
        map.put("correo", c.getCorreo());
        map.put("id", c.getId());
        map.put("clave", c.getClave());
        map.put("estado", c.getEstado());
        map.put("usuario", dp.getObj().getUsuario());
        return map;
    }

    private LinkedList<HashMap<String, Object>> listPrivate() throws Exception {
        LinkedList<HashMap<String, Object>> lista = new LinkedList<>();
        if (!listAll().isEmpty()) {
            Cuenta[] aux = listAll().toArray();
            for (Cuenta c : aux) {
                lista.add(toDictPassword(c));
            }
        }
        return lista;
    }

    private int partition(HashMap<String, Object> arr[], int begin, int end, Integer type, String attribute) {
        // hashmap //clave - valor
        // Calendar cd = Calendar.getInstance();

        HashMap<String, Object> pivot = arr[end];
        int i = (begin - 1);
        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) < 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().compareTo(pivot.get(attribute).toString()) > 0) {
                    // if (arr[j] <= pivot) {
                    i++;
                    HashMap<String, Object> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, Object> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quickSort(HashMap<String, Object> arr[], int begin, int end, Integer type, String attribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, attribute);

            quickSort(arr, begin, partitionIndex - 1, type, attribute);
            quickSort(arr, partitionIndex + 1, end, type, attribute);
        }
    }

    /*
     * public LinkedList<Artista> orderQ(Integer type) {
     * LinkedList<Artista> lista = new LinkedList<>();
     * if (!listAll().isEmpty()) {
     * 
     * Artista arr[] = listAll().toArray();
     * quickSort(arr, 0, arr.length - 1, type);
     * lista.toList(arr);
     * }
     * return lista;
     * }
     */

    public HashMap<String, Object> login(String email, String password) throws Exception {
        if (!listAll().isEmpty()) {
            HashMap<String, Object>[] arreglo = listPrivate().toArray();
            quickSort(arreglo, 0, arreglo.length - 1, 1, "correo");
            HashMap<String, Object> search = BinarySearchRecursive(arreglo, 0, arreglo.length - 1, "correo", email);
            if(search != null) {
                if (((Boolean)search.get("estado"))){
                    if(search.get("clave").toString().equals(password)) {
                        return toDict(get((Integer)search.get("id")));
                    } else throw new Exception("Su clave o usaurio son incorrectos");
                } else throw new Exception("Cuenta desactivada");
            } else
                throw new Exception("No se encontro la cuenta");
        } else
            return null;
    }
    // 1, 6, 9, 12, 14, 16
    //3, -1
    //3, 1
    public HashMap<String, Object> BinarySearchRecursive(HashMap<String, Object> arr[], int a, int b, String attribute,
            String value) throws Exception {
        // Base Case to Exit the Recursive Function
        if (b < 1) {
            return null;
        }
        int n = a + (b = 1) / 2;
        // If number is found at mean index of start and end
        if (arr[n].get(attribute).toString().equals(value))
            return arr[n];
        // If number to search for is greater than the arr value at index 'n'
        else if (arr[n].get(attribute).toString().compareTo(value) > 0)
            return BinarySearchRecursive(arr, a, n - 1, attribute, value);
        // If number to search for is greater than the arr value at index 'n'
        else
            return BinarySearchRecursive(arr, n + 1, b, attribute, value);
    }

}
