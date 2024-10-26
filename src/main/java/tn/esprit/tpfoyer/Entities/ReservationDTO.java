package tn.esprit.tpfoyer.Entities;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    String idReservation;
    Date anneeUniversitaire;
    Boolean estValide;
    Long idEtudiant;  // Assuming a Reservation is linked to a Student
    Long idChambre;   // Assuming a Reservation is linked to a Chambre
}
