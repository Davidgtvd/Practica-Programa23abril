package org.unl.music.base.controller.data_struct.list;

/**
 * Clase genérica Node que representa un nodo en una estructura de datos enlazada.
 * 
 * @param <E> El tipo de dato que almacenará el nodo
 * 
 * Convenciones de nombres genéricos:
 * K, V  -- key, value (para mapas)
 * E     -- element (para colecciones)
 * T     -- type (para tipos genéricos)
 */
public class Node<E> {
    private E data;           // Dato almacenado en el nodo
    private Node<E> next;     // Referencia al siguiente nodo

    /**
     * Obtiene el dato almacenado en el nodo
     * @return El dato almacenado
     */
    public E getData() {
        return this.data;
    }

    /**
     * Establece el dato del nodo
     * @param data El dato a almacenar
     */
    public void setData(E data) {
        this.data = data;
    }

    /**
     * Obtiene el siguiente nodo
     * @return El siguiente nodo
     */
    public Node<E> getNext() {
        return this.next;
    }

    /**
     * Establece el siguiente nodo
     * @param next El nodo a establecer como siguiente
     */
    public void setNext(Node<E> next) {
        this.next = next;
    }

    /**
     * Constructor que inicializa el nodo con dato y siguiente
     * @param data El dato a almacenar
     * @param next El siguiente nodo
     */
    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }

    /**
     * Constructor que inicializa el nodo solo con dato
     * @param data El dato a almacenar
     */
    public Node(E data) {
        this.data = data;
        this.next = null;
    }

    /**
     * Constructor por defecto
     */
    public Node() {
        this.data = null;
        this.next = null;
    }

    /**
     * Verifica si el nodo tiene un siguiente
     * @return true si tiene siguiente, false si no
     */
    public boolean hasNext() {
        return this.next != null;
    }

    /**
     * Verifica si el nodo tiene datos
     * @return true si tiene datos, false si no
     */
    public boolean hasData() {
        return this.data != null;
    }

    /**
     * Desconecta este nodo del siguiente
     */
    public void disconnect() {
        this.next = null;
    }

    /**
     * Crea una copia del nodo (sin copiar el siguiente)
     * @return Una nueva instancia de Node con el mismo dato
     */
    public Node<E> copy() {
        return new Node<>(this.data);
    }

    /**
     * Representación en String del nodo
     * @return String representando el contenido del nodo
     */
    @Override
    public String toString() {
        return "Node{" +
                "data=" + (data != null ? data.toString() : "null") +
                ", hasNext=" + (next != null) +
                '}';
    }

    /**
     * Compara este nodo con otro objeto
     * @param obj El objeto a comparar
     * @return true si son iguales, false si no
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Node<?> node = (Node<?>) obj;
        
        if (data != null ? !data.equals(node.data) : node.data != null) return false;
        return true;
    }

    /**
     * Genera un código hash para el nodo
     * @return El código hash
     */
    @Override
    public int hashCode() {
        int result = data != null ? data.hashCode() : 0;
        return result;
    }
}