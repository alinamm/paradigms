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

public class ArrayQueueADT {
    private int size;
    private Object[] elements = new Object[2];
    private int head = 0;
    private int tail = 0;


    //Pred: element != null
    //Post: n = (n' + 1) && a[n] = element && forall i = 1..n': a[i] = a'[i]
    public static void enqueue(queue.ArrayQueueADT queue, Object element) {
        assert element != null;
        ensureCapacity(queue, queue.size + 1);

        queue.elements[queue.tail % queue.elements.length] = element;
        queue.tail++;
        queue.size++;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int capacity) {
        if (capacity > queue.elements.length) {
            int l = queue.elements.length;
            Object[] a = new Object[2 * l];
            System.arraycopy(queue.elements, queue.head % l, a, 0, l - queue.head % l);
            System.arraycopy(queue.elements, 0, a, l - queue.head % l, queue.tail % l);
            queue.elements = a;
            queue.head = 0;
            queue.tail = queue.head + queue.size;
        }
    }

    //Pred: n > 0
    //Post: n = (n' - 1) && forall i = 2..n': a[i] == a'[i] && R == a'[1]
    public static Object dequeue(queue.ArrayQueueADT queue) {
        assert queue.size > 0;

        Object value;

        value = queue.elements[queue.head % queue.elements.length];
        queue.elements[queue.head % queue.elements.length] = 0;

        queue.head++;
        queue.size--;

        return value;
    }

    //Pred: n > 0
    //Post: R == a[1] && Immutable
    public static Object element(queue.ArrayQueueADT queue) {
        assert queue.size > 0;

        return queue.elements[queue.head % queue.elements.length];
    }

    //Pred: true
    //Post: R == n && Immutable
    public static int size(queue.ArrayQueueADT queue) {
        return queue.size;
    }

    //Pred: true
    //Post: n == 0
    public static void clear(queue.ArrayQueueADT queue) {
        queue.elements = new Object[2];
        queue.head = queue.tail;
        queue.size = 0;
    }

    //Pred: true
    //Post: R == n > 0 && Immutable
    public static boolean isEmpty(queue.ArrayQueueADT queue) {
        return queue.head == queue.tail;
    }

    //Pred: i >= 1 && i <= n
    //Post: R == a[i] && Immutable
    public static Object get(queue.ArrayQueueADT queue, int i) {
        return queue.elements[(queue.head + i) % queue.elements.length];
    }

    //Pred: e != null && i >= 1 && i <= n
    //Post: Immutable && a[i] == e && n == n' && for all j = 1..n, j != i: a[j] == a'[j]
    public static void set(queue.ArrayQueueADT queue, int i, Object e) {
        queue.elements[(queue.head + i) % queue.elements.length] = e;
    }
}
