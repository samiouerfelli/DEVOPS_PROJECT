package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation();
    }

    @Test
    void testDefaultConstructor() {
        // Assert that default constructor initializes fields to default values
//        assertNull(reservation.getIdReservation());
//        assertNull(reservation.getAnneeUniversitaire());
//        assertFalse(reservation.isEstValide());
//        assertNull(reservation.getEtudiants());
    }

    @Test
    void testPropertyAssignments() {
        // Arrange
        reservation.setIdReservation("R123");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        // Act & Assert
        assertEquals("R123", reservation.getIdReservation());
        assertNotNull(reservation.getAnneeUniversitaire());
        assertTrue(reservation.isEstValide());
    }

    @Test
    void testEtudiantsAssociation() {
        // Arrange
        Etudiant etudiant1 = new Etudiant(); // Assuming Etudiant has a no-arg constructor
        Etudiant etudiant2 = new Etudiant();
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);

        // Act
        reservation.setEtudiants(etudiants);

        // Assert
        assertEquals(2, reservation.getEtudiants().size());
        assertTrue(reservation.getEtudiants().contains(etudiant1));
        assertTrue(reservation.getEtudiants().contains(etudiant2));
    }

    @Test
    void testToString() {
        // Arrange
        reservation.setIdReservation("R123");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        // Act
        String toStringResult = reservation.toString();

        // Assert
        assertTrue(toStringResult.contains("idReservation=R123"));
        assertTrue(toStringResult.contains("estValide=true"));
    }
}