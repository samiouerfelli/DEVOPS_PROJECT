package tn.esprit.tpfoyer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>

{
    Optional<Reservation> findByIdEtudiantAndAnneeUniversitaireAndEstValideTrue(Long idEtudiant, Date anneeUniversitaire);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.idChambre = :idChambre AND r.anneeUniversitaire = :anneeUniversitaire AND r.estValide = true")
    int countByChambreAndAnneeUniversitaire(@Param("idChambre") Long idChambre, @Param("anneeUniversitaire") Date anneeUniversitaire);
    List<Reservation> findByIdChambreAndAnneeUniversitaireAndEstValideTrue(Long idChambre, Date anneeUniversitaire);
    List<Reservation> findByIdEtudiant(Long idEtudiant);



}
