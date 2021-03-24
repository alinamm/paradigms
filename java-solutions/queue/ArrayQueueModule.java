package queue;

<<<<<<< HEAD


public class ArrayQueueModule {
    private static int size;
    private static Object[] elements = new Object[2];
    private static int head = 0;
    private static int tail = 0;

    //Pred: element != null
    //Post: n = (n' + 1) && a[n] = element && forall i = 1..n': a[i] = a'[i]
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(size + 1);

        elements[tail % elements.length] = element;
        tail++;
        size++;
    }

    private static void ensureCapacity(int capacity) {
        if (capacity > elements.length) {
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
    public static Object dequeue() {
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
    public static Object element() {
        assert size > 0;

        return elements[head % elements.length];
    }

    //Pred: true
    //Post: R == n && Immutable
    public static int size() {
        return size;
    }

    //Pred: true
    //Post: n == 0
    public static void clear() {
        elements = new Object[2];
        head = tail;
        size = 0;
    }

    //Pred: true
    //Post: R == n > 0 && Immutable
    public static boolean isEmpty() {
        return head == tail;
    }

    //Pred: i >= 1 && i <= n
    //Post: R == a[i] && Immutable
    public static Object get(int i) {
        return elements[(head + i) % elements.length];
    }

    //Pred: e != null && i >= 1 && i <= n
    //Post: Immutable && a[i] == e && n == n' && for all j = 1..n, j != i: a[j] == a'[j]
    public static void set(int i, Object e) {
        elements[(head + i) % elements.length] = e;
    }
=======
public class ArrayQueueModule {
>>>>>>> 0ad04519b817984df6dd89162e4efc2c1f40d8ee
}
