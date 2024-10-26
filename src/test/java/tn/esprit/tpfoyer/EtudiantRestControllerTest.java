package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.restcontroller.EtudiantRestController;
import tn.esprit.tpfoyer.services.EtudiantServiceImpl;
import tn.esprit.tpfoyer.entities.Etudiant;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantRestController.class)
@ExtendWith(MockitoExtension.class)
 class EtudiantRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private EtudiantServiceImpl etudiantService;

    @InjectMocks
    private EtudiantRestController etudiantRestController;

    @BeforeEach
     void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(etudiantRestController).build();
    }

    ///////////// Etudiant //////////////////

    @Test
     void testAddEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Doe");
        etudiant.setPrenomEtudiant("John");

        when(etudiantService.addEtudiant(any(Etudiant.class))).thenReturn(etudiant);

        mockMvc.perform(post("/api/v1/etudiants/add-etudiant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"Doe\",\"prenomEtudiant\":\"John\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEtudiant").value(1L))
                .andExpect(jsonPath("$.nomEtudiant").value("Doe"))
                .andExpect(jsonPath("$.prenomEtudiant").value("John"));

        verify(etudiantService, times(1)).addEtudiant(any(Etudiant.class));
    }

    @Test
     void testGetEtudiantById() throws Exception {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Doe");

        when(etudiantService.getEtudiantById(1L)).thenReturn(etudiant);

        mockMvc.perform(get("/api/v1/etudiants/retrieve-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1L))
                .andExpect(jsonPath("$.nomEtudiant").value("Doe"));

        verify(etudiantService, times(1)).getEtudiantById(1L);
    }

    @Test
     void testGetAllEtudiants() throws Exception {
        List<Etudiant> etudiants = new ArrayList<>();
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEtudiant("Doe");
        etudiants.add(etudiant);

        when(etudiantService.getAllEtudiants()).thenReturn(etudiants);

        mockMvc.perform(get("/api/v1/etudiants/retrieve-all-etudiants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEtudiant").value(1L))
                .andExpect(jsonPath("$[0].nomEtudiant").value("Doe"));

        verify(etudiantService, times(1)).getAllEtudiants();
    }

    @Test
     void testUpdateEtudiant() throws Exception {
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setIdEtudiant(1L);
        updatedEtudiant.setNomEtudiant("Doe Updated");

        when(etudiantService.updateEtudiant(eq(1L), any(Etudiant.class))).thenReturn(updatedEtudiant);

        mockMvc.perform(put("/api/v1/etudiants/update-etudiant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomEtudiant\":\"Doe Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEtudiant").value(1L))
                .andExpect(jsonPath("$.nomEtudiant").value("Doe Updated"));

        verify(etudiantService, times(1)).updateEtudiant(eq(1L), any(Etudiant.class));
    }

    @Test
     void testDeleteEtudiant() throws Exception {
        mockMvc.perform(delete("/api/v1/etudiants/delete-etudiant/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Etudiant with ID 1 has been deleted successfully."));

        verify(etudiantService, times(1)).deleteEtudiant(1L);
    }

    @Test
     void testUpdateEtudiantReservations() throws Exception {
        List<String> reservations = List.of("R1", "R2");

        mockMvc.perform(put("/api/v1/etudiants/1/update-reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[\"R1\", \"R2\"]"))
                .andExpect(status().isOk())
                .andExpect(content().string("Reservations updated successfully for Etudiant with ID: 1"));

        verify(etudiantService, times(1)).updateEtudiantReservations(1L, reservations);
    }
}
