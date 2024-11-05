package tn.esprit.tpfoyer.services;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.tpfoyer.entity.Etudiant;

import tn.esprit.tpfoyer.repository.EtudiantRepository;

import tn.esprit.tpfoyer.service.EtudiantServiceImpl;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Test
    void testRetrieveAllEtudiants() {
        // Arrange
        List<Etudiant> etudiants = List.of(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        // Act
        List<Etudiant> result = etudiantService.retrieveAllEtudiants();

        // Assert
        assertEquals(2, result.size());
        verify(etudiantRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveEtudiant() {
        // Arrange
        Long id = 1L;
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(id);
        when(etudiantRepository.findById(id)).thenReturn(Optional.of(etudiant));

        // Act
        Etudiant result = etudiantService.retrieveEtudiant(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(id);
    }

    @Test
    void testAddEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.addEtudiant(etudiant);

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testModifyEtudiant() {
        // Arrange
        Etudiant etudiant = new Etudiant();
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.modifyEtudiant(etudiant);

        // Assert
        assertNotNull(result);
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void testRemoveEtudiant() {
        // Arrange
        Long id = 1L;

        // Act
        etudiantService.removeEtudiant(id);

        // Assert
        verify(etudiantRepository, times(1)).deleteById(id);
    }

    @Test
    void testRecupererEtudiantParCin() {
        // Arrange
        long cin = 12345678L;
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cin);
        when(etudiantRepository.findEtudiantByCinEtudiant(cin)).thenReturn(etudiant);

        // Act
        Etudiant result = etudiantService.recupererEtudiantParCin(cin);

        // Assert
        assertNotNull(result);
        assertEquals(cin, result.getCinEtudiant());
        verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant(cin);
    }
}