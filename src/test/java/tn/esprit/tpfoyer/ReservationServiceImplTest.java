package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entities.ChambreDTO;
import tn.esprit.tpfoyer.entities.EtudiantDTO;
import tn.esprit.tpfoyer.entities.Reservation;
import tn.esprit.tpfoyer.feignclient.ChambreClient;
import tn.esprit.tpfoyer.feignclient.EtudiantClient;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.services.ReservationServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

 class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EtudiantClient etudiantClient;

    @Mock
    private ChambreClient chambreClient;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateReservation() {
        EtudiantDTO etudiant = new EtudiantDTO();
        etudiant.setIdReservations(new ArrayList<>());

        ChambreDTO chambre = new ChambreDTO();
        chambre.setIdReservations(new ArrayList<>());

        when(etudiantClient.getEtudiantById(anyLong())).thenReturn(etudiant);
        when(chambreClient.getChambreById(anyLong())).thenReturn(chambre);
        when(reservationRepository.findByIdEtudiantAndAnneeUniversitaireAndEstValideTrue(anyLong(), any(Date.class)))
                .thenReturn(Optional.empty());
        when(reservationRepository.countByChambreAndAnneeUniversitaire(anyLong(), any(Date.class))).thenReturn(0);

        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = reservationService.createReservation(1L, 1L, new Date());

        assertNotNull(createdReservation);
        assertEquals("1", createdReservation.getIdReservation());
    }

    @Test
     void testCancelReservation() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setIdEtudiant(1L);
        reservation.setIdChambre(1L);
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);

        EtudiantDTO etudiant = new EtudiantDTO();
        etudiant.setIdReservations(new ArrayList<>());

        ChambreDTO chambre = new ChambreDTO();
        chambre.setIdReservations(new ArrayList<>());

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));
        when(etudiantClient.getEtudiantById(1L)).thenReturn(etudiant);
        when(chambreClient.getChambreById(1L)).thenReturn(chambre);

        reservationService.cancelReservation("1");

        assertFalse(reservation.isEstValide());
        verify(etudiantClient, times(1)).updateEtudiantReservations(anyLong(), anyList());
        verify(chambreClient, times(1)).updateChambreReservations(anyLong(), anyList());
    }

    @Test
     void testGetReservationById() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById("1");

        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
    }

    @Test
     void testGetReservationsByEtudiant() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findByIdEtudiant(anyLong())).thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.getReservationsByEtudiant(1L);

        assertEquals(1, reservations.size());
        assertEquals("1", reservations.get(0).getIdReservation());
    }

    @Test
     void testGetReservationsByChambreAndAnnee() {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        when(reservationRepository.findByIdChambreAndAnneeUniversitaireAndEstValideTrue(anyLong(), any(Date.class)))
                .thenReturn(Collections.singletonList(reservation));

        List<Reservation> reservations = reservationService.getReservationsByChambreAndAnnee(1L, new Date());

        assertEquals(1, reservations.size());
        assertEquals("1", reservations.get(0).getIdReservation());
    }
}
