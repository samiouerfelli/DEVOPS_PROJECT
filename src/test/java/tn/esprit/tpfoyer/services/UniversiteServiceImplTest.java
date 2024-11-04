package tn.esprit.tpfoyer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    private Universite universite;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = new Universite(1, "Test University", "123 Test St", null);
    }

    @Test
     void testRetrieveAllUniversites() {
        // Arrange
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite));

        // Act
        List<Universite> result = universiteService.retrieveAllUniversites();

        // Assert
        assertEquals(1, result.size());
        assertEquals(universite, result.get(0));
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
     void testRetrieveUniversite() {
        // Arrange
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        // Act
        Universite result = universiteService.retrieveUniversite(1L);

        // Assert
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
     void testAddUniversite() {
        // Arrange
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Act
        Universite result = universiteService.addUniversite(universite);

        // Assert
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
     void testModifyUniversite() {
        // Arrange
        when(universiteRepository.save(universite)).thenReturn(universite);

        // Act
        Universite result = universiteService.modifyUniversite(universite);

        // Assert
        assertEquals(universite, result);
        verify(universiteRepository, times(1)).save(universite);
    }

    @Test
     void testRemoveUniversite() {
        // Act
        universiteService.removeUniversite(1L);

        // Assert
        verify(universiteRepository, times(1)).deleteById(1L);
    }
}