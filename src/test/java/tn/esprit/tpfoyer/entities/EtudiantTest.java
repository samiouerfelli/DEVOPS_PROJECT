package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

class EtudiantTest {

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        etudiant = new Etudiant();
    }

    @Test
    void testDefaultConstructor() {
        // Assert that default constructor initializes fields to default values
        assertEquals(0, etudiant.getIdEtudiant());
        assertNull(etudiant.getNomEtudiant());
        assertNull(etudiant.getPrenomEtudiant());
        assertEquals(0, etudiant.getCinEtudiant());
        assertNull(etudiant.getDateNaissance());
        assertNotNull(etudiant.getReservations()); // Ensure this is not null
        assertTrue(etudiant.getReservations().isEmpty()); // Should be empty by default
    }

    @Test
    void testPropertyAssignments() {
        String expectedNom = "John";
        String expectedPrenom = "Doe";
        long expectedCin = 123456;
        Date expectedDateNaissance = new Date();

        etudiant.setNomEtudiant(expectedNom);
        etudiant.setPrenomEtudiant(expectedPrenom);
        etudiant.setCinEtudiant(expectedCin);
        etudiant.setDateNaissance(expectedDateNaissance);

        assertEquals(expectedNom, etudiant.getNomEtudiant());
        assertEquals(expectedPrenom, etudiant.getPrenomEtudiant());
        assertEquals(expectedCin, etudiant.getCinEtudiant());
        assertEquals(expectedDateNaissance, etudiant.getDateNaissance());
    }

    @Test
    void testReservationsAssociation() {
        Set<Reservation> reservations = new HashSet<>();
        Reservation reservation1 = new Reservation(); // Assume Reservation class is defined
        Reservation reservation2 = new Reservation(); // Assume Reservation class is defined

        reservations.add(reservation1);
        reservations.add(reservation2);
        etudiant.setReservations(reservations);

        assertEquals(2, etudiant.getReservations().size());
        assertTrue(etudiant.getReservations().contains(reservation1));
        assertTrue(etudiant.getReservations().contains(reservation2));
    }

    @Test
    void testToString() {
        etudiant.setNomEtudiant("John");
        etudiant.setPrenomEtudiant("Doe");

        String toStringResult = etudiant.toString();

        assertNotNull(toStringResult);
        assertTrue(toStringResult.contains("nomEtudiant=John"));
        assertTrue(toStringResult.contains("prenomEtudiant=Doe"));
    }
}
