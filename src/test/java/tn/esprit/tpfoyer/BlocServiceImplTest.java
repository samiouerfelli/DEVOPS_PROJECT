package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.tpfoyer.FeignClient.ChambreClient;
import tn.esprit.tpfoyer.FeignClient.FoyerClient;
import tn.esprit.tpfoyer.Repository.BlocRepository;
import tn.esprit.tpfoyer.Services.BlocServiceImpl;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.BlocDTO;
import tn.esprit.tpfoyer.entities.FoyerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BlocServiceImplTest {

    @InjectMocks
    private BlocServiceImpl blocService;

    @Mock
    private BlocRepository blocRepository;

    @Mock
    private FoyerClient foyerClient;

    @Mock
    private ChambreClient chambreClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddBlocAndAssignToFoyer() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        FoyerDTO foyerDTO = new FoyerDTO();
        foyerDTO.setIdFoyer(1L);

        when(foyerClient.retrieveFoyer(1L)).thenReturn(foyerDTO);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBlocAndAssignToFoyer(bloc, 1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdFoyer());

        verify(foyerClient, times(1)).addBlocToFoyer(1L, bloc.getIdBloc());
    }

    @Test
    public void testDeleteBlocsByFoyerId() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc());

        when(blocRepository.findByIdFoyer(1L)).thenReturn(blocs);

        blocService.deleteBlocsByFoyerId(1L);

        verify(blocRepository, times(1)).deleteAll(blocs);
    }

    @Test
    public void testDeleteBlocAndRemoveFromFoyer() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setIdFoyer(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        blocService.deleteBlocAndRemoveFromFoyer(1L);

        verify(blocRepository, times(1)).deleteById(1L);
        verify(foyerClient, times(1)).removeBlocFromFoyer(1L, 1L);
    }

    @Test
    public void testGetBlocsByFoyerId() {
        Bloc bloc = new Bloc();
        bloc.setIdFoyer(1L);
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(bloc);

        when(blocRepository.findByIdFoyer(1L)).thenReturn(blocs);

        List<BlocDTO> result = blocService.getBlocsByFoyerId(1L);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdFoyer());
    }

    @Test
    public void testRetrieveBloc() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        BlocDTO result = blocService.retrieveBloc(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
    }

    @Test
    public void testAddChambreToBloc() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        List<Long> chambres = new ArrayList<>();
        bloc.setIdChambres(chambres);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(blocRepository.save(bloc)).thenReturn(bloc);

        blocService.addChambreToBloc(1L, 2L);

        assertTrue(bloc.getIdChambres().contains(2L));
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testDeleteBlocAndChambres() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        blocService.deleteBlocAndChambres(1L);

        verify(chambreClient, times(1)).deleteChambresByBlocId(1L);
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testRemoveChambreFromBloc() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        List<Long> chambres = new ArrayList<>();
        chambres.add(2L);
        bloc.setIdChambres(chambres);

        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        when(blocRepository.save(bloc)).thenReturn(bloc);

        blocService.removeChambreFromBloc(1L, 2L);

        assertFalse(bloc.getIdChambres().contains(2L));
        verify(blocRepository, times(1)).save(bloc);
    }
}
