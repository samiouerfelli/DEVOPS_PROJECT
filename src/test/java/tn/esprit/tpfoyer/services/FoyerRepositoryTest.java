package tn.esprit.tpfoyer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // This will only load JPA components and a limited context for testing repository classes
class FoyerRepositoryTest {

    @Autowired
    private FoyerRepository foyerRepository;

    @Test
    void testSaveFoyer() {
        // Arrange
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);

        // Act
        Foyer savedFoyer = foyerRepository.save(foyer);

        // Assert
        assertNotNull(savedFoyer);
        assertEquals("Test Foyer", savedFoyer.getNomFoyer());
    }

    @Test
    void testFindAllFoyers() {
        // Arrange
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);
        foyerRepository.save(foyer);

        // Act
        List<Foyer> foyers = foyerRepository.findAll();

        // Assert
        assertFalse(foyers.isEmpty());
    }
}
