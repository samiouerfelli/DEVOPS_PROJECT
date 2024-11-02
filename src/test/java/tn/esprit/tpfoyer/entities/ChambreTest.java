package tn.esprit.tpfoyer.entities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.entity.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

public class ChambreTest {

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        chambre = new Chambre();
    }

    @Test
    void testDefaultConstructor() {
        // Assert that default constructor initializes fields to null or default values
        assertEquals(0, chambre.getIdChambre());
        assertEquals(0, chambre.getNumeroChambre());
        assertNull(chambre.getTypeC());
        assertNull(chambre.getBloc());
        assertNotNull(chambre.getReservations());
        assertTrue(chambre.getReservations().isEmpty());
    }

    @Test
    void testPropertyAssignments() {
        // Arrange
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        // Create a Bloc instance to associate with Chambre
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        chambre.setBloc(bloc);

        // Act & Assert
        assertEquals(1L, chambre.getIdChambre());
        assertEquals(101, chambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, chambre.getTypeC());
        assertEquals(bloc, chambre.getBloc());
    }

    @Test
    void testReservationsAssociation() {
        // Arrange
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        Set<Reservation> reservations = new HashSet<>();
        reservations.add(reservation1);
        reservations.add(reservation2);

        // Act
        chambre.setReservations(reservations);

        // Assert
        assertEquals(2, chambre.getReservations().size());
        assertTrue(chambre.getReservations().contains(reservation1));
        assertTrue(chambre.getReservations().contains(reservation2));
    }

    @Test
    void testToString() {
        // Arrange
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.DOUBLE);

        // Act
        String toStringResult = chambre.toString();

        // Assert
        assertTrue(toStringResult.contains("idChambre=1"));
        assertTrue(toStringResult.contains("numeroChambre=101"));
        assertTrue(toStringResult.contains("typeC=DOUBLE"));
    }

    @Test
    void testTypeChambreEnumValues() {
        // Check that enum values are as expected
        assertEquals(TypeChambre.SIMPLE, TypeChambre.valueOf("SIMPLE"));
        assertEquals(TypeChambre.DOUBLE, TypeChambre.valueOf("DOUBLE"));
        assertEquals(TypeChambre.TRIPLE, TypeChambre.valueOf("TRIPLE"));
    }
}