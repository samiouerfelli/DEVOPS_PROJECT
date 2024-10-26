package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl  {

    ReservationRepository reservationRepository;
    private final EtudiantClient etudiantClient;
    private final ChambreClient chambreClient;


    private ReservationDTO convertToDto(Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setIdReservation(reservation.getIdReservation());
        reservationDTO.setAnneeUniversitaire(reservation.getAnneeUniversitaire());
        reservationDTO.setEstValide(reservation.isEstValide());
        reservationDTO.setIdEtudiant(reservation.getIdEtudiant());
        reservationDTO.setIdChambre(reservation.getIdChambre());
        return reservationDTO;
    }

    public Reservation createReservation(Long idEtudiant, Long idChambre, Date anneeUniversitaire) {
        EtudiantDTO etudiant = etudiantClient.getEtudiantById(idEtudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }

        ChambreDTO chambre = chambreClient.getChambreById(idChambre);
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }

        Optional<Reservation> existingReservationForEtudiant = reservationRepository
                .findByIdEtudiantAndAnneeUniversitaireAndEstValideTrue(idEtudiant, anneeUniversitaire);
        if (existingReservationForEtudiant.isPresent()) {
            throw new RuntimeException("Etudiant already has an active reservation for the selected academic year");
        }

        int chambreReservationCount = reservationRepository.countByChambreAndAnneeUniversitaire(idChambre, anneeUniversitaire);
        if (chambreReservationCount >= 2) {
            throw new RuntimeException("Chambre already has the maximum number of reservations for the selected academic year");
        }

        Reservation reservation = new Reservation();
        reservation.setIdReservation(java.util.UUID.randomUUID().toString());
        reservation.setAnneeUniversitaire(anneeUniversitaire);
        reservation.setEstValide(true);
        reservation.setIdEtudiant(idEtudiant);
        reservation.setIdChambre(idChambre);

        return reservationRepository.save(reservation);
    }

    public void cancelReservation(String idReservation) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setEstValide(false);
        reservationRepository.save(reservation);

        Long idChambre = reservation.getIdChambre();
        Date anneeUniversitaire = reservation.getAnneeUniversitaire();

        int remainingReservations = reservationRepository.countByChambreAndAnneeUniversitaire(idChambre, anneeUniversitaire);

        if (remainingReservations < 2) {
            chambreClient.updateChambreAvailability(idChambre, false);
        }
    }

    public Reservation getReservationById(String idReservation) {
        return reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }

    public List<Reservation> getReservationsByEtudiant(Long idEtudiant) {
        return reservationRepository.findByIdEtudiant(idEtudiant);
    }

    public List<Reservation> getReservationsByChambreAndAnnee(Long idChambre, Date anneeUniversitaire) {
        return reservationRepository.findByIdChambreAndAnneeUniversitaireAndEstValideTrue(idChambre, anneeUniversitaire);
    }





}
