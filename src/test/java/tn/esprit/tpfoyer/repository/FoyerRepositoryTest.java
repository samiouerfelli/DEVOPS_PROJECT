package tn.esprit.tpfoyer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import tn.esprit.tpfoyer.entity.Foyer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
 class FoyerRepositoryTest {

    @Autowired
    private FoyerRepository foyerRepository;

    @Autowired
    private EntityManager entityManager;

    private Foyer foyer;

    @BeforeEach
    public void setUp() {
        foyer = new Foyer();
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);
        entityManager.persist(foyer);
        entityManager.flush();
    }

    @Test
     void testFindById() {
        Optional<Foyer> foundFoyer = foyerRepository.findById(foyer.getIdFoyer());
        assertThat(foundFoyer).isPresent();
        assertThat(foundFoyer.get().getNomFoyer()).isEqualTo(foyer.getNomFoyer());
    }

    @Test
     void testSaveFoyer() {
        Foyer newFoyer = new Foyer();
        newFoyer.setNomFoyer("New Foyer");
        newFoyer.setCapaciteFoyer(200);

        Foyer savedFoyer = foyerRepository.save(newFoyer);
        assertThat(savedFoyer.getIdFoyer()).isNotNull();
        assertThat(savedFoyer.getNomFoyer()).isEqualTo("New Foyer");
    }

    @Test
     void testDeleteFoyer() {
        foyerRepository.delete(foyer);
        Optional<Foyer> deletedFoyer = foyerRepository.findById(foyer.getIdFoyer());
        assertThat(deletedFoyer).isNotPresent();
    }

    @Test
     void testUpdateFoyer() {
        foyer.setNomFoyer("Updated Foyer");
        Foyer updatedFoyer = foyerRepository.save(foyer);
        assertThat(updatedFoyer.getNomFoyer()).isEqualTo("Updated Foyer");
    }

    @Test
     void testCountFoyers() {
        long count = foyerRepository.count();
        assertThat(count).isEqualTo(1); // Should be 1 if only the initial foyer is present
    }
}
