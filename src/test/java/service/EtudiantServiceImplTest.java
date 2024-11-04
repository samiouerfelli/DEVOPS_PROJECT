// EtudiantServiceImplTest.java

package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);
        assertNotNull(result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testRetrieveAllEtudiants() {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllEtudiantsEmpty() {
        when(etudiantRepository.findAll()).thenReturn(Collections.emptyList());

        List<Etudiant> result = etudiantService.retrieveAllEtudiants();
        assertTrue(result.isEmpty());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveEtudiant() {
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.retrieveEtudiant(1L);
        assertNotNull(result);
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveEtudiantNotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        Etudiant result = etudiantService.retrieveEtudiant(1L);
        assertNull(result);
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);  // Set an ID to ensure it simulates an existing Etudiant in the database

        // Mock findById to return the Etudiant we are updating
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.modifyEtudiant(etudiant);

        assertNotNull(result);
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(1)).save(etudiant);
    }


    @Test
    void testDeleteEtudiant() {
        Long id = 1L;
        doNothing().when(etudiantRepository).deleteById(id);

        etudiantService.removeEtudiant(id);
        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteEtudiantNotFound() {
        Long id = 1L;
        doThrow(new IllegalArgumentException("Etudiant not found")).when(etudiantRepository).deleteById(id);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            etudiantService.removeEtudiant(id);
        });

        assertEquals("Etudiant not found", exception.getMessage());
        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void testFindEtudiantByCin() {
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant("12345678");

        when(etudiantRepository.findEtudiantByCinEtudiant("12345678")).thenReturn(etudiant);

        Etudiant result = etudiantService.recupererEtudiantParCin("12345678");
        assertNotNull(result);
        assertEquals("12345678", result.getCinEtudiant());
        verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant("12345678");
    }

    @Test
    void testFindEtudiantByCinNotFound() {
        when(etudiantRepository.findEtudiantByCinEtudiant("12345678")).thenReturn(null);

        Etudiant result = etudiantService.recupererEtudiantParCin("12345678");
        assertNull(result);
        verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant("12345678");
    }
}
