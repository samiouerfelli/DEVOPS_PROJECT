package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.Repository.UniversiteRepository;
import tn.esprit.tpfoyer.Entities.Universite;
import tn.esprit.tpfoyer.Entities.UniversiteDTO;
import tn.esprit.tpfoyer.FeignClient.FoyerClient;
import tn.esprit.tpfoyer.Services.UniversiteServiceImpl;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UniversiteServiceImplTest {

    @InjectMocks
    private UniversiteServiceImpl universiteService;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private FoyerClient foyerClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Test University");
        universite.setAdresse("Test Address");

        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        Universite result = universiteService.addUniversite(universite);

        assertEquals("Test University", result.getNomUniversite());
        assertEquals("Test Address", result.getAdresse());
    }

    @Test
    public void testAssignFoyerToUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);

        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);

        universiteService.assignFoyerToUniversite(1L, 2L);

        verify(universiteRepository, times(1)).save(universite);
        assertEquals(2L, universite.getIdFoyer());
    }

    @Test
    public void testRetrieveUniversite() {
        Universite mockUniversite = new Universite();
        mockUniversite.setIdUniversite(1L);
        mockUniversite.setNomUniversite("Test University");
        mockUniversite.setAdresse("Test Address");

        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(mockUniversite));

        UniversiteDTO result = universiteService.retrieveUniversite(1L);

        assertEquals("Test University", result.getNomUniversite());
        assertEquals("Test Address", result.getAdresse());
    }

    @Test
    public void testRetrieveAllUniversites() {
        Universite universite1 = new Universite();
        universite1.setIdUniversite(1L);
        universite1.setNomUniversite("University A");

        Universite universite2 = new Universite();
        universite2.setIdUniversite(2L);
        universite2.setNomUniversite("University B");

        when(universiteRepository.findAll()).thenReturn(List.of(universite1, universite2));

        List<UniversiteDTO> results = universiteService.retrieveAllUniversites();

        assertEquals(2, results.size());
        assertEquals("University A", results.get(0).getNomUniversite());
        assertEquals("University B", results.get(1).getNomUniversite());
    }

    @Test
    public void testDeleteUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setIdFoyer(2L);

        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite));

        universiteService.deleteUniversite(1L);

        verify(foyerClient, times(1)).unassignFoyerFromUniversite(2L);
        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUnassignFoyerFromUniversite() {
        Universite universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setIdFoyer(2L);

        when(universiteRepository.findById(anyLong())).thenReturn(Optional.of(universite));

        universiteService.unassignFoyerFromUniversite(1L);

        verify(universiteRepository, times(1)).save(universite);
        assertNull(universite.getIdFoyer());
    }
}
