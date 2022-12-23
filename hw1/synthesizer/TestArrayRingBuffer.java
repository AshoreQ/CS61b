package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        arb.enqueue(1);
        arb.enqueue(3);
        arb.enqueue(2);
        arb.enqueue(5);
        assertEquals(1, (int) arb.dequeue());
        assertEquals(3, (int) arb.dequeue());
        assertEquals(2, (int) arb.dequeue());
        assertEquals(5, (int) arb.dequeue());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
