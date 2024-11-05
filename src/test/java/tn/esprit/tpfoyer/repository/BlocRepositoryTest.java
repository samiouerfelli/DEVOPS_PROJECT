package tn.esprit.tpfoyer.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import tn.esprit.tpfoyer.entity.Bloc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
 class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    @Autowired
    private EntityManager entityManager;

    private Bloc bloc;

    @BeforeEach
    public void setUp() {
        bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(75);
        entityManager.persist(bloc);
        entityManager.flush();
    }

    @Test
     void testFindAllByCapaciteBlocGreaterThan() {
        List<Bloc> blocs = blocRepository.findAllByCapaciteBlocGreaterThan(50);
        assertThat(blocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindAllByNomBlocStartingWith() {
        List<Bloc> blocs = blocRepository.findAllByNomBlocStartingWith("Bl");
        assertThat(blocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindAllByNomBlocAndCapaciteBloc() {
        List<Bloc> blocs = blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 75);
        assertThat(blocs)
                .isNotEmpty()
                .contains(bloc);
    }

    @Test
     void testFindByNomBloc() {
        Bloc foundBloc = blocRepository.findByNomBloc("Bloc A");
        assertThat(foundBloc)
                .isNotNull()
                .extracting(Bloc::getNomBloc)
                .isEqualTo(bloc.getNomBloc());
    }

    @Test
     void testFindBlocByNomBlocAndCapaciteBlocGreaterThan() {
        Bloc foundBloc = blocRepository.findBlocByNomBlocAndCapaciteBlocGreaterThan("Bloc A", 50);
        assertThat(foundBloc)
                .isNotNull()
                .extracting(Bloc::getNomBloc)
                .isEqualTo(bloc.getNomBloc());
    }

    @Test
     void testFindAllByFoyerIsNull() {
        List<Bloc> nonAffectedBlocs = blocRepository.findAllByFoyerIsNull();
        assertThat(nonAffectedBlocs)
                .contains(bloc);
    }

    @Test
     void testDeleteBloc() {
        blocRepository.delete(bloc);
        Optional<Bloc> deletedBloc = blocRepository.findById(bloc.getIdBloc());
        assertThat(deletedBloc)
                .isNotPresent();
    }

    @Test
     void testUpdateBloc() {
        bloc.setNomBloc("Updated Bloc");
        Bloc updatedBloc = blocRepository.save(bloc);
        assertThat(updatedBloc)
                .extracting(Bloc::getNomBloc)
                .isEqualTo("Updated Bloc");
    }
}
