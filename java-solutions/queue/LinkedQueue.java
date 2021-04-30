package queue;

/*
Model:
[a1, a2, ...an]
n -- размер очереди

Inv:
n >= 0
forall i = 1..n: a[i] != null

Immutable: n == n' && forall i = 1..n: a[i] == a'[i]
 */

public class LinkedQueue extends AbstractQueue {
    private Node tail;
    private Node head;

    class Node {
        private Object value;
        Node next;

        public Node(Object value) {
            this.value = value;
            this.next = null;
        }
    }

    protected void enqueueImpl(final Object element) {
        Node node = new Node(element);

        if (this.tail == null) {
            this.head = this.tail = node;
            return;
        }
        this.tail.next = node;
        this.tail = node;
    }

    protected Object dequeueImpl() {
        Object res = head.value;
        this.head = this.head.next;
        if (this.head == null) {
            this.tail = null;
        }
        return res;
    }

    public Object elementImpl() {
        return head.value;
    }

    public void clearImpl() {
        this.tail = this.head = null;
    }

    //Pred: i >= 0 && i < n
    //Post: R == a[i + 1] && Immutable
    @Override
    public Object get(int i) {
        Node temp = this.head;
        int k = 0;
        while (i != k) {
            temp = temp.next;
            k++;
        }
        return temp.value;
    }

    //Pred: e != null && i >= 0 && i < n
    //Post: a[i+ 1] == e && n == n' && for all j = 1..n, j != i + 1: a[j] == a'[j]
    @Override
    public void set(int i, Object e) {
        Node temp = this.head;
        int k = 0;
        while (i != k) {
            temp = temp.next;
            k++;
        }
        temp.value = e;
    }

    //Pred: true
    //Post: R == [a1, a2, ...an] && Immutable
    @Override
    public Object[] toArray() {
        Object[] a = new Object[size()];
        for (int i = 0; i < size(); i++) {
            a[i] = get(i);
        }
        return a;
    }

}
