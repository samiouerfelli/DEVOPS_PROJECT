package tn.esprit.tpfoyer;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.tpfoyer.entity.*;

import tn.esprit.tpfoyer.repository.*;

import tn.esprit.tpfoyer.service.*;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
 class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @Test
     void testRetrieveAllFoyers() {
        // Arrange
        List<Foyer> foyers = List.of(new Foyer(), new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        // Act
        List<Foyer> result = foyerService.retrieveAllFoyers();

        // Assert
        assertEquals(2, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
     void testRetrieveFoyer() {
        // Arrange
        Long id = 1L;
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(id);
        when(foyerRepository.findById(id)).thenReturn(Optional.of(foyer));

        // Act
        Foyer result = foyerService.retrieveFoyer(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getIdFoyer());
        verify(foyerRepository, times(1)).findById(id);
    }

    @Test
     void testAddFoyer() {
        // Arrange
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Act
        Foyer result = foyerService.addFoyer(foyer);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
     void testModifyFoyer() {
        // Arrange
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Act
        Foyer result = foyerService.modifyFoyer(foyer);

        // Assert
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
     void testRemoveFoyer() {
        // Arrange
        Long id = 1L;

        // Act
        foyerService.removeFoyer(id);

        // Assert
        verify(foyerRepository, times(1)).deleteById(id);
    }
}
