package tn.esprit.tpfoyer.controllers;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
                        .content("{\"nomEtudiant\":\"John\", \"prenomEtudiant\":\"Doe\", \"cinEtudiant\":12345678}"))
                .andExpect(status().isCreated());  // Change to isCreated()

        verify(etudiantService, times(1)).addEtudiant(any(Etudiant.class));
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
    void testRetrieveEtudiantById() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).retrieveEtudiant(anyLong());
    }

    @Test
    void testRetrieveEtudiantByCin() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.recupererEtudiantParCin(anyLong())).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/12345678"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).recupererEtudiantParCin(anyLong());
    }

    @Test
    void testDeleteEtudiant() throws Exception {
        doNothing().when(etudiantService).removeEtudiant(anyLong());

        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isNoContent());  // Change to isNoContent()

        verify(etudiantService, times(1)).removeEtudiant(anyLong());
    }

    @Test
    void testModifyEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(put("/etudiant/modify-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"Jane\", \"prenomEtudiant\":\"Doe\", \"cinEtudiant\":87654321}"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testRetrieveNonExistentEtudiant() throws Exception {
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(null);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddEtudiantWithInvalidData() throws Exception {
        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"\", \"prenomEtudiant\":\"\", \"cinEtudiant\":12345678}"))
                .andExpect(status().isBadRequest()); // Change expected status to 400
    }

}
