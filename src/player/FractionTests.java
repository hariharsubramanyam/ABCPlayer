package player;

import static org.junit.Assert.*;

import org.junit.Test;

public class FractionTests {

    @Test
    public void GCDTests() {
        // test when b > a
        assertEquals(6, Fraction.GCD(24, 42));
        // test when a > b
        assertEquals(6, Fraction.GCD(42, 24));
        // test when b is a really big number
        assertEquals(1, Fraction.GCD(24, Integer.MAX_VALUE));
        // test when either a or b is zero
        assertEquals(1, Fraction.GCD(1, 0));
        // test when both a and b are zero
        assertEquals(0, Fraction.GCD(0, 0));
    }
    
    @Test
    public void compareToTest(){
        Fraction a = new Fraction(1,2);
        Fraction b = new Fraction(1,3);
        Fraction c = new Fraction(2,4);
        assertTrue(a.compareTo(c) == 0);
        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
    }
    
    @Test
    public void LCMTests() {
        // test when b > a
        assertEquals(168, Fraction.LCM(24, 42));
        // test when a > b
        assertEquals(168, Fraction.LCM(42, 24));
        // test when either a or b is zero
        assertEquals(0, Fraction.LCM(1, 0));
        // test when both a and b are zero
        assertEquals(0, Fraction.LCM(0, 0));
    }

}