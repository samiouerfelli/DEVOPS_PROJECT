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

import static org.mockito.ArgumentMatchers.*;
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
        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nomEtudiant", "John")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "12345")
                        .param("dateNaissance", "01/01/2000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CIN must be exactly 8 digits long and numeric."));

        verify(etudiantService, times(0)).addEtudiant(any(Etudiant.class));
    }

    @Test
    void testAddEtudiantDuplicateCin() throws Exception {
        when(etudiantService.recupererEtudiantParCin("12345678")).thenReturn(new Etudiant());

        mockMvc.perform(post("/etudiant/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nomEtudiant", "John")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "12345678")
                        .param("dateNaissance", "01/01/2000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CIN already exists. Each student must have a unique CIN."));

        verify(etudiantService, times(0)).addEtudiant(any(Etudiant.class));
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
        when(etudiantService.recupererEtudiantParCin("12345678")).thenReturn(etudiant);

        mockMvc.perform(get("/etudiant/retrieve-etudiant-cin/12345678"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).recupererEtudiantParCin("12345678");
    }

    @Test
    void testDeleteEtudiant() throws Exception {
        doNothing().when(etudiantService).removeEtudiant(anyLong());

        mockMvc.perform(delete("/etudiant/remove-etudiant/1"))
                .andExpect(status().isNoContent());

        verify(etudiantService, times(1)).removeEtudiant(anyLong());
    }

    @Test
    void testModifyEtudiantValidData() throws Exception {
        Etudiant etudiant = new Etudiant();
        when(etudiantService.modifyEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(put("/etudiant/modify-etudiant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nomEtudiant", "Jane")
                        .param("prenomEtudiant", "Doe")
                        .param("cinEtudiant", "87654321")
                        .param("dateNaissance", "02/02/1999"))
                .andExpect(status().isOk());

        verify(etudiantService, times(1)).modifyEtudiant(any(Etudiant.class));
    }

    @Test
    void testModifyEtudiantInvalidCin() throws Exception {
        mockMvc.perform(put("/etudiant/modify-etudiant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("cinEtudiant", "123"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CIN must be exactly 8 digits long and numeric."));
    }

    @Test
    void testRetrieveNonExistentEtudiant() throws Exception {
        when(etudiantService.retrieveEtudiant(anyLong())).thenReturn(null);

        mockMvc.perform(get("/etudiant/retrieve-etudiant/99"))
                .andExpect(status().isNotFound());

        verify(etudiantService, times(1)).retrieveEtudiant(anyLong());
    }
}
