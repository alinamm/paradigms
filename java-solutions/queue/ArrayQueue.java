package queue;


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

}

