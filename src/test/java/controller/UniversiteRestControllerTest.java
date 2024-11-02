package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.tpfoyer.control.*;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.service.*;

import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteRestControllerTest {

    @Mock
    private IUniversiteService universiteService;

    @InjectMocks
    private UniversiteRestController universiteRestController;

    private Universite universite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Test University");
        universite.setAdresse("123 Test Address");
    }

    @Test
    void testGetUniversites() {
        when(universiteService.retrieveAllUniversites()).thenReturn(Arrays.asList(universite));

        var response = universiteRestController.getUniversites();

        assertEquals(1, response.size());
        assertEquals("Test University", response.get(0).getNomUniversite());
        verify(universiteService, times(1)).retrieveAllUniversites();
    }

    @Test
    void testRetrieveUniversite() {
        when(universiteService.retrieveUniversite(1L)).thenReturn(universite);

        Universite found = universiteRestController.retrieveUniversite(1L);

        assertNotNull(found);
        assertEquals("Test University", found.getNomUniversite());
        verify(universiteService, times(1)).retrieveUniversite(1L);
    }

    @Test
    void testAddUniversite() {
        when(universiteService.addUniversite(universite)).thenReturn(universite);

        Universite created = universiteRestController.addUniversite(universite);

        assertNotNull(created);
        assertEquals("Test University", created.getNomUniversite());
        verify(universiteService, times(1)).addUniversite(universite);
    }

    @Test
    void testRemoveUniversite() {
        doNothing().when(universiteService).removeUniversite(1L);

        universiteRestController.removeUniversite(1L);

        verify(universiteService, times(1)).removeUniversite(1L);
    }

    @Test
    void testModifyUniversite() {
        when(universiteService.modifyUniversite(universite)).thenReturn(universite);

        Universite modified = universiteRestController.modifyUniversite(universite);

        assertNotNull(modified);
        assertEquals("Test University", modified.getNomUniversite());
        verify(universiteService, times(1)).modifyUniversite(universite);
    }
}