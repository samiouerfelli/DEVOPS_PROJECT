package tn.esprit.tpfoyer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class FoyerServiceImplTest {

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @Mock
    private FoyerRepository foyerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllFoyers_success() {
        List<Foyer> foyers = Arrays.asList(new Foyer(), new Foyer());
        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.retrieveAllFoyers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveAllFoyers_emptyList() {
        when(foyerRepository.findAll()).thenReturn(Arrays.asList());

        List<Foyer> result = foyerService.retrieveAllFoyers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveFoyer_existingId() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(anyLong())).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);

        assertNotNull(result);
        assertEquals(foyer, result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveFoyer_nonExistentId() {
        when(foyerRepository.findById(anyLong())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(99L));
        assertEquals("Foyer with ID 99 not found", exception.getMessage());
        verify(foyerRepository, times(1)).findById(99L);
    }

    @Test
    void testAddFoyer_success() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);

        assertNotNull(result);
        assertEquals(foyer, result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testAddFoyer_nullFoyer() {
        when(foyerRepository.save(null)).thenThrow(new IllegalArgumentException("Foyer cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, times(1)).save(null);
    }

    @Test
    void testModifyFoyer_success() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(any(Foyer.class))).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);

        assertNotNull(result);
        assertEquals(foyer, result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testModifyFoyer_nullFoyer() {
        when(foyerRepository.save(null)).thenThrow(new IllegalArgumentException("Foyer cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, times(1)).save(null);
    }

    @Test
    void testRemoveFoyer_existingId() {
        doNothing().when(foyerRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> foyerService.removeFoyer(1L));
        verify(foyerRepository, times(1)).deleteById(1L);
    }




    @Test
    void testRemoveFoyer_nonExistentId() {
        doThrow(new RuntimeException("Foyer not found")).when(foyerRepository).deleteById(anyLong());

        assertThrows(RuntimeException.class, () -> foyerService.removeFoyer(99L));
        verify(foyerRepository, times(1)).deleteById(99L);
    }
}
