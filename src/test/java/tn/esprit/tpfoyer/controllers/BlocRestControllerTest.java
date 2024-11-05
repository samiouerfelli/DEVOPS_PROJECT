package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.BlocRestController;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.service.IBlocService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BlocRestControllerTest {
    private static final String BLOC = "Bloc1";

    @InjectMocks
    private BlocRestController blocRestController;

    @Mock
    private IBlocService blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBlocs() {
        // Arrange
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(new Bloc());
        when(blocService.retrieveAllBlocs()).thenReturn(blocList);

        // Act
        List<Bloc> result = blocRestController.getBlocs();

        // Assert
        assertEquals(1, result.size());
        verify(blocService, times(1)).retrieveAllBlocs();
    }

    @Test
    void testRetrieveBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        when(blocService.retrieveBloc(1L)).thenReturn(bloc);

        // Act
        Bloc result = blocRestController.retrieveBloc(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
        verify(blocService, times(1)).retrieveBloc(1L);
    }

    @Test
    void testAddBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocService.addBloc(any(Bloc.class))).thenReturn(bloc);

        // Act
        Bloc result = blocRestController.addBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocService, times(1)).addBloc(bloc);
    }

    @Test
    void testRemoveBloc() {
        // Act
        blocRestController.removeBloc(1L);

        // Assert
        verify(blocService, times(1)).removeBloc(1L);
    }

    @Test
    void testModifyBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocService.modifyBloc(any(Bloc.class))).thenReturn(bloc);

        // Act
        Bloc result = blocRestController.modifyBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocService, times(1)).modifyBloc(bloc);
    }

    @Test
    void testGetBlocswirhoutFoyer() {
        // Arrange
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(new Bloc());
        when(blocService.trouverBlocsSansFoyer()).thenReturn(blocList);

        // Act
        List<Bloc> result = blocRestController.getBlocswirhoutFoyer();

        // Assert
        assertEquals(1, result.size());
        verify(blocService, times(1)).trouverBlocsSansFoyer();
    }

    @Test
    void testRecuperBlocsParNomEtCap() {
        // Arrange
        List<Bloc> blocList = new ArrayList<>();
        blocList.add(new Bloc());
        when(blocService.trouverBlocsParNomEtCap(BLOC, 100)).thenReturn(blocList);

        // Act
        List<Bloc> result = blocRestController.recuperBlocsParNomEtCap(BLOC, 100);

        // Assert
        assertEquals(1, result.size());
        verify(blocService, times(1)).trouverBlocsParNomEtCap(BLOC, 100);
    }
}