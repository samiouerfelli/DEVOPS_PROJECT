package tn.esprit.tpfoyer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllBlocs() {
        // Arrange
        List<Bloc> mockList = new ArrayList<>();
        mockList.add(new Bloc());
        when(blocRepository.findAll()).thenReturn(mockList);

        // Act
        List<Bloc> result = blocService.retrieveAllBlocs();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite_ValidCapacite() {
        // Arrange
        Bloc bloc1 = new Bloc();
        bloc1.setCapaciteBloc(150);
        Bloc bloc2 = new Bloc();
        bloc2.setCapaciteBloc(75);
        List<Bloc> mockList = List.of(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(mockList);

        // Act
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(100);

        // Assert
        assertEquals(1, result.size());
        assertEquals(150, result.get(0).getCapaciteBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite_EmptyResult() {
        // Arrange
        Bloc bloc1 = new Bloc();
        bloc1.setCapaciteBloc(50);
        List<Bloc> mockList = List.of(bloc1);
        when(blocRepository.findAll()).thenReturn(mockList);

        // Act
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(100);

        // Assert
        assertTrue(result.isEmpty());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBloc_Found() {
        // Arrange
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        // Act
        Bloc result = blocService.retrieveBloc(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveBloc_NotFound() {
        // Arrange
        when(blocRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> blocService.retrieveBloc(1L));
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        // Act
        Bloc result = blocService.addBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testModifyBloc() {
        // Arrange
        Bloc bloc = new Bloc();
        when(blocRepository.save(bloc)).thenReturn(bloc);

        // Act
        Bloc result = blocService.modifyBloc(bloc);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRemoveBloc() {
        // Act
        blocService.removeBloc(1L);

        // Assert
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testTrouverBlocsSansFoyer() {
        // Arrange
        List<Bloc> mockList = new ArrayList<>();
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(mockList);

        // Act
        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void testTrouverBlocsParNomEtCap() {
        // Arrange
        List<Bloc> mockList = new ArrayList<>();
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Test", 100)).thenReturn(mockList);

        // Act
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Test", 100);

        // Assert
        assertNotNull(result);
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Test", 100);
    }
}
