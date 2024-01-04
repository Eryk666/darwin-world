package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {
    @Test
    void testEquals(){
        Vector2d v1 = new Vector2d(2,4); //true
        Vector2d v2 = new Vector2d(-2137,-135235); //false
        int v3 = 11; //false
        Vector2d pattern = new Vector2d(2,4);

        assertTrue(v1.equals(pattern));
        assertTrue(pattern.equals(v1));
        assertFalse(v1.equals(v2));
        assertFalse(pattern.equals(v2));
        assertFalse(pattern.equals(v3));
        assertTrue(pattern.equals(pattern));
    }

    @Test
    void testToString(){
        Vector2d v1 = new Vector2d(2,4);
        Vector2d v2 = new Vector2d(-314,279);
        Vector2d v3 = new Vector2d(0,0);

        assertEquals("(2,4)",v1.toString());
        assertEquals("(-314,279)",v2.toString());
        assertEquals("(0,0)", v3.toString());
    }

    @Test
    void testPrecedes(){
        Vector2d v1 = new Vector2d(2,2);
        Vector2d v2 = new Vector2d(3,3);
        Vector2d v3 = new Vector2d(2,3);
        Vector2d v4 = new Vector2d(3,2);
        Vector2d v5 = new Vector2d(1,1);

        assertTrue(v1.precedes(v2));
        assertTrue(v1.precedes(v3));
        assertTrue(v1.precedes(v4));
        assertFalse(v1.precedes(v5));
        assertTrue(v1.precedes(v1));
    }

    @Test
    void testFollows(){
        Vector2d v1 = new Vector2d(3,3);
        Vector2d v2 = new Vector2d(2,2);
        Vector2d v3 = new Vector2d(2,3);
        Vector2d v4 = new Vector2d(3,2);
        Vector2d v5 = new Vector2d(4,4);
        Vector2d v6 = new Vector2d(3,4);

        assertTrue(v1.follows(v2));
        assertTrue(v1.follows(v3));
        assertTrue(v1.follows(v4));
        assertFalse(v1.follows(v5));
        assertFalse(v1.follows(v6));
        assertTrue(v1.follows(v1));
    }

    @Test
    void testUpperRight(){
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(3,2);
        Vector2d v3 = new Vector2d(3,3);
        Vector2d v4 = new Vector2d(-2,-2);
        Vector2d v5 = new Vector2d(-2,-1);

        assertEquals(v3, v1.upperRight(v2));
        assertEquals(v3, v2.upperRight(v1));
        assertEquals(v1, v1.upperRight(v1));
        assertEquals(v3, v1.upperRight(v3));
        assertEquals(v5,v4.upperRight(v5));
    }

    @Test
    void testLowerLeft(){
        Vector2d v1 = new Vector2d(2, 3);
        Vector2d v2 = new Vector2d(3,2);
        Vector2d v3 = new Vector2d(2,2);
        Vector2d v4 = new Vector2d(-2,-2);
        Vector2d v5 = new Vector2d(-3,-1);

        assertEquals(v3, v1.lowerLeft(v2));
        assertEquals(v3, v2.lowerLeft(v1));
        assertEquals(v1, v1.lowerLeft(v1));
        assertEquals(v3, v1.lowerLeft(v3));
        assertEquals(new Vector2d(-3,-2),v4.lowerLeft(v5));
    }

    @Test
    void testAdd(){
        Vector2d v1 = new Vector2d(2,3);
        Vector2d v2 = new Vector2d(-12,12);
        Vector2d v3 = new Vector2d(69,-69);
        Vector2d v4 = new Vector2d(0,0);

        assertEquals(new Vector2d(-10,15), v1.add(v2));
        assertEquals(new Vector2d(-10,15), v2.add(v1));
        assertEquals(new Vector2d(4,6), v1.add(v1));
        assertEquals(new Vector2d(-24,24), v2.add(v2));
        assertEquals(new Vector2d(71,-66), v3.add(v1));
        assertEquals(v4, v4.add(v4));
        assertEquals(v1, v1.add(v4));
    }

    @Test
    void testSubtract(){
        Vector2d v1 = new Vector2d(2,3);
        Vector2d v2 = new Vector2d(-12,12);
        Vector2d v3 = new Vector2d(69,-69);
        Vector2d v4 = new Vector2d(0,0);

        assertEquals(new Vector2d(14,-9), v1.subtract(v2));
        assertEquals(new Vector2d(-14,9), v2.subtract(v1));
        assertEquals(new Vector2d(81,-81),v3.subtract(v2));
        assertEquals(v4, v1.subtract(v1));
        assertEquals(v4, v2.subtract(v2));
        assertEquals(v1,v1.subtract(v4));
        assertEquals(v3,v3.subtract(v4));
    }

}
