package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.UniversiteRestController;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.service.IUniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UniversiteRestControllerTest {

    @Mock
    private IUniversiteService universiteService;

    @InjectMocks
    private UniversiteRestController universiteRestController;

    private Universite universite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = new Universite(1, "Test University", "123 Test St", null);
    }

    @Test
    public void testGetUniversites() {
        // Arrange
        when(universiteService.retrieveAllUniversites()).thenReturn(Arrays.asList(universite));

        // Act
        List<Universite> result = universiteRestController.getUniversites();

        // Assert
        assertEquals(1, result.size());
        assertEquals(universite, result.get(0));
        verify(universiteService, times(1)).retrieveAllUniversites();
    }

    @Test
    public void testRetrieveUniversite() {
        // Arrange
        when(universiteService.retrieveUniversite(1L)).thenReturn(universite);

        // Act
        Universite result = universiteRestController.retrieveUniversite(1L);

        // Assert
        assertEquals(universite, result);
        verify(universiteService, times(1)).retrieveUniversite(1L);
    }

    @Test
    public void testAddUniversite() {
        // Arrange
        when(universiteService.addUniversite(universite)).thenReturn(universite);

        // Act
        Universite result = universiteRestController.addUniversite(universite);

        // Assert
        assertEquals(universite, result);
        verify(universiteService, times(1)).addUniversite(universite);
    }

    @Test
    public void testRemoveUniversite() {
        // Act
        universiteRestController.removeUniversite(1L);

        // Assert
        verify(universiteService, times(1)).removeUniversite(1L);
    }

    @Test
    public void testModifyUniversite() {
        // Arrange
        when(universiteService.modifyUniversite(universite)).thenReturn(universite);

        // Act
        Universite result = universiteRestController.modifyUniversite(universite);

        // Assert
        assertEquals(universite, result);
        verify(universiteService, times(1)).modifyUniversite(universite);
    }
}
