package tn.esprit.tpfoyer.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Test Bloc");
        bloc.setCapaciteBloc(100);
    }

    @Test
    void testRetrieveAllBlocs() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        List<Bloc> blocs = blocService.retrieveAllBlocs();

        assertEquals(1, blocs.size());
        assertEquals("Test Bloc", blocs.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBloc() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc found = blocService.retrieveBloc(1L);

        assertNotNull(found);
        assertEquals("Test Bloc", found.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc created = blocService.addBloc(bloc);

        assertNotNull(created);
        assertEquals("Test Bloc", created.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testModifyBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc modified = blocService.modifyBloc(bloc);

        assertNotNull(modified);
        assertEquals("Test Bloc", modified.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRemoveBloc() {
        doNothing().when(blocRepository).deleteById(1L);

        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }



    @Test
    void testRetrieveBlocsSelonCapacite_WithValidCapacity() {
        // Arrange
        Bloc bloc1 = new Bloc(); // Create a Bloc that meets the condition
        bloc1.setCapaciteBloc(60); // This should be >= 50
        Bloc bloc2 = new Bloc(); // Create a Bloc that does not meet the condition
        bloc2.setCapaciteBloc(40); // This is < 50
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> blocs = blocService.retrieveBlocsSelonCapacite(50);

        // Assert
        assertEquals(1, blocs.size()); // Only one bloc meets the condition
        assertTrue(blocs.get(0).getCapaciteBloc() >= 50);
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite_WithNoValidBlocs() {
        // Arrange
        Bloc bloc1 = new Bloc();
        bloc1.setCapaciteBloc(40); // This is < 50
        Bloc bloc2 = new Bloc();
        bloc2.setCapaciteBloc(30); // This is also < 50
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc1, bloc2));

        // Act
        List<Bloc> blocs = blocService.retrieveBlocsSelonCapacite(50);

        // Assert
        assertTrue(blocs.isEmpty()); // Expect an empty list since none meet the condition
        verify(blocRepository, times(1)).findAll();
    }


    @Test
    void testFindBlocsSansFoyer() {
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(Arrays.asList(bloc));

        List<Bloc> blocs = blocService.trouverBlocsSansFoyer();

        assertEquals(1, blocs.size());
        assertEquals("Test Bloc", blocs.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void testFindBlocsParNomEtCap() {
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Test Bloc", 100)).thenReturn(Arrays.asList(bloc));

        List<Bloc> blocs = blocService.trouverBlocsParNomEtCap("Test Bloc", 100);

        assertEquals(1, blocs.size());
        assertEquals("Test Bloc", blocs.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Test Bloc", 100);
    }
}

