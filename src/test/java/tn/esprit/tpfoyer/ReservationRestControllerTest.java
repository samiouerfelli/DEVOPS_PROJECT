package tn.esprit.tpfoyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.control.ReservationRestController;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.service.IReservationService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationRestControllerTest {

    @Mock
    private ReservationRestController reservationRestController;

    @Mock
    private IReservationService reservationService;

    private MockMvc mockMvc;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationRestController).build();

        // Setting up a sample reservation for testing
        reservation = new Reservation();
        reservation.setIdReservation("1");
        reservation.setAnneeUniversitaire(new Date());
        reservation.setEstValide(true);
    }

    @Test
    void getReservations_ShouldReturnListOfReservations() throws Exception {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.retrieveAllReservations()).thenReturn(reservations);

        mockMvc.perform(get("/reservation/retrieve-all-reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idReservation").value("1"));

        verify(reservationService, times(1)).retrieveAllReservations();
    }

    @Test
    void retrieveReservation_ShouldReturnReservation_WhenExists() throws Exception {
        when(reservationService.retrieveReservation(anyString())).thenReturn(reservation);

        mockMvc.perform(get("/reservation/retrieve-reservation/{reservation-id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).retrieveReservation("1");
    }

    @Test
    void retrieveReservation_ShouldReturn404_WhenDoesNotExist() throws Exception {
        when(reservationService.retrieveReservation("2")).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/reservation/retrieve-reservation/{reservation-id}", "2"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).retrieveReservation("2");
    }

    @Test
    void retrieveReservationParDateEtStatus_ShouldReturnListOfReservations() throws Exception {
        List<Reservation> reservations = Arrays.asList(reservation);
        when(reservationService.trouverResSelonDateEtStatus(any(Date.class), any(Boolean.class))).thenReturn(reservations);

        mockMvc.perform(get("/reservation/retrieve-reservation-date-status/{d}/{v}", new Date(), true))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].idReservation").value("1"));

        verify(reservationService, times(1)).trouverResSelonDateEtStatus(any(Date.class), any(Boolean.class));
    }

    @Test
    void addReservation_ShouldReturnSavedReservation() throws Exception {
        when(reservationService.addReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservation/add-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).addReservation(any(Reservation.class));
    }

    @Test
    void removeReservation_ShouldCallRemove() throws Exception {
        mockMvc.perform(delete("/reservation/remove-reservation/{reservation-id}", "1"))
                .andExpect(status().isOk());

        verify(reservationService, times(1)).removeReservation("1");
    }

    @Test
    void modifyReservation_ShouldReturnModifiedReservation() throws Exception {
        when(reservationService.modifyReservation(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(put("/reservation/modify-reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idReservation").value("1"));

        verify(reservationService, times(1)).modifyReservation(any(Reservation.class));
    }
}
