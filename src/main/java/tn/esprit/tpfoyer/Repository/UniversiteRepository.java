package tn.esprit.tpfoyer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.Entities.Universite;

import java.util.List;

@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long>
{
    List<Universite> findByIdFoyerIsNull();


}
