package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FoyerTest {

    @Test
    void testFoyerConstructorAndGetters() {
        // Create a Foyer instance using the constructor
        Foyer foyer = new Foyer(1L, "Foyer A", 100, null, null);

        // Assert the properties
        assertEquals(1L, foyer.getIdFoyer());
        assertEquals("Foyer A", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
        assertNull(foyer.getUniversite());
        assertNull(foyer.getBlocs());
    }

    @Test
    void testSetters() {
        // Create a Foyer instance using the no-arg constructor
        Foyer foyer = new Foyer();

        // Set values
        foyer.setIdFoyer(2L);
        foyer.setNomFoyer("Foyer B");
        foyer.setCapaciteFoyer(200);

        // Assert the properties
        assertEquals(2L, foyer.getIdFoyer());
        assertEquals("Foyer B", foyer.getNomFoyer());
        assertEquals(200, foyer.getCapaciteFoyer());
    }

    @Test
    void testFoyerWithBlocs() {
        // Create a Foyer instance
        Foyer foyer = new Foyer();
        Set<Bloc> blocs = new HashSet<>();
        blocs.add(new Bloc()); // Assume Bloc has a no-arg constructor for this test

        // Associate the blocs with the foyer
        foyer.setBlocs(blocs);

        // Assert the association
        assertEquals(1, foyer.getBlocs().size());
        assertNotNull(foyer.getBlocs());
    }

    @Test
    void testFoyerWithUniversite() {
        // Create a Universite instance
        Universite universite = new Universite(); // Assume Universite has a no-arg constructor
        Foyer foyer = new Foyer();

        // Set the universite
        foyer.setUniversite(universite);

        // Assert the association
        assertNotNull(foyer.getUniversite());
    }

    @Test
    void testFoyerToString() {
        Foyer foyer = new Foyer(3L, "Foyer C", 150, null, null);
        String expectedString = "Foyer(idFoyer=3, nomFoyer=Foyer C, capaciteFoyer=150, universite=null, blocs=null)";

        assertEquals(expectedString, foyer.toString());
    }

    @Test
    void testFoyerEquality() {
        Foyer foyer1 = new Foyer(4L, "Foyer D", 250, null, null);
        Foyer foyer2 = new Foyer(4L, "Foyer D", 250, null, null);
        Foyer foyer3 = new Foyer(5L, "Foyer E", 300, null, null);

        // Check equality
        assertEquals(foyer1, foyer2);
        assertNotEquals(foyer1, foyer3);
    }

    @Test
    void testFoyerHashCode() {
        Foyer foyer = new Foyer(6L, "Foyer F", 350, null, null);

        // Test hashCode
        assertNotNull(foyer.hashCode());
    }
}
