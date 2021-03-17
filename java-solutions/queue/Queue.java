package queue;

public interface Queue {
    //Pred: element != null
    //Post: n = (n' + 1) && a[n] = element && forall i = 1..n': a[i] = a'[i]
    void enqueue(Object e);

    //Pred: n > 0
    //Post: n = (n' - 1) && forall i = 2..n': a[i] == a'[i] && R == a'[1]
    Object dequeue();

    //Pred: n > 0
    //Post: R == a[1] && Immutable
    Object element();

    //Pred: true
    //Post: n == 0
    public void clear();

    //Pred: true
    //Post: R = n && Immutable
    int size();

    //Pred: true
    //Post: R = n == 0 && Immutable
    boolean isEmpty();

    //Pred: i >= 1 && i <= n
    //Post: R == a[i] && Immutable
    Object get(int i);

    //Pred: e != null && i >= 1 && i <= n
    //Post: a[i] == e && n == n' && for all j = 1..n, j != i: a[j] == a'[j]
    void set(int i, Object e);

    //Pred: true
    //Post: R == [a1, a2, ...an] && Immutable
    Object[] toArray();

}
