package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tn.esprit.tpfoyer.RestController.BlocRestController;
import tn.esprit.tpfoyer.Services.BlocServiceImpl;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.BlocDTO;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
public class BlocRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BlocServiceImpl blocService;

    @InjectMocks
    private BlocRestController blocRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(blocRestController).build();
    }

    ////////////////////// Foyers ////////////////////////

    @Test
    public void testAddBlocAndAssignToFoyer() throws Exception {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);

        when(blocService.addBlocAndAssignToFoyer(any(Bloc.class), eq(1L))).thenReturn(bloc);

        mockMvc.perform(post("/api/v1/blocs/add-bloc/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomBloc\":\"Bloc A\",\"capaciteBloc\":50}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idBloc").value(1L));

        verify(blocService, times(1)).addBlocAndAssignToFoyer(any(Bloc.class), eq(1L));
    }

    @Test
    public void testDeleteBlocsByFoyerId() throws Exception {
        mockMvc.perform(delete("/api/v1/blocs/delete-by-foyer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("All blocs associated with Foyer ID 1 have been deleted successfully."));

        verify(blocService, times(1)).deleteBlocsByFoyerId(1L);
    }

    @Test
    public void testDeleteBlocAndRemoveFromFoyer() throws Exception {
        mockMvc.perform(delete("/api/v1/blocs/delete-bloc-foyer/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bloc with ID 1 has been deleted successfully, and it was removed from the associated Foyer."));

        verify(blocService, times(1)).deleteBlocAndRemoveFromFoyer(1L);
    }

    @Test
    public void testGetBlocsByFoyerId() throws Exception {
        List<BlocDTO> blocs = new ArrayList<>();
        BlocDTO blocDTO = new BlocDTO();
        blocDTO.setIdFoyer(1L);
        blocs.add(blocDTO);

        when(blocService.getBlocsByFoyerId(1L)).thenReturn(blocs);

        mockMvc.perform(get("/api/v1/blocs/retrieve-by-foyer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFoyer").value(1L));

        verify(blocService, times(1)).getBlocsByFoyerId(1L);
    }

    @Test
    public void testGetBlocById() throws Exception {
        BlocDTO blocDTO = new BlocDTO();
        blocDTO.setIdBloc(1L);

        when(blocService.retrieveBloc(1L)).thenReturn(blocDTO);

        mockMvc.perform(get("/api/v1/blocs/retrieve-bloc/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idBloc").value(1L));

        verify(blocService, times(1)).retrieveBloc(1L);
    }

    ////////////////////// Chambres ////////////////////////

    @Test
    public void testAddChambreToBloc() throws Exception {
        mockMvc.perform(put("/api/v1/blocs/1/add-chambre/2"))
                .andExpect(status().isNoContent());

        verify(blocService, times(1)).addChambreToBloc(1L, 2L);
    }

    @Test
    public void testDeleteBlocAndChambres() throws Exception {
        mockMvc.perform(delete("/api/v1/blocs/delete-bloc-chambres/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bloc with ID 1 and all associated chambres have been deleted successfully."));

        verify(blocService, times(1)).deleteBlocAndChambres(1L);
    }

    @Test
    public void testRemoveChambreFromBloc() throws Exception {
        mockMvc.perform(put("/api/v1/blocs/1/remove-chambre/2"))
                .andExpect(status().isNoContent());

        verify(blocService, times(1)).removeChambreFromBloc(1L, 2L);
    }
}
