package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;

class TypeChambreTest {

    @Test
    void testEnumValues() {
        // Assert that all enum values are correct
        TypeChambre[] expectedValues = {TypeChambre.SIMPLE, TypeChambre.DOUBLE, TypeChambre.TRIPLE};
        assertArrayEquals(expectedValues, TypeChambre.values());
    }

    @Test
    void testEnumNames() {
        // Assert that the names of the enum constants are correct
        assertEquals("SIMPLE", TypeChambre.SIMPLE.name());
        assertEquals("DOUBLE", TypeChambre.DOUBLE.name());
        assertEquals("TRIPLE", TypeChambre.TRIPLE.name());
    }

    @Test
    void testEnumOrdinal() {
        // Assert that the ordinal values of the enum constants are correct
        assertEquals(0, TypeChambre.SIMPLE.ordinal());
        assertEquals(1, TypeChambre.DOUBLE.ordinal());
        assertEquals(2, TypeChambre.TRIPLE.ordinal());
    }

    @Test
    void testValueOf() {
        // Assert that we can retrieve the enum constants using valueOf
        assertEquals(TypeChambre.SIMPLE, TypeChambre.valueOf("SIMPLE"));
        assertEquals(TypeChambre.DOUBLE, TypeChambre.valueOf("DOUBLE"));
        assertEquals(TypeChambre.TRIPLE, TypeChambre.valueOf("TRIPLE"));
    }

    @Test
    void testInvalidValueOf() {
        // Assert that an IllegalArgumentException is thrown for an invalid name
        assertThrows(IllegalArgumentException.class, () -> {
            TypeChambre.valueOf("INVALID");
        });
    }
}
