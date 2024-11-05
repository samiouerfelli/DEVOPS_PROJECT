package tn.esprit.tpfoyer.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test") // Use a test profile to load test-specific configurations
 class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    private Bloc bloc;

    @BeforeEach
    public void setUp() {
        Foyer foyer = new Foyer(); // Create a new Foyer instance for association
        foyer.setNomFoyer("Test Foyer");
        foyer.setCapaciteFoyer(100);

        bloc = new Bloc();
        bloc.setNomBloc("Test Bloc");
        bloc.setCapaciteBloc(60);
        bloc.setFoyer(foyer); // Associate with Foyer
    }

    @Test
     void testSaveBloc() {
        Bloc savedBloc = blocRepository.save(bloc);
        assertThat(savedBloc)
                .isNotNull()
                .extracting(Bloc::getIdBloc, Bloc::getNomBloc, Bloc::getCapaciteBloc)
                .containsExactly(savedBloc.getIdBloc(), "Test Bloc", 60);
    }

    @Test
     void testFindByNomBloc() {
        blocRepository.save(bloc);
        Bloc foundBloc = blocRepository.findByNomBloc("Test Bloc");
        assertThat(foundBloc)
                .isNotNull()
                .extracting(Bloc::getNomBloc)
                .isEqualTo("Test Bloc");
    }

    @Test
     void testFindAllByCapaciteBlocGreaterThan() {
        blocRepository.save(bloc);
        List<Bloc> foundBlocs = blocRepository.findAllByCapaciteBlocGreaterThan(50);
        assertThat(foundBlocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindAllByNomBlocStartingWith() {
        blocRepository.save(bloc);
        List<Bloc> foundBlocs = blocRepository.findAllByNomBlocStartingWith("Bl");
        assertThat(foundBlocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindAllByNomBlocAndCapaciteBloc() {
        blocRepository.save(bloc);
        List<Bloc> foundBlocs = blocRepository.findAllByNomBlocAndCapaciteBloc("Test Bloc", 60);
        assertThat(foundBlocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindBlocByNomBlocAndCapaciteBlocGreaterThan() {
        blocRepository.save(bloc);
        Bloc foundBloc = blocRepository.findBlocByNomBlocAndCapaciteBlocGreaterThan("Test Bloc", 50);
        assertThat(foundBloc)
                .isNotNull()
                .extracting(Bloc::getNomBloc)
                .isEqualTo("Test Bloc");
    }

    @Test
     void testFindAllByFoyerIsNull() {
        blocRepository.save(bloc);
        // Now delete the foyer association
        bloc.setFoyer(null);
        blocRepository.save(bloc); // Save the updated Bloc

        List<Bloc> foundBlocs = blocRepository.findAllByFoyerIsNull();
        assertThat(foundBlocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testDeleteBloc() {
        Bloc savedBloc = blocRepository.save(bloc);
        blocRepository.delete(savedBloc);
        Optional<Bloc> foundBloc = blocRepository.findById(savedBloc.getIdBloc());
        assertThat(foundBloc).isNotPresent();
    }
}
