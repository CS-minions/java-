package tests;

import main.Complex;
import org.junit.Test;
import static org.junit.Assert.*;

public class ComplexTest {

    @Test
    public void testComplexSub() {
        Complex c1 = new Complex(3, 5);
        Complex c2 = new Complex(2, 7);
        Complex result = c1.complexSub(c2);
        assertEquals(1, result.getRealPart(), 0.001);
        assertEquals(-2, result.getImagePart(), 0.001);
    }

    @Test
    public void testComplexMult() {
        Complex c1 = new Complex(3, 5);
        Complex c2 = new Complex(2, 7);
        Complex result = c1.complexMult(c2);
        assertEquals(-29, result.getRealPart(), 0.001);
        assertEquals(31, result.getImagePart(), 0.001);
    }

    @Test
    public void testToString() {
        Complex c = new Complex(3, 5);
        assertEquals("3.0+5.0i", c.toString());
    }
}