package domain;

import util.Utility;

public class CircularLinkedList implements List {
    private Node first; // apuntador al inicio de la lista
    private Node last; // apuntador al final de la lista

    public CircularLinkedList() {
        this.first = null; // la lista no existe
        this.last = null; // la lista no existe
    }

    @Override
    public int size() throws ListException {
        if (isEmpty()) {
            return 0;
        }

        int count = 0;
        Node current = first;

        do {
            count++;
            current = current.next;
        } while (current != first);

        return count;
    }

    @Override
    public void clear() {
        this.first = null; // anulamos la lista
        this.last = null; // anulamos la lista
    }

    @Override
    public boolean isEmpty() {
        return this.first == null; // si es nulo está vacía
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

        // Se sale del while, cuando aux==last entonces solo queda verificar si el elemento a buscar está en el último nodo
        return Utility.compare(current.data, element) == 0; // elemento encontrado?
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
    }

    @Override
    public void addFirst(Object element) {
        Node newNode = new Node(element);
        if (isEmpty())
            first = last = newNode;
        else {
            newNode.next = first;
            first = newNode;
        }
        //Se hace el enlace circular
        last.next = first;
    }


    @Override
    public void addLast(Object element) {
        Node newNode = new Node(element);
        if (isEmpty()) {
            first = newNode; // Si la lista está vacía, el nuevo nodo se convierte en el primer nodo
        } else {
            Node aux = first;
            // Iteramos hasta llegar al último nodo
            while (aux.next != null) {
                aux = aux.next;
            }
            // Enlazamos el último nodo con el nuevo nodo
            aux.next = newNode;
        }
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
        //CASO 1 el elemento a suprimir está al inicio:
        if (Utility.compare(first.data, element) == 0) {
            first = first.next; //saltamos al primer nodo
        } else {  //caso 2 suprimir el ultimo
            Node prev = first; //dejo un apuntador al nodo anterior
            Node aux = first.next;
            while (aux != last && !(Utility.compare(aux.data, element) == 0)) {
                prev = aux;
                aux = aux.next;
            }
            //se sale cuando encuentra el elemento
            if (Utility.compare(aux.data, element) == 0) {
                //ya lo encontró procede a desenlazar el nodo
                prev.next = aux.next;
            }

            //Que pasas si el elemento a suprimir esta en el ultimo nodo
            if (aux == last && Utility.compare(aux.data, element) == 0) {
                last = prev; //desenlaza el ultimo nodo
            }
        }

        //Mantengo el enlace circular
        last.next = first;

        //Otro caso:
        //Si solo queda un nodo y es el que quiero eliminar
        if (first == last && util.Utility.compare(first.data, element) == 0) {
            clear();//anulo la lista
        }
    }

    @Override
    public Object removeFirst() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Object removedData = first.data; //Guarda el valor del primer nodo que se va a eliminar
        first = first.next; //Actualizamos el puntero first para que apunte al siguiente nodo
        return removedData; //Devolvemos el valor del primer nodo eliminado
    }

    @Override
    public Object removeLast() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is empty");
        }
        Object removedData = last.data; //Guardamos el valor del último nodo que se va a eliminar
        //Si solo hay un nodo en la lista
        if (first == last) {
            clear(); //anulo la lista
        } else {
            Node current = first;
            while (current.next != last) {
                current = current.next;
            }
            last = current; //desenlazo el último nodo
            //Mantengo el enlace circular
            last.next = first;
            first.prev = last;
        }
        return removedData;
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
            throw new ListException("Circular Linked List is Empty");
        }
        if (Utility.compare(first.data, element) == 0) {
            return "It's the first, it has no previous";
        }
        Node aux = first;
        //mientras no llegue al ult nodo
        while (aux.next != last) {
            if (Utility.compare(aux.next.data, element) == 0) {
                return aux.data; //retornamos la data del nodo actual
            }
            aux = aux.next;
        }

        //Se sale cuando aux.next==last
        if (Utility.compare(aux.next.data, element) == 0) {
            return aux.data;
        }

        return "Does not exist in Single Linked List";
    }


    @Override
    public Object getNext(Object element) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }

        Node aux = first;
        boolean found = false;

        // Buscamos el elemento en la lista
        do {
            if (Utility.compare(aux.data, element) == 0) {
                // Si el elemento actual no es el último, retornamos el siguiente
                if (aux.next != null) {
                    return aux.next.data;
                } else {
                    // En una lista circular, si es el último elemento, el siguiente será el primero
                    return first.data;
                }
            }
            aux = aux.next;
        } while (aux != first);

        // Si el elemento no se encuentra en la lista
        return "Does not exist in Circular Linked List";
    }


    @Override
    public Node getNode(int index) throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular Linked List is Empty");
        }

        if (index < 1 || index > size()) { // Verificar si el índice está fuera de los límites
            throw new ListException("Index out of bounds");
        }

        Node aux = first;
        int i = 1; // Posición del primer nodo
        while (aux != last) {
            if (Utility.compare(i, index) == 0) { // Ya encontró el índice
                return aux;
            }
            i++; // Incrementar el índice
            aux = aux.next; // Mover el auxiliar al siguiente nodo
        }

        // Se sale cuando aux == last
        if (Utility.compare(i, index) == 0) { // Ya encontró el índice
            return aux;
        }

        // Si el índice no coincide con ninguna posición válida, lanzar una excepción
        throw new ListException("Index not found");
    }


    @Override
    public String toString() {
        String result = "Circular Linked List Content\n\n";
        Node aux = first;
        while (aux != null) {
            result += aux.data.toString() + "\n ";
            aux = aux.next;
        }
        return result + "\n" + last.data; // agrego la data último nodo
    }

    public void sortByProductId() throws ListException {
        if (isEmpty()) {
            throw new ListException("Circular linked list is empty");
        }

        for (int i = 1; i <= size(); i++) {
            boolean swapped = false;

            for (int j = 1; j <= size() - 1; j++) {
                Node currentNode = getNode(j);
                Node nextNode = getNode(j + 1);

                Order currentOrder = (Order) currentNode.data;
                Order nextOrder = (Order) nextNode.data;

                // Comparar IDs directamente
                if (currentOrder.getProductId() > nextOrder.getProductId()) {
                    Object temp = currentNode.data;
                    currentNode.data = nextNode.data;
                    nextNode.data = temp;
                    swapped = true;
                }
            }

            // Si no se ha realizado ningún intercambio en esta iteración,
            // la lista está ordenada y podemos terminar.
            if (!swapped) {
                break;
            }
        }
    }
}