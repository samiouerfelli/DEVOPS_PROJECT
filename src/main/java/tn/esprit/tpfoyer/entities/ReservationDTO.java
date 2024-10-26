package tn.esprit.tpfoyer.entities;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDTO {
    String idReservation;
    Date anneeUniversitaire;
    Boolean estValide;
    Long idEtudiant;
    Long idChambre;
}
