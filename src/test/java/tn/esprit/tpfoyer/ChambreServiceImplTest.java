package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;
import tn.esprit.tpfoyer.service.ChambreServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChambreServiceImplTest {

    @InjectMocks
    private ChambreServiceImpl chambreService;

    @Mock
    private ChambreRepository chambreRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllChambres() {
        // Arrange
        List<Chambre> chambreList = new ArrayList<>();
        chambreList.add(new Chambre());
        when(chambreRepository.findAll()).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreService.retrieveAllChambres();

        // Assert
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        // Act
        Chambre result = chambreService.retrieveChambre(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    void testAddChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreService.addChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testModifyChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreService.modifyChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testRemoveChambre() {
        // Act
        chambreService.removeChambre(1L);

        // Assert
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRecupererChambresSelonTyp() {
        // Arrange
        List<Chambre> chambreList = new ArrayList<>();
        chambreList.add(new Chambre());
        when(chambreRepository.findAllByTypeC(TypeChambre.SIMPLE)).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);

        // Assert
        assertEquals(1, result.size());
        verify(chambreRepository, times(1)).findAllByTypeC(TypeChambre.SIMPLE);
    }

    @Test
    void testTrouverchambreSelonEtudiant() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreRepository.trouverChselonEt(12345)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.trouverchambreSelonEtudiant(12345);

        // Assert
        assertNotNull(result);
        verify(chambreRepository, times(1)).trouverChselonEt(12345);
    }
}
