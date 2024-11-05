package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationRestControllerTest {

    @InjectMocks
    private ReservationRestController reservationRestController;

    @Mock
    private IReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReservations() {
        // Arrange
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(new Reservation());
        when(reservationService.retrieveAllReservations()).thenReturn(reservationList);

        // Act
        List<Reservation> result = reservationRestController.getReservations();

        // Assert
        assertEquals(1, result.size());
        verify(reservationService, times(1)).retrieveAllReservations();
    }

    @Test
    void testRetrieveReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");
        when(reservationService.retrieveReservation("1")).thenReturn(reservation);

        // Act
        Reservation result = reservationRestController.retrieveReservation("1");

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getIdReservation());
        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void testRetrieveReservationParDateEtStatus() {
        // Arrange
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(new Reservation());
        Date date = new Date();
        when(reservationService.trouverResSelonDateEtStatus(date, true)).thenReturn(reservationList);

        // Act
        List<Reservation> result = reservationRestController.retrieveReservationParDateEtStatus(date, true);

        // Assert
        assertEquals(1, result.size());
        verify(reservationService, times(1)).trouverResSelonDateEtStatus(date, true);
    }

    @Test
    void testAddReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationService.addReservation(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationRestController.addReservation(reservation);

        // Assert
        assertNotNull(result);
        verify(reservationService, times(1)).addReservation(reservation);
    }

    @Test
    void testRemoveReservation() {
        // Act
        reservationRestController.removeReservation("1");

        // Assert
        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void testModifyReservation() {
        // Arrange
        Reservation reservation = new Reservation();
        when(reservationService.modifyReservation(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationRestController.modifyReservation(reservation);

        // Assert
        assertNotNull(result);
        verify(reservationService, times(1)).modifyReservation(reservation);
    }
}