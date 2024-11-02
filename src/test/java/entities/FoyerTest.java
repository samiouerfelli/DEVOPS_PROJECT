package entities;

import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FoyerTest {

    @Test
    void testFoyerCreation() {
        Foyer foyer = new Foyer(1L, "Foyer A", 100, null, null);
        assertNotNull(foyer);
        assertEquals("Foyer A", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
    }

    @Test
    void testFoyerEquality() {
        Foyer foyer1 = new Foyer(4L, "Foyer D", 250, null, null);
        Foyer foyer2 = new Foyer(4L, "Foyer D", 250, null, null);
        Foyer foyer3 = new Foyer(5L, "Foyer E", 300, null, null);

        // Check equality
        assertEquals(foyer1, foyer2);  // Should pass
        assertNotEquals(foyer1, foyer3); // Should pass
    }

    @Test
    void testFoyerToString() {
        Foyer foyer = new Foyer(3L, "Foyer C", 150, null, null);
        String expectedString = "Foyer(idFoyer=3, nomFoyer=Foyer C, capaciteFoyer=150, universite=null, blocs=null)";

        assertEquals(expectedString, foyer.toString()); // Should pass
    }

    @Test
    void testFoyerId() {
        Foyer foyer = new Foyer(6L, "Foyer F", 400, null, null);
        assertEquals(6L, foyer.getIdFoyer());
    }

    @Test
    void testFoyerNom() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer G");
        assertEquals("Foyer G", foyer.getNomFoyer());
    }

    @Test
    void testFoyerCapacite() {
        Foyer foyer = new Foyer();
        foyer.setCapaciteFoyer(300);
        assertEquals(300, foyer.getCapaciteFoyer());
    }

    @Test
    void testFoyerSetUniversite() {
        Foyer foyer = new Foyer();
        Universite universite = new Universite();
        foyer.setUniversite(universite);
        assertEquals(universite, foyer.getUniversite());
    }

    @Test
    void testFoyerSetBlocs() {
        Foyer foyer = new Foyer();
        Set<Bloc> blocs = Set.of(new Bloc());
        foyer.setBlocs(blocs);
        assertEquals(blocs, foyer.getBlocs());
    }
}