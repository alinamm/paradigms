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

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[2];
    private int head = 0;
    private int tail = 0;


    protected void enqueueImpl(final Object element) {
        this.ensureCapacity(size + 1);

        elements[tail % elements.length] = element;
        tail++;
    }

    private void ensureCapacity(int capacity) {
        if (capacity > this.elements.length) {
            elements = toArray();
            this.elements = Arrays.copyOf(this.elements, 2 * capacity);
            head = 0;
            tail = head + size;
        }
    }

    protected Object dequeueImpl() {
        Object value;
        value = elements[head % elements.length];
        elements[head % elements.length] = 0;
        head++;

        return value;
    }

    public Object elementImpl() {
        return elements[head % elements.length];
    }

    public void clearImpl() {
        elements = new Object[2];
        head = tail = 0;
    }

    //Pred: e != null && i >= 0 && i < n
    //Post: a[i+ 1] == e && n == n' && for all j = 1..n, j != i + 1: a[j] == a'[j]
    @Override
    public void set(int i, Object e) {
        elements[(head + i) % elements.length] = e;
    }

    @Override
    public Object get(int i) {
        return elements[(head + i) % elements.length];
    }

    //Pred: true
    //Post: R == [a1, a2, ...an] && Immutable
    @Override
    public Object[] toArray() {
        Object[] a = new Object[size()];
        if (size() == 0) {
            return a;
        }
        for (int i = 0; i < size(); i++) {
            a[i] = get(i);
        }
        return a;
    }

}

