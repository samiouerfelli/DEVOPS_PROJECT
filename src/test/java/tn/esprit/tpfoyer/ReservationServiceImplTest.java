package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void retrieveAllReservations_ShouldReturnListOfReservations() {
        when(reservationRepository.findAll()).thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationService.retrieveAllReservations();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void retrieveReservation_ShouldReturnReservation_WhenExists() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.retrieveReservation("1");

        assertNotNull(result);
        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    void retrieveReservation_ShouldThrowException_WhenDoesNotExist() {
        when(reservationRepository.findById("2")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reservationService.retrieveReservation("2");
        });
        verify(reservationRepository, times(1)).findById("2");
    }

    @Test
    void addReservation_ShouldSaveAndReturnReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.addReservation(reservation);

        assertNotNull(result);
        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void modifyReservation_ShouldUpdateAndReturnReservation() {
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        Reservation result = reservationService.modifyReservation(reservation);

        assertNotNull(result);
        assertEquals(reservation, result);
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void trouverResSelonDateEtStatus_ShouldReturnListOfReservations() {
        Date date = new Date();
        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(date, true))
                .thenReturn(Arrays.asList(reservation));

        List<Reservation> result = reservationService.trouverResSelonDateEtStatus(date, true);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(date, true);
    }

    @Test
    void removeReservation_ShouldCallDeleteById() {
        reservationService.removeReservation("1");
        verify(reservationRepository, times(1)).deleteById("1");
    }
}
