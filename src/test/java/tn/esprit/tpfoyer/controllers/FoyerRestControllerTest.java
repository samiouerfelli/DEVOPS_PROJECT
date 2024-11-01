package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.control.FoyerRestController;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.service.IFoyerService;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FoyerRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IFoyerService foyerService;

    @InjectMocks
    private FoyerRestController foyerRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(foyerRestController).build();
    }

    @Test
    void testAddFoyer() throws Exception {
        Foyer foyer = new Foyer();
        when(foyerService.addFoyer(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/add-foyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"Foyer A\", \"capaciteFoyer\":100}"))
                .andExpect(status().isCreated());

        verify(foyerService, times(1)).addFoyer(any(Foyer.class));
    }

    @Test
    void testRetrieveAllFoyers() throws Exception {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        List<Foyer> foyers = Arrays.asList(foyer1, foyer2);

        when(foyerService.retrieveAllFoyers()).thenReturn(foyers);

        mockMvc.perform(get("/foyer/retrieve-all-foyers"))
                .andExpect(status().isOk());

        verify(foyerService, times(1)).retrieveAllFoyers();
    }

    @Test
    void testRetrieveFoyerById() throws Exception {
        Foyer foyer = new Foyer();
        when(foyerService.retrieveFoyer(anyLong())).thenReturn(foyer);

        mockMvc.perform(get("/foyer/retrieve-foyer/1"))
                .andExpect(status().isOk());

        verify(foyerService, times(1)).retrieveFoyer(anyLong());
    }

    @Test
    void testDeleteFoyer() throws Exception {
        doNothing().when(foyerService).removeFoyer(anyLong());

        mockMvc.perform(delete("/foyer/remove-foyer/1"))
                .andExpect(status().isNoContent());

        verify(foyerService, times(1)).removeFoyer(anyLong());
    }

    @Test
    void testModifyFoyer() throws Exception {
        Foyer foyer = new Foyer();
        when(foyerService.modifyFoyer(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(put("/foyer/modify-foyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"Updated Foyer\", \"capaciteFoyer\":150}"))
                .andExpect(status().isOk());

        verify(foyerService, times(1)).modifyFoyer(any(Foyer.class));
    }

    @Test
    void testRetrieveNonExistentFoyer() throws Exception {
        when(foyerService.retrieveFoyer(anyLong())).thenReturn(null);

        mockMvc.perform(get("/foyer/retrieve-foyer/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddFoyerWithInvalidData() throws Exception {
        mockMvc.perform(post("/foyer/add-foyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomFoyer\":\"\", \"capaciteFoyer\":0}"))
                .andExpect(status().isBadRequest());
    }
}
