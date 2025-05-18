package org.unl.music.base.controller.data_struct.list;

import org.unl.music.base.controller.data_struct.stack.Stack;
import org.unl.music.base.controller.data_struct.stack.StackImplementation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LinkedList<E> implements Iterable<E> {
    private Node<E> head;
    private Node<E> last;
    private Integer length;

    public Integer getLength() {
        return this.length;
    }

    public LinkedList() {
        head = null;
        last = null;
        length = 0;
    }

    public Boolean isEmpty() {
        return head == null || length == 0;
    }

    private Node<E> getNode(Integer pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
            // System.out.println("Lista vacia");
            // return null;
        } else if (pos < 0 || pos >= length) {
            // System.out.println("Fuera de rango");
            // return null;
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return head;
        } else if ((length.intValue() - 1) == pos.intValue()) {
            return last;
        } else {
            Node<E> search = head;
            Integer cont = 0;
            while (cont < pos) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    private E getDataFirst() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else {
            return head.getData();
        }
    }

    private E getDataLast() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else {
            return last.getData();
        }
    }

    public E get(Integer pos) {
        return getNode(pos).getData();
        /*
         * if (isEmpty()) {
         * throw new ArrayIndexOutOfBoundsException("List empty");
         * // System.out.println("Lista vacia");
         * // return null;
         * } else if (pos < 0 || pos >= length) {
         * // System.out.println("Fuera de rango");
         * // return null;
         * throw new ArrayIndexOutOfBoundsException("Index out range");
         * }else if (pos == 0) {
         * return getDataFirst();
         * } else if (length.intValue() == pos.intValue()) {
         * return getDataLast();
         * } else {
         * return getNode(pos).getData();
         * }
         */
    }

    private void addFirst(E data) {
        if (isEmpty()) {
            Node<E> aux = new Node<>(data);
            head = aux;
            last = aux;
        } else {
            Node<E> head_old = head;
            Node<E> aux = new Node<>(data, head_old);
            head = aux;
        }
        length++;
    }

    private void addLast(E data) {
        if (isEmpty()) {
            addFirst(data);
        } else {
            Node<E> aux = new Node<>(data);
            last.setNext(aux);
            last = aux;
            length++;
        }

    }

    public void add(E data, Integer pos) throws Exception {
        if (pos == 0) {
            addFirst(data);
        } else if (length.intValue() == pos.intValue()) {
            addLast(data);
        } else {
            Node<E> search_preview = getNode(pos - 1);
            Node<E> search = getNode(pos);
            Node<E> aux = new Node<>(data, search);
            search_preview.setNext(aux);
            length++;
        }
    }

    public void add(E data) {
        addLast(data);
    }

    public String print() {
        if (isEmpty())
            return "Esta vacia";
        else {
            StringBuilder resp = new StringBuilder();
            Node<E> help = head;
            while (help != null) {
                // resp += help.getData()+" - ";
                resp.append(help.getData()).append(" - ");
                help = help.getNext();
            }
            resp.append("\n");
            return resp.toString();
        }
    }

    public void update(E data, Integer pos) {
        getNode(pos).setData(data);
    }

    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    public E[] toArray() {
        Class clazz = null;
        E[] matriz = null;
        if (this.length > 0) {
            clazz = head.getData().getClass();
            matriz = (E[]) java.lang.reflect.Array.newInstance(clazz, this.length);
            Node<E> aux = head;
            for (int i = 0; i < length; i++) {
                matriz[i] = aux.getData();
                aux = aux.getNext();
            }
        }
        return matriz;
    }

    public LinkedList<E> toList(E[] matriz) {
        clear();
        for (int i = 0; i < matriz.length; i++) {
            this.add(matriz[i]);
        }
        return this;
    }

    protected E deleteFirst() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        } else {
            E element = head.getData();
            Node<E> aux = head.getNext();
            head = aux;
            if (length.intValue() == 1)
                last = null;
            length--;
            return element;
        }
    }

    protected E deleteLast() throws Exception {
        if (isEmpty()) {
            throw new Exception("List empty");
        } else {
            E element = last.getData();
            Node<E> aux = getNode(length - 2);
            if (aux == null) {
                last = null;
                if (length == 2) {
                    last = head;
                } else {
                    head = null;
                }
            } else {
                last = null;
                last = aux;
                last.setNext(null);
            }
            length--;
            return element;
        }
    }

    public E delete(Integer pos) throws Exception {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");

        } else if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return deleteFirst();
        } else if ((length.intValue() - 1) == pos.intValue()) {
            return deleteLast();
        } else {
            Node<E> preview = getNode(pos - 1);
            Node<E> actualy = getNode(pos);
            E element = preview.getData();
            Node<E> next = actualy.getNext();
            actualy = null;
            preview.setNext(next);
            length--;
            return element;
        }
    }
    public static void main(String[] args) {
        // StackImplementation<Integer> si = new StackImplementation<>(5);
        Stack<Integer> stack = new Stack<>(5);

    }

    /**
     * Convierte la lista enlazada a una List de Java
     */
    public List<E> toJavaList() {
        List<E> list = new ArrayList<>();
        Node<E> current = head;
        while (current != null) {
            list.add(current.getData());
            current = current.getNext();
        }
        return list;
    }

    /**
     * Encuentra un elemento que cumpla con el predicado dado
     */
    public E find(Function<E, Boolean> predicate) {
        Node<E> current = head;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                return current.getData();
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Implementación del método iterator() para poder usar forEach
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                E data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    /**
     * Método para convertir todos los elementos usando una función
     */
    public <R> List<R> map(Function<E, R> mapper) {
        List<R> result = new ArrayList<>();
        for (E item : this) {
            result.add(mapper.apply(item));
        }
        return result;
    }

    /**
     * Método para filtrar elementos
     */
    public LinkedList<E> filter(Function<E, Boolean> predicate) {
        LinkedList<E> result = new LinkedList<>();
        for (E item : this) {
            if (predicate.apply(item)) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Método para verificar si la lista contiene un elemento que cumpla con el predicado
     */
    public boolean contains(Function<E, Boolean> predicate) {
        return find(predicate) != null;
    }

    /**
     * Método para actualizar un elemento que cumpla con el predicado
     */
    public boolean updateIf(Function<E, Boolean> predicate, E newData) {
        Node<E> current = head;
        int pos = 0;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                update(newData, pos);
                return true;
            }
            current = current.getNext();
            pos++;
        }
        return false;
    }

    /**
     * Método para eliminar un elemento que cumpla con el predicado
     */
    public boolean removeIf(Function<E, Boolean> predicate) {
        Node<E> current = head;
        int pos = 0;
        while (current != null) {
            if (predicate.apply(current.getData())) {
                try {
                    delete(pos);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
            current = current.getNext();
            pos++;
        }
        return false;
    }


    /**
     * Método para obtener el tamaño de la lista
     */
    public int size() {
        return length;
    }

    /**
     * Método para convertir la lista a una colección
     */
    public Collection<E> toCollection() {
        return toJavaList();
    }

    /**
     * Método para buscar por ID (útil para entidades)
     */
    public E findById(Integer id) {
        return find(item -> {
            try {
                // Asumimos que el objeto tiene un método getId()
                return ((Object)item).getClass().getMethod("getId").invoke(item).equals(id);
            } catch (Exception e) {
                return false;
            }
        });
    }
}