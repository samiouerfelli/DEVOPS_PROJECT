package tn.esprit.tpfoyer.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class BlocServiceImplTest {
    private static final String BLOC_A = "Bloc A";
    @Mock
    private BlocRepository blocRepository;


    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc;

    @BeforeEach
    public void setUp() {
        // Initialize a Foyer object if needed for your tests
        Foyer foyer = new Foyer();

        bloc = new Bloc();
        bloc.setIdBloc(1L); // Adjusted to use idBloc
        bloc.setNomBloc(BLOC_A);
        bloc.setCapaciteBloc(100);
        bloc.setFoyer(foyer); // Assuming you may want to link it with a Foyer
    }

    @Test
    public void testRetrieveAllBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveAllBlocs();

        assertEquals(1, result.size());
        assertEquals(BLOC_A, result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveBlocsSelonCapacite() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(50);

        assertEquals(1, result.size());
        assertEquals(BLOC_A, result.get(0).getNomBloc());
    }

    @Test
    public void testRetrieveBloc() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.retrieveBloc(1L);

        assertNotNull(result);
        assertEquals(BLOC_A, result.getNomBloc());
    }

    @Test
    public void testAddBloc() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertNotNull(result);
        assertEquals(BLOC_A, result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testModifyBloc() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.modifyBloc(bloc);

        assertNotNull(result);
        assertEquals(BLOC_A, result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testRemoveBloc() {
        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testTrouverBlocsSansFoyer() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);

        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        assertEquals(1, result.size());
        assertEquals(BLOC_A, result.get(0).getNomBloc());
    }

    @Test
    public void testTrouverBlocsParNomEtCap() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);

        when(blocRepository.findAllByNomBlocAndCapaciteBloc(BLOC_A, 100)).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsParNomEtCap(BLOC_A, 100);

        assertEquals(1, result.size());
        assertEquals(BLOC_A, result.get(0).getNomBloc());
    }
}
