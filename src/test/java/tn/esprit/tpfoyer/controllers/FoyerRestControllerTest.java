package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.FoyerRestController;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.service.IFoyerService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class FoyerRestControllerTest {

    @InjectMocks
    private FoyerRestController foyerRestController;

    @Mock
    private IFoyerService foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for retrieve-all-foyers endpoint
    @Test
    void testGetFoyers_withFoyers() {
        List<Foyer> mockFoyers = Arrays.asList(new Foyer(), new Foyer());
        when(foyerService.retrieveAllFoyers()).thenReturn(mockFoyers);

        List<Foyer> foyers = foyerRestController.getFoyers();

        assertEquals(2, foyers.size());
        verify(foyerService, times(1)).retrieveAllFoyers();
    }

    @Test
    void testGetFoyers_noFoyers() {
        when(foyerService.retrieveAllFoyers()).thenReturn(Collections.emptyList());

        List<Foyer> foyers = foyerRestController.getFoyers();

        assertEquals(0, foyers.size());
        verify(foyerService, times(1)).retrieveAllFoyers();
    }

    // Tests for retrieve-foyer/{foyer-id} endpoint
    @Test
    void testRetrieveFoyer_existingId() {
        Long foyerId = 8L;
        Foyer mockFoyer = new Foyer();
        when(foyerService.retrieveFoyer(foyerId)).thenReturn(mockFoyer);

        Foyer foyer = foyerRestController.retrieveFoyer(foyerId);

        assertEquals(mockFoyer, foyer);
        verify(foyerService, times(1)).retrieveFoyer(foyerId);
    }

    @Test
    void testRetrieveFoyer_nonExistentId() {
        Long foyerId = 999L;
        when(foyerService.retrieveFoyer(foyerId)).thenReturn(null);

        Foyer foyer = foyerRestController.retrieveFoyer(foyerId);

        assertNull(foyer);
        verify(foyerService, times(1)).retrieveFoyer(foyerId);
    }

    // Tests for add-foyer endpoint
    @Test
    void testAddFoyer_successfulAddition() {
        Foyer newFoyer = new Foyer();
        Foyer savedFoyer = new Foyer();
        when(foyerService.addFoyer(newFoyer)).thenReturn(savedFoyer);

        Foyer foyer = foyerRestController.addFoyer(newFoyer);

        assertEquals(savedFoyer, foyer);
        verify(foyerService, times(1)).addFoyer(newFoyer);
    }

    @Test
    void testAddFoyer_withNullFoyer() {
        when(foyerService.addFoyer(null)).thenReturn(null);

        Foyer foyer = foyerRestController.addFoyer(null);

        assertNull(foyer);
        verify(foyerService, times(1)).addFoyer(null);
    }

    // Tests for remove-foyer/{foyer-id} endpoint
    @Test
    void testRemoveFoyer_existingId() {
        Long foyerId = 8L;

        foyerRestController.removeFoyer(foyerId);

        verify(foyerService, times(1)).removeFoyer(foyerId);
    }

    @Test
    void testRemoveFoyer_nonExistentId() {
        Long foyerId = 999L;

        // Assume service layer has exception handling or returns no response
        doNothing().when(foyerService).removeFoyer(foyerId);

        foyerRestController.removeFoyer(foyerId);

        verify(foyerService, times(1)).removeFoyer(foyerId);
    }

    // Tests for modify-foyer endpoint
    @Test
    void testModifyFoyer_successfulModification() {
        Foyer modifiedFoyer = new Foyer();
        Foyer updatedFoyer = new Foyer();
        when(foyerService.modifyFoyer(modifiedFoyer)).thenReturn(updatedFoyer);

        Foyer foyer = foyerRestController.modifyFoyer(modifiedFoyer);

        assertEquals(updatedFoyer, foyer);
        verify(foyerService, times(1)).modifyFoyer(modifiedFoyer);
    }

    @Test
    void testModifyFoyer_nonExistentFoyer() {
        Foyer modifiedFoyer = new Foyer();
        when(foyerService.modifyFoyer(modifiedFoyer)).thenReturn(null);

        Foyer foyer = foyerRestController.modifyFoyer(modifiedFoyer);

        assertNull(foyer);
        verify(foyerService, times(1)).modifyFoyer(modifiedFoyer);
    }
}
