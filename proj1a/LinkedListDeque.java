public class LinkedListDeque<T> {
    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }

        public Node() {
            item = null;
            prev = null;
            next = null;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node();
        size = 0;
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void addFirst(T item) {
        Node temp = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = temp;
        sentinel.next = temp;
        size += 1;
    }

    public void addLast(T item) {
        Node temp = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = temp;
        sentinel.prev = temp;
        size += 1;
    }

    public void printDeque() {
        Node p = sentinel;
        while (p.next != sentinel) {
            p = p.next;
            System.out.print(p.item + " ");
        }
    }

    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        T item = sentinel.next.item;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        size -= 1;
        return item;
    }

    public T removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        T item = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size -= 1;
        return item;
    }

    public T get(int index) {
        Node p = sentinel;
        int pos = index + 1;
        if (size < pos) {
            return null;
        }
        while (pos > 0) {
            p = p.next;
            pos = pos - 1;
        }
        return p.item;
    }

    private T getRecursiveHelper(Node p, int index) {
        if (p == sentinel || index == 0) {
            return p.item;
        } else {
            return getRecursiveHelper(p.next, --index);
        }
    }

    public T getRecursive(int index) {
        if (size < index + 1) {
            return null;
        }
        Node first = sentinel.next;
        return getRecursiveHelper(first, index);    
    }
}
