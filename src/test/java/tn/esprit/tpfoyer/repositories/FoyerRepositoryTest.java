package tn.esprit.tpfoyer.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.FoyerRepository;

import java.util.HashSet;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test") // Use a test profile to load test-specific configurations
 class FoyerRepositoryTest {

    @Autowired
    private FoyerRepository foyerRepository;

    private Foyer foyer;

    @BeforeEach
    public void setUp() {
        foyer = new Foyer();
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100); // Set a sample capacity
        foyer.setUniversite(new Universite()); // Assuming Universite has a no-arg constructor
        foyer.setBlocs(new HashSet<>()); // Initialize with an empty set
    }

    @Test
     void testSaveFoyer() {
        Foyer savedFoyer = foyerRepository.save(foyer);
        assertThat(savedFoyer).isNotNull();
        assertThat(savedFoyer.getIdFoyer()).isNotNull();
        assertThat(savedFoyer.getNomFoyer()).isEqualTo("Test Foyer");
        assertThat(savedFoyer.getCapaciteFoyer()).isEqualTo(100);
    }

    @Test
     void testFindById() {
        Foyer savedFoyer = foyerRepository.save(foyer);
        Optional<Foyer> foundFoyer = foyerRepository.findById(savedFoyer.getIdFoyer());
        assertThat(foundFoyer).isPresent();
        assertThat(foundFoyer.get().getNomFoyer()).isEqualTo("Test Foyer");
        assertThat(foundFoyer.get().getCapaciteFoyer()).isEqualTo(100);
    }

    @Test
     void testUpdateFoyer() {
        Foyer savedFoyer = foyerRepository.save(foyer);
        savedFoyer.setNomFoyer("Updated Foyer");
        savedFoyer.setCapaciteFoyer(200);
        Foyer updatedFoyer = foyerRepository.save(savedFoyer);
        assertThat(updatedFoyer.getNomFoyer()).isEqualTo("Updated Foyer");
        assertThat(updatedFoyer.getCapaciteFoyer()).isEqualTo(200);
    }

    @Test
     void testDeleteFoyer() {
        Foyer savedFoyer = foyerRepository.save(foyer);
        foyerRepository.delete(savedFoyer);
        Optional<Foyer> foundFoyer = foyerRepository.findById(savedFoyer.getIdFoyer());
        assertThat(foundFoyer).isNotPresent();
    }

    @Test
     void testFindAllFoyers() {
        Foyer savedFoyer1 = new Foyer();
        savedFoyer1.setNomFoyer("Foyer 1");
        savedFoyer1.setCapaciteFoyer(150);
        foyerRepository.save(savedFoyer1);

        Foyer savedFoyer2 = new Foyer();
        savedFoyer2.setNomFoyer("Foyer 2");
        savedFoyer2.setCapaciteFoyer(250);
        foyerRepository.save(savedFoyer2);

        assertThat(foyerRepository.findAll()).hasSize(2);
    }

    @Test
     void testFoyerWithBlocs() {
        Foyer savedFoyer = foyerRepository.save(foyer);
        Bloc bloc = new Bloc(); // Create a new Bloc instance
        bloc.setFoyer(savedFoyer); // Link the Bloc to the Foyer
        savedFoyer.getBlocs().add(bloc); // Add the Bloc to the Foyer's set
        foyerRepository.save(savedFoyer); // Save the Foyer again to persist the changes

        Foyer foundFoyer = foyerRepository.findById(savedFoyer.getIdFoyer()).orElse(null);
        assertThat(foundFoyer).isNotNull();
        assertThat(foundFoyer.getBlocs()).isNotEmpty();
        assertThat(foundFoyer.getBlocs()).hasSize(1);
    }

    @Test
     void testFoyerWithUniversite() {
        Universite universite = new Universite(); // Create a new Universite instance
        universite.setFoyer(foyer); // Link the Universite to the Foyer
        foyer.setUniversite(universite); // Set the Universite in the Foyer

        Foyer savedFoyer = foyerRepository.save(foyer);
        assertThat(savedFoyer.getUniversite()).isNotNull();
    }
}
