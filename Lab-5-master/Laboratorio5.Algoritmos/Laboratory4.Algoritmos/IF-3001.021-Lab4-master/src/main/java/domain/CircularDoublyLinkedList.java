package domain;

import util.Utility;

import java.io.FileReader;

import static java.lang.StringTemplate.STR;

public class CircularDoublyLinkedList implements List {
    private Node first; //apuntador al inicio de la lista
    private Node last; //apuntador al final de la lista

    public CircularDoublyLinkedList() {
        this.first = null; //la lista no existe
        this.last = null; //la lista no existe
    }


    @Override
    public int size() throws ListException {
        int count = 0;
        Node current = first;

        while (current != last) {//mientas que no llegue al último, por ser circular
            count++;
            current = current.next;
        }

        return count + 1;//Para que cuente el último nodo +1
    }


    @Override
    public void clear() {
        this.first = null; //anulamos la lista
        this.last = null; //anulamos la lista
    }

    @Override
    public boolean isEmpty() {
        return this.first == null; //si es nulo está vacía
    }

    @Override
    public boolean contains(Object element) throws ListException {
        Node current = first;

        while (current != last) {
            if (Utility.compare(current.data, element) == 0) {
                return true; // Elemento encontrado en la lista
            }
            current = current.next;
        }

        //Se sale del while, cuando aux==last entonces solo queda verificar si el elemento a buscar está en el último nodo
        return Utility.compare(current.data, element) == 0;//elemento encontrado?
    }


    @Override
    public void add(Object element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            first = last = newNode; //un solo nodo, ambos apuntan al mismo nodo
        } else {
            last.next = newNode;
            //ponemos last a apuntar al ultimo nodo
            last = newNode;
        }

        //Se hace el enlace circular
        last.next = first;
        //hago el enlace doble
        first.prev = last;
    }

    @Override
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if (isEmpty())
            first = last = newNode;
        else {
            newNode.next = first;
            //Hago el enlace doble
            first.prev = newNode;
            first = newNode;
        }
        //Garantizo el enlace circular y doble
        last.next = first;
        first.prev = last;

    }


    @Override
    public void addLast(Object element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            first = last = newNode; // Si la lista está vacía, el nuevo nodo se convierte en el primer y último nodo
        } else {
            last.next = newNode;
            newNode.prev = last; // Establecemos el enlace doble
            last = newNode; // Actualizamos last para que apunte al nuevo último nodo
        }
        // Mantenemos el enlace circular
        last.next = first;
        first.prev = last;
    }


    @Override
    public void addInSortedList(Object element) {
        Node newNode = new Node(element);
        if (isEmpty() || Utility.compare(first.data, element) > 0) {
            // Si la lista está vacía o el elemento es menor que el primer nodo,
            // el nuevo nodo se convierte en el primer nodo
            newNode.next = first;
            first = newNode;
        } else {
            Node current = first;
            Node previous = null;

            // Avanzamos hasta encontrar el lugar correcto para insertar el nuevo nodo
            while (current != null && Utility.compare(current.data, element) <= 0) {
                previous = current;
                current = current.next;
            }

            // Insertamos el nuevo nodo entre previous y current
            previous.next = newNode;
            newNode.next = current;
        }
    }


    @Override
    public void remove(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }

        // CASO 1: el elemento a suprimir está al inicio
        if (Utility.compare(first.data, element) == 0) {
            first = first.next; // saltamos al siguiente nodo
        } else {
            // caso 2: suprimir un elemento que no está al inicio
            Node prev = first; // dejamos un apuntador al nodo anterior
            Node aux = first.next;
            while (aux != last && !(Utility.compare(aux.data, element) == 0)) {
                prev = aux;
                aux = aux.next;
            }
            // si se sale del bucle y encuentra el elemento
            if (Utility.compare(aux.data, element) == 0) {
                // desenlaza el nodo
                prev.next = aux.next;
                // mantiene el doble enlace
                aux.next.prev = prev;
            }

            // caso 3: si el elemento a suprimir está en el último nodo
            if (aux == last && Utility.compare(aux.data, element) == 0) {
                // desenlaza el último nodo
                prev.next = first; // ahora el anterior al último apunta al primero
                last = prev; // actualiza el último nodo
            }
        }

        // mantiene el enlace circular y doble
        last.next = first;
        first.prev = last;

        // otro caso: si solo queda un nodo y es el que queremos eliminar
        if (first == last && Utility.compare(first.data, element) == 0) {
            clear(); // anula la lista
        }
    }


    @Override
    public Object removeFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Object removedData = first.data; //Guardamos el valor del primer nodo que se va a eliminar
        first = first.next; //Actualizamos el puntero first para que apunte al siguiente nodo
        //Si solo queda un nodo
        if (first == last) {
            clear(); //anulo la lista
        } else {
            //Mantengo el enlace circular y doble
            last.next = first;
            first.prev = last;
        }
        return removedData;
    }

    @Override
    public Object removeLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Object removedData = last.data; // Guardamos el valor del último nodo que se va a eliminar
        if (first == last) {
            // Si solo hay un nodo en la lista, eliminamos ese nodo
            first = last = null;
        } else {
            last = last.prev; // Actualizamos last para que apunte al nodo anterior
            last.next = first; // Mantenemos el enlace circular
        }
        return removedData; // Devolvemos el valor del último nodo eliminado
    }

    @Override
    public void sort() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }

        boolean swapped;
        do {
            swapped = false;
            Node current = first;
            Node next = first.next;
            Node previous = null;

            while (next != null) {
                if (Utility.compare(current.data, next.data) > 0) {
                    if (previous != null) {
                        previous.next = next;
                    } else {
                        first = next;
                    }
                    current.next = next.next;
                    next.next = current;

                    previous = next;
                    next = current.next;
                    swapped = true;
                } else {
                    previous = current;
                    current = next;
                    next = next.next;
                }
            }
        } while (swapped);
    }


    @Override
    public int indexOf(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }
        Node aux = first;
        int index = 1; //la lista inicia en 1
        while (aux != last) {
            if (Utility.compare(aux.data, element) == 0) {
                return index;
            }
            index++; //incremento el indice
            aux = aux.next; //muevo aux al sgte nodo
        }

        //Se sale cuando alcanza last, retorne el indice
        if (Utility.compare(aux.data, element) == 0) {
            return index;
        }

        return -1; //indica q el elemento no existe
    }

    @Override
    public Object getFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }
        return first.data;
    }

    @Override
    public Object getLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }
        return last.data;
    }

    @Override
    public Object getPrev(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Doubly Linked List is Empty");
        }

        Node aux = last;
        do {
            if (Utility.compare(aux.data, element) == 0) {
                // Se encontró el elemento
                return aux.prev != last ? aux.prev.data : "It's the first, it has no previous";
            }
            aux = aux.prev;
        } while (aux != last);

        // Si el elemento no se encuentra en la lista
        throw new ListException("Element not found in Circular Doubly Linked List");
    }

    @Override
    public Object getNext(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Doubly Linked List is Empty");
        }

        Node aux = first;
        do {
            if (Utility.compare(aux.data, element) == 0) {
                // Se encontró el elemento
                return aux.next != first ? aux.next.data : "It's the last, it has no next";
            }
            aux = aux.next;
        } while (aux != first);

        // Si el elemento no se encuentra en la lista
        throw new ListException("Element not found in Circular Doubly Linked List");
    }






    @Override
    public Node getNode(int index) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }

        Node aux = first;
        int i = 1; //pos del primer nodo
        while (aux != last) {
            if (Utility.compare(i, index) == 0) { //Ya encontró el indice
                return aux;
            }
            i++; //incrementos el indice
            aux = aux.next;// muevo el aux al siguiente nodo
        }

        //se sale caundo aux==last
        if (Utility.compare(i, index) == 0) { //Ya encontró el indice
            return aux;
        }
        return null;
    }

    @Override
    public String toString() {
        String result = "Circular Linked List Content\n\n";
        Node aux = first;
        while (aux != last) {
            result += aux.data + "\n"; // Concatenamos el valor del nodo actual
            aux = aux.next; // Movemos el auxiliar al siguiente nodo
        }
        return result + aux.data + "\n"; // Agregamos la data del último nodo
    }
}

/////////////////////////////////////////
//falta el remove last y first, getnext
////////////////////////////////////////