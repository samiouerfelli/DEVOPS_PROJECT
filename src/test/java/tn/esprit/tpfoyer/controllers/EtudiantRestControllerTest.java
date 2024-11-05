package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.EtudiantRestController;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantRestControllerTest {

    @Mock
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Doe");
        etudiant.setPrenomEtudiant("John");
        etudiant.setCinEtudiant(12345678L);
    }

    @Test
    void testGetEtudiants() {
        when(etudiantService.retrieveAllEtudiants()).thenReturn(Arrays.asList(etudiant));

        List<Etudiant> response = etudiantRestController.getEtudiants();

        assertEquals(1, response.size());
        assertEquals("Doe", response.get(0).getNomEtudiant());
        verify(etudiantService, times(1)).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiantParCin() {
        when(etudiantService.recupererEtudiantParCin(12345678L)).thenReturn(etudiant);

        Etudiant found = etudiantRestController.retrieveEtudiantParCin(12345678L);

        assertNotNull(found);
        assertEquals("Doe", found.getNomEtudiant());
        verify(etudiantService, times(1)).recupererEtudiantParCin(12345678L);
    }

    @Test
    void testRetrieveEtudiant() {
        when(etudiantService.retrieveEtudiant(1L)).thenReturn(etudiant);

        Etudiant found = etudiantRestController.retrieveEtudiant(1L);

        assertNotNull(found);
        assertEquals("Doe", found.getNomEtudiant());
        verify(etudiantService, times(1)).retrieveEtudiant(1L);
    }

    @Test
    void testAddEtudiant() {
        when(etudiantService.addEtudiant(etudiant)).thenReturn(etudiant);

        Etudiant created = etudiantRestController.addEtudiant(etudiant);

        assertNotNull(created);
        assertEquals("Doe", created.getNomEtudiant());
        verify(etudiantService, times(1)).addEtudiant(etudiant);
    }

    @Test
    void testRemoveEtudiant() {
        doNothing().when(etudiantService).removeEtudiant(1L);

        etudiantRestController.removeEtudiant(1L);

        verify(etudiantService, times(1)).removeEtudiant(1L);
    }

    @Test
    void testModifyEtudiant() {
        when(etudiantService.modifyEtudiant(etudiant)).thenReturn(etudiant);

        Etudiant modified = etudiantRestController.modifyEtudiant(etudiant);

        assertNotNull(modified);
        assertEquals("Doe", modified.getNomEtudiant());
        verify(etudiantService, times(1)).modifyEtudiant(etudiant);
    }
}