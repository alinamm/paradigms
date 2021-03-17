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

public abstract class AbstractQueue implements Queue {
    protected int size;

    //Pred: element != null
    //Post: n = (n' + 1) && a[n] = element && forall i = 1..n': a[i] = a'[i]
    public void enqueue(Object element) {
        assert element != null;
        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(final Object e);

    //Pred: n > 0
    //Post: n = (n' - 1) && forall i = 2..n': a[i] == a'[i] && R == a'[1]
    public Object dequeue() {
        assert size > 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    //Pred: n > 0
    //Post: R == a[1] && Immutable
    public Object element() {
        assert size > 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    //Pred: true
    //Post: n == 0
    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();

    //Pred: true
    //Post: R = n && Immutable
    public int size() {
        return size;
    }

    //Pred: true
    //Post: R = n == 0 && Immutable
    public boolean isEmpty() {
        return size == 0;
    }

    //Pred: i >= 1 && i <= n
    //Post: R == a[i] && Immutable
    public Object get(int i) {
        Object[] a = new Object[size()];
        Object res = 0;
        int s = size();
        for (int j = 0; j < s; j++) {
            if (j == i) {
                res = element();
            }
            a[j] = dequeue();
        }
        for (int j = 0; j < s; j++) {
            enqueue(a[j]);
        }
        return res;
    }

    //Pred: e != null && i >= 1 && i <= n
    //Post: a[i] == e && n == n' && for all j = 1..n, j != i: a[j] == a'[j]
    public void set(int i, Object e) {
        Object[] a = new Object[size()];
        int s = size();
        for (int j = 0; j < s; j++) {
            a[j] = dequeue();
        }
        for (int j = 0; j < s; j++) {
            if (j == i) {
                enqueue(e);
            } else {
                enqueue(a[j]);
            }
        }
    }

    //Pred: true
    //Post: R == [a1, a2, ...an] && Immutable
    public Object[] toArray() {
        Object[] a = new Object[size()];
        int s = size();
        for (int j = 0; j < s; j++) {
            a[j] = dequeue();
        }
        for (int j = 0; j < s; j++) {
            enqueue(a[j]);
        }
        return a;
    }

}
