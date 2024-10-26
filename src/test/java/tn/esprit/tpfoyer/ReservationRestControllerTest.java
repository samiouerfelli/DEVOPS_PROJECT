package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.Entities.Reservation;
import tn.esprit.tpfoyer.Exception.ReservationException;
import tn.esprit.tpfoyer.RestController.ReservationRestController;
import tn.esprit.tpfoyer.Services.ReservationServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
public class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationServiceImpl reservationService;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testCreateReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        Mockito.when(reservationService.createReservation(anyLong(), anyLong(), any(Date.class)))
                .thenReturn(reservation);

        mockMvc.perform(post("/api/v1/reservations/create")
                        .param("idEtudiant", "1")
                        .param("idChambre", "1")
                        .param("anneeUniversitaire", dateFormat.format(new Date()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idReservation").value("1"));
    }

    @Test
    public void testCreateReservationThrowsException() throws Exception {
        Mockito.when(reservationService.createReservation(anyLong(), anyLong(), any(Date.class)))
                .thenThrow(new ReservationException("Etudiant not found with ID: 1"));

        mockMvc.perform(post("/api/v1/reservations/create")
                        .param("idEtudiant", "1")
                        .param("idChambre", "1")
                        .param("anneeUniversitaire", dateFormat.format(new Date()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Etudiant not found with ID: 1"));
    }

    @Test
    public void testCancelReservation() throws Exception {
        mockMvc.perform(put("/api/v1/reservations/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservation has been canceled."));
    }

    @Test
    public void testGetReservationById() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        Mockito.when(reservationService.getReservationById("1")).thenReturn(reservation);

        mockMvc.perform(get("/api/v1/reservations/retrieve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("1"));
    }

    @Test
    public void testGetReservationsByEtudiant() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        Mockito.when(reservationService.getReservationsByEtudiant(anyLong()))
                .thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(get("/api/v1/reservations/retrieve-by-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("1"));
    }

    @Test
    public void testGetReservationsByChambreAndAnnee() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setIdReservation("1");

        Mockito.when(reservationService.getReservationsByChambreAndAnnee(anyLong(), any(Date.class)))
                .thenReturn(Collections.singletonList(reservation));

        mockMvc.perform(get("/api/v1/reservations/retrieve-by-chambre-year/1")
                        .param("anneeUniversitaire", dateFormat.format(new Date()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("1"));
    }
}
