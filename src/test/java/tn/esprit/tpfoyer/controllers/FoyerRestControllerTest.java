package tn.esprit.tpfoyer.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.control.*;
import tn.esprit.tpfoyer.entity.*;
import tn.esprit.tpfoyer.service.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FoyerRestControllerTest {

    @InjectMocks
    private FoyerRestController foyerRestController;

    @Mock
    private IFoyerService foyerService;

    private Foyer foyer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);
    }

    @Test
    void testGetFoyers() {
        // Arrange
        when(foyerService.retrieveAllFoyers()).thenReturn(Arrays.asList(foyer));

        // Act
        List<Foyer> response = foyerRestController.getFoyers();

        // Assert
        assertEquals(1, response.size());
        assertEquals("Test Foyer", response.get(0).getNomFoyer());
        verify(foyerService, times(1)).retrieveAllFoyers();
    }

    @Test
    void testRetrieveFoyer() {
        // Arrange
        when(foyerService.retrieveFoyer(1L)).thenReturn(foyer);

        // Act
        Foyer found = foyerRestController.retrieveFoyer(1L);

        // Assert
        assertNotNull(found);
        assertEquals("Test Foyer", found.getNomFoyer());
        verify(foyerService, times(1)).retrieveFoyer(1L);
    }

    @Test
    void testAddFoyer() {
        // Arrange
        when(foyerService.addFoyer(foyer)).thenReturn(foyer);

        // Act
        Foyer created = foyerRestController.addFoyer(foyer);

        // Assert
        assertNotNull(created);
        assertEquals("Test Foyer", created.getNomFoyer());
        verify(foyerService, times(1)).addFoyer(foyer);
    }

    @Test
    void testRemoveFoyer() {
        // Arrange
        Long id = 1L;
        doNothing().when(foyerService).removeFoyer(id);

        // Act
        foyerRestController.removeFoyer(id);

        // Assert
        verify(foyerService, times(1)).removeFoyer(id);
    }

    @Test
    void testModifyFoyer() {
        // Arrange
        when(foyerService.modifyFoyer(foyer)).thenReturn(foyer);

        // Act
        Foyer modified = foyerRestController.modifyFoyer(foyer);

        // Assert
        assertNotNull(modified);
        assertEquals("Test Foyer", modified.getNomFoyer());
        verify(foyerService, times(1)).modifyFoyer(foyer);
    }
}