package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.Entities.Foyer;
import tn.esprit.tpfoyer.Entities.FoyerDTO;
import tn.esprit.tpfoyer.Entities.UniversiteDTO;
import tn.esprit.tpfoyer.FeignClient.BlocClient;
import tn.esprit.tpfoyer.FeignClient.UniversiteClient;
import tn.esprit.tpfoyer.Repository.FoyerRepository;
import tn.esprit.tpfoyer.Services.FoyerServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteClient universiteClient;

    @Mock
    private BlocClient blocClient;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for adding a Foyer and assigning it to a Universite
    @Test
    public void testAddFoyerAndAssignToUniversite() {
        UniversiteDTO universiteDTO = new UniversiteDTO();
        universiteDTO.setIdUniversite(1L);

        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Foyer1");
        foyer.setCapaciteFoyer(100L);

        Foyer savedFoyer = new Foyer();
        savedFoyer.setIdFoyer(1L);
        savedFoyer.setNomFoyer("Foyer1");
        savedFoyer.setCapaciteFoyer(100L);
        savedFoyer.setIdUniversite(1L);

        when(universiteClient.retrieveUniversite(1L)).thenReturn(universiteDTO);
        when(foyerRepository.save(any(Foyer.class))).thenReturn(savedFoyer);

        Foyer result = foyerService.addFoyerAndAssignToUniversite(foyer, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFoyer());
        assertEquals("Foyer1", result.getNomFoyer());
        verify(foyerRepository, times(1)).save(any(Foyer.class));
    }

    // Test for retrieving a Foyer by ID
    @Test
    public void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer1");

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        FoyerDTO result = foyerService.retrieveFoyer(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdFoyer());
        assertEquals("Foyer1", result.getNomFoyer());
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test for retrieving all Foyers
    @Test
    public void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer1");

        Foyer foyer2 = new Foyer();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer2");

        when(foyerRepository.findAll()).thenReturn(List.of(foyer1, foyer2));

        List<FoyerDTO> result = foyerService.retrieveAllFoyers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    // Test for unassigning a Universite from a Foyer
    @Test
    public void testUnassignUniversiteFromFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setIdUniversite(1L);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        foyerService.unassignUniversiteFromFoyer(1L);

        assertNull(foyer.getIdUniversite());
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test for deleting a Foyer
    @Test
    public void testDeleteFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setIdUniversite(1L);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        foyerService.deleteFoyer(1L);

        verify(foyerRepository, times(1)).deleteById(1L);
        verify(universiteClient, times(1)).unassignFoyerFromUniversite(1L);
    }

    // Test for adding a Bloc to a Foyer
    @Test
    public void testAddBlocToFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setIdBlocs(new ArrayList<>());

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        foyerService.addBlocToFoyer(1L, 2L);

        assertTrue(foyer.getIdBlocs().contains(2L));
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test for deleting a Foyer and its associated Blocs
    @Test
    public void testDeleteFoyerAndBlocs() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setIdUniversite(1L);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        foyerService.deleteFoyerAndBlocs(1L);

        verify(blocClient, times(1)).deleteBlocsByFoyerId(1L);
        verify(universiteClient, times(1)).unassignFoyerFromUniversite(1L);
        verify(foyerRepository, times(1)).deleteById(1L);
    }

    // Test for removing a Bloc from a Foyer
    @Test
    public void testRemoveBlocFromFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setIdBlocs(new ArrayList<>(List.of(2L)));

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        foyerService.removeBlocFromFoyer(1L, 2L);

        assertFalse(foyer.getIdBlocs().contains(2L));
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test for retrieving Foyers without Blocs
    @Test
    public void testGetFoyersWithoutBlocs() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer1");
        foyer.setIdBlocs(new ArrayList<>());

        when(foyerRepository.findByIdBlocsIsEmpty()).thenReturn(List.of(foyer));

        List<FoyerDTO> result = foyerService.getFoyersWithoutBlocs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Foyer1", result.get(0).getNomFoyer());
        verify(foyerRepository, times(1)).findByIdBlocsIsEmpty();
    }
}
