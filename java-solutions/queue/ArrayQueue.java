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

public class ArrayQueue {
    private int size;
    private Object[] elements = new Object[2];
    private int head = 0;
    private int tail = 0;


    //Pred: element != null
    //Post: n = (n' + 1) && a[n] = element && forall i = 1..n': a[i] = a'[i]
    public void enqueue(Object element) {
        assert element != null;
        this.ensureCapacity(size + 1);

        elements[tail % elements.length] = element;
        tail++;
        size++;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > this.elements.length) {
            int l = elements.length;
            Object[] a = new Object[2 * l];
            System.arraycopy(elements, head % l, a, 0, l - head % l);
            System.arraycopy(elements, 0, a, l - head % l, tail % l);
            elements = a;
            head = 0;
            tail = head + size;
        }
    }

    //Pred: n > 0
    //Post: n = (n' - 1) && forall i = 2..n': a[i] == a'[i] && R == a'[1]
    public Object dequeue() {
        assert size > 0;

        Object value;

        value = elements[head % elements.length];
        elements[head % elements.length] = 0;

        head++;
        size--;

        return value;
    }

    //Pred: n > 0
    //Post: R == a[1] && Immutable
    public Object element() {
        assert size > 0;

        return elements[head % elements.length];
    }

    //Pred: true
    //Post: R == n && Immutable
    public int size() {
        return size;
    }

    //Pred: true
    //Post: R == n > 0 && Immutable
    public boolean isEmpty() {
        return head == tail;
    }

    //Pred: true
    //Post: n == 0
    public void clear() {
        elements = new Object[2];
        head = tail;
        size = 0;
    }

    //Pred: i >= 1 && i <= n
    //Post: R == a[i] && Immutable
    public Object get(int i) {
        return elements[(head + i) % elements.length];
    }

    //Pred: e != null && i >= 1 && i <= n
    //Post: Immutable && a[i] == e && n == n' && for all j = 1..n, j != i: a[j] == a'[j]
    public void set(int i, Object e) {
        elements[(head + i) % elements.length] = e;
    }

}
