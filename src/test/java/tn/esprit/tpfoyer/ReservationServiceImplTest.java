package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.Entities.ChambreDTO;
import tn.esprit.tpfoyer.Entities.EtudiantDTO;
import tn.esprit.tpfoyer.Entities.Reservation;
import tn.esprit.tpfoyer.FeignClient.ChambreClient;
import tn.esprit.tpfoyer.FeignClient.EtudiantClient;
import tn.esprit.tpfoyer.Repository.ReservationRepository;
import tn.esprit.tpfoyer.Services.ReservationServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantClient etudiantClient;

    @Mock
    private ChambreClient chambreClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation() {
        EtudiantDTO etudiant = new EtudiantDTO();
        etudiant.setIdEtudiant(1L);

        ChambreDTO chambre = new ChambreDTO();
        chambre.setIdChambre(1L);

        when(etudiantClient.getEtudiantById(anyLong())).thenReturn(etudiant);
        when(chambreClient.getChambreById(anyLong())).thenReturn(chambre);
        when(reservationRepository.findByIdEtudiantAndAnneeUniversitaireAndEstValideTrue(anyLong(), any())).thenReturn(Optional.empty());
        when(reservationRepository.countByChambreAndAnneeUniversitaire(anyLong(), any())).thenReturn(1);

        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.createReservation(1L, 1L, new Date());

        assertNotNull(result);
        verify(etudiantClient, times(1)).updateEtudiantReservations(anyLong(), any());
        verify(chambreClient, times(1)).updateChambreReservations(anyLong(), any());
    }

    @Test
    public void testCancelReservation() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setIdChambre(1L);
        reservation.setIdEtudiant(1L);
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        when(reservationRepository.findById(anyString())).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation("1");

        assertFalse(reservation.isEstValide());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testGetReservationById() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById("1");

        assertEquals("1", result.getIdReservation());
    }

    @Test
    public void testGetReservationsByEtudiant() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findByIdEtudiant(anyLong())).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getReservationsByEtudiant(1L);

        assertFalse(reservations.isEmpty());
    }

    @Test
    public void testGetReservationsByChambreAndAnnee() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findByIdChambreAndAnneeUniversitaireAndEstValideTrue(anyLong(), any())).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getReservationsByChambreAndAnnee(1L, new Date());

        assertFalse(reservations.isEmpty());
    }
}
