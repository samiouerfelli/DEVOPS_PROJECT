package tn.esprit.tpfoyer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer.Entities.Universite;
import tn.esprit.tpfoyer.Entities.UniversiteDTO;
import tn.esprit.tpfoyer.RestController.UniversiteRestController;
import tn.esprit.tpfoyer.Services.UniversiteServiceImpl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversiteRestController.class)
class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniversiteServiceImpl universiteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddUniversite() throws Exception {
        Universite universite = new Universite();
        universite.setNomUniversite("New University");

        when(universiteService.addUniversite(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/api/v1/universites/add-universite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomUniversite").value("New University"));
    }

    @Test
    void testRetrieveUniversite() throws Exception {
        Universite universite = new Universite();
       UniversiteDTO u =  universiteService.convertToDto(universite);
        universite.setIdUniversite(1L);
        universite.setNomUniversite("Existing University");

        when(universiteService.retrieveUniversite(1L)).thenReturn(u);

        mockMvc.perform(get("/api/v1/universites/retrieve-universite/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("Existing University"));
    }

    @Test
    void testAssignFoyerToUniversite() throws Exception {
        mockMvc.perform(put("/api/v1/universites/1/assign-foyer")
                        .param("idFoyer", "2"))
                .andExpect(status().isNoContent());

        verify(universiteService, times(1)).assignFoyerToUniversite(1L, 2L);
    }

    @Test
    void testDeleteUniversite() throws Exception {
        mockMvc.perform(delete("/api/v1/universites/delete-universite/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Universite with ID 1 has been deleted successfully."));

        verify(universiteService, times(1)).deleteUniversite(1L);
    }
}
