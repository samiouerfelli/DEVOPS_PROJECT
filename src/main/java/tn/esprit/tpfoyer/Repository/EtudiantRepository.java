package tn.esprit.tpfoyer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.Entities.Etudiant;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Etudiant findEtudiantByCinEtudiant(long cin);

}