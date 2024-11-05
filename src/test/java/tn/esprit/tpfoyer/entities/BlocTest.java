package tn.esprit.tpfoyer.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.Foyer;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;

 class BlocTest {
    private static final String BLOC_A = "Bloc A";

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        bloc = new Bloc();
    }

    @Test
    void testDefaultConstructor() {
        // Assert that default constructor initializes fields to default values
        assertEquals(0, bloc.getIdBloc());
        assertNull(bloc.getNomBloc());
        assertEquals(0, bloc.getCapaciteBloc());
        assertNull(bloc.getFoyer());
        assertNotNull(bloc.getChambres());
        assertTrue(bloc.getChambres().isEmpty());
    }

    @Test
    void testPropertyAssignments() {
        // Arrange
        bloc.setIdBloc(1L);
        bloc.setNomBloc(BLOC_A);
        bloc.setCapaciteBloc(100);

        // Create a Foyer instance for testing
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L); // Assuming a method to set Foyer ID
        bloc.setFoyer(foyer);

        // Act & Assert
        assertEquals(1L, bloc.getIdBloc());
        assertEquals(BLOC_A, bloc.getNomBloc());
        assertEquals(100, bloc.getCapaciteBloc());
        assertEquals(foyer, bloc.getFoyer());
    }

    @Test
    void testChambresAssociation() {
        // Arrange
        Chambre chambre1 = new Chambre();
        Chambre chambre2 = new Chambre();
        Set<Chambre> chambres = new HashSet<>();
        chambres.add(chambre1);
        chambres.add(chambre2);

        // Act
        bloc.setChambres(chambres);

        // Assert
        assertEquals(2, bloc.getChambres().size());
        assertTrue(bloc.getChambres().contains(chambre1));
        assertTrue(bloc.getChambres().contains(chambre2));
    }

    @Test
    void testToString() {
        // Arrange
        bloc.setIdBloc(1L);
        bloc.setNomBloc(BLOC_A);
        bloc.setCapaciteBloc(100);

        // Act
        String toStringResult = bloc.toString();

        // Assert
        assertTrue(toStringResult.contains("idBloc=1"));
        assertTrue(toStringResult.contains("nomBloc=Bloc A"));
        assertTrue(toStringResult.contains("capaciteBloc=100"));
    }
}