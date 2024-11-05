package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UniversiteTest {

    @Test
     void testUniversiteCreation() {
        // Arrange
        Foyer foyerMock = mock(Foyer.class);
        Universite universite = new Universite(1, "Test University", "123 Test St", foyerMock);

        // Act & Assert
        assertEquals(1, universite.getIdUniversite());
        assertEquals("Test University", universite.getNomUniversite());
        assertEquals("123 Test St", universite.getAdresse());
        assertEquals(foyerMock, universite.getFoyer());
    }

    @Test
     void testSettersAndGetters() {
        // Arrange
        Universite universite = new Universite();
        Foyer foyerMock = mock(Foyer.class);

        // Act
        universite.setIdUniversite(1);
        universite.setNomUniversite("Test University");
        universite.setAdresse("123 Test St");
        universite.setFoyer(foyerMock);

        // Assert
        assertEquals(1, universite.getIdUniversite());
        assertEquals("Test University", universite.getNomUniversite());
        assertEquals("123 Test St", universite.getAdresse());
        assertEquals(foyerMock, universite.getFoyer());
    }

    @Test
     void testToString() {
        // Arrange
        Foyer foyerMock = mock(Foyer.class);
        Universite universite = new Universite(1, "Test University", "123 Test St", foyerMock);

        // Act
        String result = universite.toString();

        // Assert
        assertTrue(result.contains("Test University"));
        assertTrue(result.contains("123 Test St"));
    }

    @Test
     void testNoArgsConstructor() {
        // Arrange
        Universite universite = new Universite();

        // Act & Assert
        assertNotNull(universite);
    }
}