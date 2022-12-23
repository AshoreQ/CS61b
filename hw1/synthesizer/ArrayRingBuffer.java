package synthesizer;
import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
        if (!isFull()) {
            rb[last] = x;
            fillCount = fillCount + 1;
            last = (last + 1) % capacity;
        } else {
            throw new RuntimeException("Ring buffer overflow");
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (!isEmpty()) {
            T returnValue = rb[first];
            fillCount = fillCount - 1;
            first = (first + 1) % capacity;
            return returnValue;
        } else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (!isEmpty()) {
            return rb[first];
        } else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BufferIterator();
    }

    private class BufferIterator implements Iterator<T> {
        private int ptr;
        BufferIterator() {
            ptr = first;
        }

        public boolean hasNext() {
            return ((ptr + 1) % capacity) != last;
        }

        public T next() {
            T returnValue = rb[ptr];
            ptr = (ptr + 1) % capacity;
            return returnValue;
        }
    }
}
