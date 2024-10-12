package tn.esprit.tpfoyer.blocservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlocRepository extends JpaRepository<Bloc, Long> {
    List<Bloc> findAllByNomBlocAndCapaciteBloc (String nom , long capacite );
    List<Bloc> findAllByIdFoyerIsNull();

}
