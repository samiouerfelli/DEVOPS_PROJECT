package tn.esprit.tpfoyer.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
@ExtendWith(MockitoExtension.class)
class ChambreTest {

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        // Initialize a new Chambre object before each test
        chambre = new Chambre();
    }

    @Test
    void testDefaultConstructor() {
        // Assert that the chambre object is not null
        assertNotNull(chambre);

        // Assert that the default values are as expected
        assertEquals(0L, chambre.getIdChambre()); // Default ID should be initialized
        assertEquals(0L, chambre.getNumeroChambre()); // Default should be zero as per default behavior
        assertNull(chambre.getTypeC()); // Type should be null by default
        assertTrue(chambre.getReservations().isEmpty()); // Reservations set should be empty
        assertNull(chambre.getBloc()); // Bloc should be null by default
    }

    @Test
    void testSettersAndGetters() {
        // Set values using setters
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE); // Assuming SIMPLE is a valid enum value
        chambre.setReservations(new HashSet<>());

        // Create a mock Bloc and set it
        Bloc mockBloc = new Bloc();
        chambre.setBloc(mockBloc);

        // Assert that the getters return the expected values
        assertEquals(101, chambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, chambre.getTypeC());
        assertNotNull(chambre.getReservations()); // Reservations should not be null
        assertTrue(chambre.getReservations().isEmpty()); // Should still be empty
        assertEquals(mockBloc, chambre.getBloc()); // Assert that the Bloc is set correctly
    }

    @Test
    void testToString() {
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        // This will assert that the toString method doesn't return null or is not empty
        assertNotNull(chambre.toString());
        assertFalse(chambre.toString().isEmpty());
    }
}