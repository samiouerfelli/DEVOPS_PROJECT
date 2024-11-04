package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;
import tn.esprit.tpfoyer.control.EtudiantRestController;

import java.util.Collections;
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
    void testAddEtudiantValidData() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);

        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "John")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "12345678")
                        .param("dateNaissance", "01/01/2000"))
                .andExpect(status().isCreated());

        verify(etudiantService, times(1)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testAddEtudiantInvalidCin() throws Exception {
        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "John")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "12345")
                        .param("dateNaissance", "01/01/2000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CIN must be exactly 8 digits long and numeric."));

        verify(etudiantService, times(0)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testAddEtudiantEmptyFields() throws Exception {
        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "")
                        .param("prenomEtudiant", "")
                        .param("cinEtudiant", "")
                        .param("dateNaissance", ""))
                .andExpect(status().isBadRequest());

        verify(etudiantService, times(0)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testAddEtudiantDuplicateCin() throws Exception {
        when(etudiantService.recupererEtudiantParCin("12345678")).thenReturn(new Etudiant());

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "John")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "12345678")
                        .param("dateNaissance", "01/01/2000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CIN already exists. Each student must have a unique CIN."));

        verify(etudiantService, times(0)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testModifyEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(etudiant);
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(put("/etudiant/modify-etudiant/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "Jane")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "87654321")
                        .param("dateNaissance", "02/02/2001"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testModifyEtudiantNotFound() throws Exception {
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(null);

        mockMvc.perform(put("/etudiant/modify-etudiant/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("nomEtudiant", "Jane"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Etudiant not found."));

        verify(etudiantService, times(0)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testRetrieveAllEtudiants() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        List<Etudiant> etudiants = Collections.singletonList(etudiant);

        when(etudiantService.retrieveAllEtudiants()).thenReturn(etudiants);

        mockMvc.perform(get("/etudiant/retrieve-all-etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(etudiantService, times(1)).retrieveAllEtudiants();
    }

    @Test
    void testRetrieveEtudiantById() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);

        when(etudiantService.retrieveEtudiant(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1));

        verify(etudiantService, times(1)).retrieveEtudiant(1L);
    }

    @Test
    void testRetrieveEtudiantByIdNotFound() throws Exception {
        when(etudiantService.retrieveEtudiant(1L)).thenReturn(null);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/1"))
                .andExpect(status().isNotFound());

        verify(etudiantService, times(1)).retrieveEtudiant(1L);
    }

    @Test
    void testRetrieveEtudiantByCin() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant("12345678");

        when(etudiantService.recupererEtudiantParCin("12345678")).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cinEtudiant").value("12345678"));

        verify(etudiantService, times(1)).recupererEtudiantParCin("12345678");
    }

    @Test
    void testRetrieveEtudiantByCinNotFound() throws Exception {
        when(etudiantService.recupererEtudiantParCin("12345678")).thenReturn(null);

        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/12345678"))
                .andExpect(status().isNotFound());

        verify(etudiantService, times(1)).recupererEtudiantParCin("12345678");
    }

    @Test
    void testRemoveEtudiant() throws Exception {
        doNothing().when(etudiantService).removeEtudiant(1L);

        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isNoContent());

        verify(etudiantService, times(1)).removeEtudiant(1L);
    }
}
