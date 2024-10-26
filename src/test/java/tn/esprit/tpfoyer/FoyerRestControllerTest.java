package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.entities.Foyer;
import tn.esprit.tpfoyer.entities.FoyerDTO;
import tn.esprit.tpfoyer.restcontroller.FoyerRestController;
import tn.esprit.tpfoyer.services.FoyerServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerRestController.class)
 class FoyerRestControllerTest {

    @Autowired
     MockMvc mockMvc;

    @MockBean
     FoyerServiceImpl foyerService;

    // Test for adding a Foyer and assigning it to a Universite
    @Test
     void testAddFoyerAndAssignToUniversite() throws Exception {
        Foyer newFoyer = new Foyer();
        newFoyer.setIdFoyer(1L);
        newFoyer.setNomFoyer("Foyer1");
        newFoyer.setCapaciteFoyer(100L);
        newFoyer.setIdUniversite(1L);

        when(foyerService.addFoyerAndAssignToUniversite(any(Foyer.class), anyLong())).thenReturn(newFoyer);

        mockMvc.perform(post("/api/v1/foyers/add-foyer/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"Foyer1\",\"capaciteFoyer\":100}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFoyer").value(1L))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer1"))
                .andExpect(jsonPath("$.capaciteFoyer").value(100));

        verify(foyerService, times(1)).addFoyerAndAssignToUniversite(any(Foyer.class), eq(1L));
    }

    // Test for retrieving a single Foyer by ID
    @Test
     void testRetrieveFoyer() throws Exception {
        FoyerDTO foyerDTO = new FoyerDTO();
        foyerDTO.setIdFoyer(1L);
        foyerDTO.setNomFoyer("Foyer1");
        foyerDTO.setCapaciteFoyer(100L);

        when(foyerService.retrieveFoyer(anyLong())).thenReturn(foyerDTO);

        mockMvc.perform(get("/api/v1/foyers/retrieve-foyer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFoyer").value(1L))
                .andExpect(jsonPath("$.nomFoyer").value("Foyer1"))
                .andExpect(jsonPath("$.capaciteFoyer").value(100));

        verify(foyerService, times(1)).retrieveFoyer(1L);
    }

    // Test for retrieving all Foyers
    @Test
     void testRetrieveAllFoyers() throws Exception {
        FoyerDTO foyer1 = new FoyerDTO();
        foyer1.setIdFoyer(1L);
        foyer1.setNomFoyer("Foyer1");

        FoyerDTO foyer2 = new FoyerDTO();
        foyer2.setIdFoyer(2L);
        foyer2.setNomFoyer("Foyer2");

        when(foyerService.retrieveAllFoyers()).thenReturn(List.of(foyer1, foyer2));

        mockMvc.perform(get("/api/v1/foyers/retrieve-all-foyers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomFoyer").value("Foyer1"))
                .andExpect(jsonPath("$[1].nomFoyer").value("Foyer2"));

        verify(foyerService, times(1)).retrieveAllFoyers();
    }

    // Test for deleting a Foyer
    @Test
     void testDeleteFoyer() throws Exception {
        mockMvc.perform(delete("/api/v1/foyers/delete-foyer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Foyer with ID 1 has been deleted successfully."));

        verify(foyerService, times(1)).deleteFoyer(1L);
    }

    // Test for unassigning a Universite from a Foyer
    @Test
     void testUnassignUniversiteFromFoyer() throws Exception {
        mockMvc.perform(put("/api/v1/foyers/unassign-universite/1"))
                .andExpect(status().isNoContent());

        verify(foyerService, times(1)).unassignUniversiteFromFoyer(1L);
    }

    // Test for adding a Bloc to a Foyer
    @Test
     void testAddBlocToFoyer() throws Exception {
        mockMvc.perform(put("/api/v1/foyers/1/add-bloc")
                        .param("idBloc", "2"))
                .andExpect(status().isNoContent());

        verify(foyerService, times(1)).addBlocToFoyer(1L, 2L);
    }

    // Test for deleting a Foyer and its associated Blocs
    @Test
     void testDeleteFoyerAndBlocs() throws Exception {
        mockMvc.perform(delete("/api/v1/foyers/delete-foyer-and-blocs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Foyer with ID 1 and all associated blocs have been deleted successfully."));

        verify(foyerService, times(1)).deleteFoyerAndBlocs(1L);
    }

    // Test for removing a Bloc from a Foyer
    @Test
     void testRemoveBlocFromFoyer() throws Exception {
        mockMvc.perform(put("/api/v1/foyers/1/remove-bloc")
                        .param("idBloc", "2"))
                .andExpect(status().isNoContent());

        verify(foyerService, times(1)).removeBlocFromFoyer(1L, 2L);
    }

    // Test for retrieving Foyers without any Blocs
    @Test
     void testGetFoyersWithoutBlocs() throws Exception {
        FoyerDTO foyerDTO = new FoyerDTO();
        foyerDTO.setIdFoyer(1L);
        foyerDTO.setNomFoyer("Foyer1");

        when(foyerService.getFoyersWithoutBlocs()).thenReturn(List.of(foyerDTO));

        mockMvc.perform(get("/api/v1/foyers/retrieve-foyers-without-blocs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomFoyer").value("Foyer1"));

        verify(foyerService, times(1)).getFoyersWithoutBlocs();
    }
}
