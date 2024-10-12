package tn.esprit.tpfoyer.reservationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String>
{
    List<Reservation> findAllByAnneeUniversitaireBeforeAndEstValide(Date d, boolean b );

}
