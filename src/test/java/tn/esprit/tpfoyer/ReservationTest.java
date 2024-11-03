package tn.esprit.tpfoyer;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ReservationTest {

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        // Initialize a Reservation object before each test
        reservation = new Reservation();
        reservation.setIdReservation("R001");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(false);
        reservation.setEtudiants(new HashSet<>());
    }

    @Test
    void testReservationCreation() {
        assertNotNull(reservation);
        assertEquals("R001", reservation.getIdReservation());
        assertNotNull(reservation.getAnneeUniversitaire());
        assertFalse(reservation.isEstValide());
        assertNotNull(reservation.getEtudiants());
        assertTrue(reservation.getEtudiants().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        reservation.setIdReservation("R002");
        reservation.setEstValide(true);

        assertEquals("R002", reservation.getIdReservation());
        assertTrue(reservation.isEstValide());
    }

    @Test
    void testAddEtudiant() {
        // Create a mock Etudiant instance
        Etudiant etudiantMock = Mockito.mock(Etudiant.class);
        reservation.getEtudiants().add(etudiantMock);

        assertEquals(1, reservation.getEtudiants().size());
        assertTrue(reservation.getEtudiants().contains(etudiantMock));
    }

    @Test
    void testRemoveEtudiant() {
        // Create a mock Etudiant instance
        Etudiant etudiantMock = Mockito.mock(Etudiant.class);
        reservation.getEtudiants().add(etudiantMock);
        reservation.getEtudiants().remove(etudiantMock);

        assertEquals(0, reservation.getEtudiants().size());
        assertFalse(reservation.getEtudiants().contains(etudiantMock));
    }

    @Test
    void testToString() {
        String expectedString = "Reservation(idReservation=R001, anneeUniversitaire=" + reservation.getAnneeUniversitaire() + ", estValide=false, etudiants=[])";
        assertEquals(expectedString, reservation.toString());
    }

    @Test
    void testEquality() {
        Reservation anotherReservation = new Reservation();
        anotherReservation.setIdReservation("R001");
        anotherReservation.setAnneeUniversitaire(reservation.getAnneeUniversitaire());
        anotherReservation.setEstValide(reservation.isEstValide());
        anotherReservation.setEtudiants(reservation.getEtudiants());

        assertEquals(reservation, anotherReservation);
        assertEquals(reservation.hashCode(), anotherReservation.hashCode());
    }

    @Test
    void testHashCode() {
        int expectedHashCode = reservation.getIdReservation().hashCode();
        assertEquals(expectedHashCode, reservation.hashCode());
    }

    @Test
    void testEtudiantInteraction() {
        // Create a mock Etudiant instance
        Etudiant etudiantMock = Mockito.mock(Etudiant.class);

        // Add the mocked Etudiant to the reservation
        reservation.getEtudiants().add(etudiantMock);

        // Verify that the interaction with the mock was correct
        verify(etudiantMock, never()).toString(); // Just an example, customize according to your Etudiant class
    }
}
