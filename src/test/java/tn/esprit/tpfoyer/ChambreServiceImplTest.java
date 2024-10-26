package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.Entities.BlocDTO;
import tn.esprit.tpfoyer.Entities.Chambre;
import tn.esprit.tpfoyer.Entities.ChambreDTO;
import tn.esprit.tpfoyer.FeignClient.BlocClient;
import tn.esprit.tpfoyer.Repository.ChambreRepository;
import tn.esprit.tpfoyer.Services.ChambreServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ChambreServiceImplTest {

    @InjectMocks
    private ChambreServiceImpl chambreService;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocClient blocClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    ////////////////////// Blocs ////////////////////////

    @Test
    public void testAddChambreAndAssignToBloc_Success() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        BlocDTO blocDTO = new BlocDTO();
        blocDTO.setIdBloc(1L);

        when(blocClient.retrieveBloc(1L)).thenReturn(blocDTO);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Chambre result = chambreService.addChambreAndAssignToBloc(chambre, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(blocClient, times(1)).addChambreToBloc(1L, 1L);
        verify(chambreRepository, times(1)).save(any(Chambre.class));
    }

    @Test
    public void testAddChambreAndAssignToBloc_BlocNotFound() {
        Chambre chambre = new Chambre();

        when(blocClient.retrieveBloc(1L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> chambreService.addChambreAndAssignToBloc(chambre, 1L));
        verify(chambreRepository, never()).save(any(Chambre.class));
    }

    @Test
    public void testDeleteChambresByBlocId() {
        List<Chambre> chambres = new ArrayList<>();
        chambres.add(new Chambre());

        when(chambreRepository.findByIdBloc(1L)).thenReturn(chambres);

        chambreService.deleteChambresByBlocId(1L);

        verify(chambreRepository, times(1)).deleteAll(chambres);
    }

    @Test
    public void testDeleteChambreAndRemoveFromBloc_Success() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setIdBloc(1L);

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        chambreService.deleteChambreAndRemoveFromBloc(1L);

        verify(chambreRepository, times(1)).deleteById(1L);
        verify(blocClient, times(1)).removeChambreFromBloc(1L, 1L);
    }

    @Test
    public void testDeleteChambreAndRemoveFromBloc_ChambreNotFound() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chambreService.deleteChambreAndRemoveFromBloc(1L));
        verify(chambreRepository, never()).deleteById(anyLong());
    }

    @Test
    public void testGetChambresByBlocId() {
        List<Chambre> chambres = new ArrayList<>();
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambres.add(chambre);

        when(chambreRepository.findByIdBloc(1L)).thenReturn(chambres);

        List<ChambreDTO> result = chambreService.getChambresByBlocId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdChambre());
        verify(chambreRepository, times(1)).findByIdBloc(1L);
    }

    //////////////////// Reservation ////////////////////////

    @Test
    public void testUpdateChambreAvailability() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        chambreService.updateChambreAvailability(1L, true);

        assertTrue(chambre.getIsReserved());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    public void testRetrieveChambre_Success() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.retrieveChambre(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
    }

    @Test
    public void testRetrieveChambre_ChambreNotFound() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chambreService.retrieveChambre(1L));
    }

    @Test
    public void testUpdateChambreReservations() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);

        List<String> reservations = List.of("res1", "res2");

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        chambreService.updateChambreReservations(1L, reservations);

        assertEquals(reservations, chambre.getIdReservations());
        verify(chambreRepository, times(1)).save(chambre);
    }
}
