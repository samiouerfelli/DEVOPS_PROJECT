//package tn.esprit.tpfoyer.services;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import tn.esprit.tpfoyer.entity.Foyer;
//import tn.esprit.tpfoyer.repository.FoyerRepository;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class FoyerRepositoryTest {
//
//    @Autowired
//    private FoyerRepository foyerRepository;
//
//    @Test
//    void testSaveFoyer() {
//        // Arrange
//        Foyer foyer = new Foyer();
//        foyer.setNomFoyer("Test Foyer");
//        foyer.setCapaciteFoyer(100);
//
//        // Act
//        Foyer savedFoyer = foyerRepository.save(foyer);
//
//        // Assert
//        assertNotNull(savedFoyer);
//        assertEquals("Test Foyer", savedFoyer.getNomFoyer());
//    }
//
//    @Test
//    void testFindAllFoyers() {
//        // Arrange
//        Foyer foyer = new Foyer();
//        foyer.setNomFoyer("Test Foyer");
//        foyer.setCapaciteFoyer(100);
//        foyerRepository.save(foyer);
//
//        // Act
//        List<Foyer> foyers = foyerRepository.findAll();
//
//        // Assert
//        assertFalse(foyers.isEmpty());
//    }
//}
