package tn.esprit.tpfoyer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.entities.Universite;
import tn.esprit.tpfoyer.restcontroller.UniversiteRestController;
import tn.esprit.tpfoyer.services.UniversiteServiceImpl;
import tn.esprit.tpfoyer.entities.UniversiteDTO;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversiteRestController.class)
 class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversiteServiceImpl universiteService;

    @Test
     void testAddUniversite() throws Exception {
        Universite newUniversite = new Universite();
        newUniversite.setIdUniversite(1L);
        newUniversite.setNomUniversite("Test University");
        newUniversite.setAdresse("Test Address");

        when(universiteService.addUniversite(Mockito.any())).thenReturn(newUniversite);

        mockMvc.perform(post("/api/v1/universites/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomUniversite\":\"Test University\",\"adresse\":\"Test Address\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomUniversite").value("Test University"))
                .andExpect(jsonPath("$.adresse").value("Test Address"));
    }

    @Test
     void testAssignFoyerToUniversite() throws Exception {
        mockMvc.perform(put("/api/v1/universites/1/assign-foyer")
                        .param("idFoyer", "2"))
                .andExpect(status().isNoContent());
    }

    @Test
     void testRetrieveUniversite() throws Exception {
        UniversiteDTO mockUniversite = new UniversiteDTO();
        mockUniversite.setIdUniversite(1L);
        mockUniversite.setNomUniversite("Test University");
        mockUniversite.setAdresse("Test Address");

        when(universiteService.retrieveUniversite(anyLong())).thenReturn(mockUniversite);

        mockMvc.perform(get("/api/v1/universites/retrieve-universite/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("Test University"))
                .andExpect(jsonPath("$.adresse").value("Test Address"));
    }

    @Test
     void testRetrieveAllUniversites() throws Exception {
        UniversiteDTO universite1 = new UniversiteDTO();
        universite1.setIdUniversite(1L);
        universite1.setNomUniversite("University A");

        UniversiteDTO universite2 = new UniversiteDTO();
        universite2.setIdUniversite(2L);
        universite2.setNomUniversite("University B");

        when(universiteService.retrieveAllUniversites()).thenReturn(List.of(universite1, universite2));

        mockMvc.perform(get("/api/v1/universites/retrieve-all-universites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniversite").value("University A"))
                .andExpect(jsonPath("$[1].nomUniversite").value("University B"));
    }

    @Test
     void testDeleteUniversite() throws Exception {
        mockMvc.perform(delete("/api/v1/universites/delete-universite/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Universite with ID 1 has been deleted successfully."));
    }

    @Test
     void testUnassignFoyerFromUniversite() throws Exception {
        mockMvc.perform(put("/api/v1/universites/unassign-foyer/1"))
                .andExpect(status().isNoContent());
    }

    @Test
     void testGetUniversitesWithoutFoyer() throws Exception {
        UniversiteDTO universiteDTO = new UniversiteDTO();
        universiteDTO.setIdUniversite(1L);
        universiteDTO.setNomUniversite("University Without Foyer");

        when(universiteService.getUniversitesWithoutFoyer()).thenReturn(List.of(universiteDTO));

        mockMvc.perform(get("/api/v1/universites/retrieve-universites-without-foyer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniversite").value("University Without Foyer"));
    }

    @Test
     void testUpdateUniversite() throws Exception {
        UniversiteDTO universiteDTO = new UniversiteDTO();
        universiteDTO.setIdUniversite(1L);
        universiteDTO.setNomUniversite("Updated University");

        when(universiteService.updateUniversite(anyLong(), Mockito.any(UniversiteDTO.class))).thenReturn(universiteDTO);

        mockMvc.perform(put("/api/v1/universites/update-universite/{idUniversite}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomUniversite\": \"Updated University\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("Updated University"));
    }
}
