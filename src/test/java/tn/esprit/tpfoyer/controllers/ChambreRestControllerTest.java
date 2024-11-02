package tn.esprit.tpfoyer.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.control.ChambreRestController;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.service.IChambreService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChambreRestControllerTest {

    @InjectMocks
    private ChambreRestController chambreRestController;

    @Mock
    private IChambreService chambreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetChambres() {
        // Arrange
        List<Chambre> chambreList = new ArrayList<>();
        chambreList.add(new Chambre());
        when(chambreService.retrieveAllChambres()).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreRestController.getChambres();

        // Assert
        assertEquals(1, result.size());
        verify(chambreService, times(1)).retrieveAllChambres();
    }

    @Test
    void testRetrieveChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        when(chambreService.retrieveChambre(1L)).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.retrieveChambre(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreService, times(1)).retrieveChambre(1L);
    }

    @Test
    void testAddChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreService.addChambre(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.addChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreService, times(1)).addChambre(chambre);
    }

    @Test
    void testRemoveChambre() {
        // Act
        chambreRestController.removeChambre(1L);

        // Assert
        verify(chambreService, times(1)).removeChambre(1L);
    }

    @Test
    void testModifyChambre() {
        // Arrange
        Chambre chambre = new Chambre();
        when(chambreService.modifyChambre(any(Chambre.class))).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.modifyChambre(chambre);

        // Assert
        assertNotNull(result);
        verify(chambreService, times(1)).modifyChambre(chambre);
    }

    @Test
    void testTrouverChambresSelonTC() {
        // Arrange
        List<Chambre> chambreList = new ArrayList<>();
        chambreList.add(new Chambre());
        when(chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE)).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreRestController.trouverChSelonTC(TypeChambre.SIMPLE);

        // Assert
        assertEquals(1, result.size());
        verify(chambreService, times(1)).recupererChambresSelonTyp(TypeChambre.SIMPLE);
    }

    @Test
    void testTrouverChSelonEtudiant() {
        // Arrange
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        when(chambreService.trouverchambreSelonEtudiant(123456)).thenReturn(chambre);

        // Act
        Chambre result = chambreRestController.trouverChSelonEt(123456);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreService, times(1)).trouverchambreSelonEtudiant(123456);
    }
}
