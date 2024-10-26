package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.Entities.Universite;
import tn.esprit.tpfoyer.Entities.UniversiteDTO;
import tn.esprit.tpfoyer.FeignClient.FoyerClient;
import tn.esprit.tpfoyer.Repository.UniversiteRepository;
import tn.esprit.tpfoyer.Services.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UniversiteServiceImplTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private FoyerClient foyerClient;

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUniversite() {
        Universite universite = new Universite();
        universite.setNomUniversite("Test University");

        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite createdUniversite = universiteService.addUniversite(universite);
        assertNotNull(createdUniversite);
        assertEquals("Test University", createdUniversite.getNomUniversite());
    }

    @Test
    void testRetrieveUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Sample University");

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        UniversiteDTO retrievedUniversite = universiteService.retrieveUniversite(1L);
        assertNotNull(retrievedUniversite);
        assertEquals("Sample University", retrievedUniversite.getNomUniversite());
    }

    @Test
    void testAssignFoyerToUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        universiteService.assignFoyerToUniversite(1L, 2L);

        verify(universiteRepository, times(1)).save(universite);
        assertEquals(2L, universite.getIdFoyer());
    }

    @Test
    void testDeleteUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setIdFoyer(2L);

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));

        universiteService.deleteUniversite(1L);

        verify(foyerClient, times(1)).unassignFoyerFromUniversite(2L);
        verify(universiteRepository, times(1)).deleteById(1L);
    }
}
