package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.control.EtudiantRestController;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EtudiantRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IEtudiantService etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(etudiantRestController).build();
    }

    @Test
    void testAddEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"John\", \"prenomEtudiant\":\"Doe\"}"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testAddEtudiantBadRequest() throws Exception {
        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"\"}")) // Invalid request
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRetrieveAllEtudiants() throws Exception {
        Etudiant etudiant1 = new Etudiant();
        Etudiant etudiant2 = new Etudiant();
        List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

        when(etudiantService.retrieveAllEtudiants()).thenReturn(etudiants);

        mockMvc.perform(get("/etudiant/retrieve-all-etudiants"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).retrieveEtudiant(anyLong());
    }

    @Test
    void testRetrieveEtudiantNotFound() throws Exception {
        when(etudiantService.retrieveEtudiant(anyLong())).thenThrow(new RuntimeException("Etudiant not found"));

        mockMvc.perform(get("/etudiant/retrieve-etudiant/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(put("/etudiant/update-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"nomEtudiant\":\"Jane\", \"prenomEtudiant\":\"Doe\"}"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testUpdateEtudiantNotFound() throws Exception {
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenThrow(new RuntimeException("Etudiant not found"));

        mockMvc.perform(put("/etudiant/update-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":999, \"nomEtudiant\":\"Jane\", \"prenomEtudiant\":\"Doe\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteEtudiant() throws Exception {
        Long id = 1L;
        doNothing().when(etudiantService).removeEtudiant(id);

        mockMvc.perform(delete("/etudiant/delete-etudiant/" + id))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).removeEtudiant(id);
    }

    @Test
    void testDeleteEtudiantNotFound() throws Exception {
        doThrow(new RuntimeException("Etudiant not found")).when(etudiantService).removeEtudiant(999L);

        mockMvc.perform(delete("/etudiant/delete-etudiant/999"))
                .andExpect(status().isNotFound());
    }
}
