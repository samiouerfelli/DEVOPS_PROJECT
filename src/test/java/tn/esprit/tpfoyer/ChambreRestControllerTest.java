package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.ChambreDTO;
import tn.esprit.tpfoyer.entities.TypeChambre;
import tn.esprit.tpfoyer.restcontroller.ChambreRestController;
import tn.esprit.tpfoyer.services.ChambreServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChambreRestController.class)
 class ChambreRestControllerTest {

    @Autowired
     MockMvc mockMvc;

    @MockBean
     ChambreServiceImpl chambreService;

    // Test for adding a chambre and assigning it to a bloc
    @Test
     void testAddChambreAndAssignToBloc() throws Exception {
        Chambre newChambre = new Chambre();
        newChambre.setIdChambre(1L);
        newChambre.setNumeroChambre(101L);
        newChambre.setTypeC(TypeChambre.SIMPLE);
        newChambre.setIsReserved(false);
        newChambre.setIdBloc(1L);

        when(chambreService.addChambreAndAssignToBloc(any(Chambre.class), anyLong())).thenReturn(newChambre);

        mockMvc.perform(post("/api/v1/chambres/add-chambre/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroChambre\":101,\"typeC\":\"SIMPLE\",\"isReserved\":false}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idChambre").value(1L))
                .andExpect(jsonPath("$.numeroChambre").value(101L))
                .andExpect(jsonPath("$.typeC").value("SIMPLE"))
                .andExpect(jsonPath("$.isReserved").value(false));

        verify(chambreService, times(1)).addChambreAndAssignToBloc(any(Chambre.class), eq(1L));
    }

    // Test for deleting all chambres associated with a bloc ID
    @Test
     void testDeleteChambresByBlocId() throws Exception {
        mockMvc.perform(delete("/api/v1/chambres/delete-by-bloc/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Chambres associated with Bloc ID 1 have been deleted successfully."));

        verify(chambreService, times(1)).deleteChambresByBlocId(1L);
    }

    // Test for deleting a chambre and removing it from a bloc
    @Test
     void testDeleteChambreAndRemoveFromBloc() throws Exception {
        mockMvc.perform(delete("/api/v1/chambres/delete-chambre/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chambre with ID 1 has been deleted successfully, and it was removed from the associated Bloc."));

        verify(chambreService, times(1)).deleteChambreAndRemoveFromBloc(1L);
    }

    // Test for retrieving chambres by bloc ID
    @Test
     void testGetChambresByBlocId() throws Exception {
        ChambreDTO chambreDTO = new ChambreDTO();
        chambreDTO.setIdChambre(1L);
        chambreDTO.setNumeroChambre(101L);
        chambreDTO.setTypeChambre(TypeChambre.SIMPLE);
        chambreDTO.setIsReserved(false);

        when(chambreService.getChambresByBlocId(anyLong())).thenReturn(List.of(chambreDTO));

        mockMvc.perform(get("/api/v1/chambres/retrieve-by-bloc/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idChambre").value(1L))
                .andExpect(jsonPath("$[0].numeroChambre").value(101L))
                .andExpect(jsonPath("$[0].typeChambre").value("SIMPLE"))
                .andExpect(jsonPath("$[0].isReserved").value(false));

        verify(chambreService, times(1)).getChambresByBlocId(1L);
    }

    // Test for updating chambre availability
    @Test
     void testUpdateChambreAvailability() throws Exception {
        mockMvc.perform(put("/api/v1/chambres/update-availability/1")
                        .param("isReserved", "true"))
                .andExpect(status().isOk());

        verify(chambreService, times(1)).updateChambreAvailability(1L, true);
    }

    // Test for retrieving a chambre by ID
    @Test
     void testGetChambreById() throws Exception {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        chambre.setIsReserved(false);

        when(chambreService.retrieveChambre(anyLong())).thenReturn(chambre);

        mockMvc.perform(get("/api/v1/chambres/retrieve-chambre/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idChambre").value(1L))
                .andExpect(jsonPath("$.numeroChambre").value(101L))
                .andExpect(jsonPath("$.typeC").value("SIMPLE"))
                .andExpect(jsonPath("$.isReserved").value(false));

        verify(chambreService, times(1)).retrieveChambre(1L);
    }

    // Test for updating chambre reservations
    @Test
     void testUpdateChambreReservations() throws Exception {
        mockMvc.perform(put("/api/v1/chambres/1/update-reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"Reservation1\", \"Reservation2\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservations updated successfully for Chambre with ID: 1"));

        verify(chambreService, times(1)).updateChambreReservations(1L, List.of("Reservation1", "Reservation2"));
    }
}
