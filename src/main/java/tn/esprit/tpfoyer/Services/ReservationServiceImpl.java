package tn.esprit.tpfoyer.Services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.Entities.ChambreDTO;
import tn.esprit.tpfoyer.Entities.EtudiantDTO;
import tn.esprit.tpfoyer.Entities.Reservation;
import tn.esprit.tpfoyer.Entities.ReservationDTO;
import tn.esprit.tpfoyer.Exception.ReservationException;
import tn.esprit.tpfoyer.FeignClient.ChambreClient;
import tn.esprit.tpfoyer.FeignClient.EtudiantClient;
import tn.esprit.tpfoyer.Repository.ReservationRepository;

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
            throw new ReservationException("Etudiant not found with ID: " + idEtudiant);
        }

        ChambreDTO chambre = chambreClient.getChambreById(idChambre);
        if (chambre == null) {
            throw new ReservationException("Chambre not found with ID: " + idChambre);
        }

        Optional<Reservation> existingReservationForEtudiant = reservationRepository
                .findByIdEtudiantAndAnneeUniversitaireAndEstValideTrue(idEtudiant, anneeUniversitaire);
        if (existingReservationForEtudiant.isPresent()) {
            throw new ReservationException("Etudiant already has an active reservation for the selected academic year");
        }

        int chambreReservationCount = reservationRepository.countByChambreAndAnneeUniversitaire(idChambre, anneeUniversitaire);
        if (chambreReservationCount >= 2) {
            throw new ReservationException("Chambre already has the maximum number of reservations for the selected academic year");
        }

        Reservation reservation = new Reservation();
        reservation.setIdReservation(java.util.UUID.randomUUID().toString());
        reservation.setAnneeUniversitaire(anneeUniversitaire);
        reservation.setEstValide(true);
        reservation.setIdEtudiant(idEtudiant);
        reservation.setIdChambre(idChambre);

        Reservation savedReservation = reservationRepository.save(reservation);
        etudiant.getIdReservations().add(savedReservation.getIdReservation());
        etudiantClient.updateEtudiantReservations(idEtudiant, etudiant.getIdReservations());

        return savedReservation;
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
