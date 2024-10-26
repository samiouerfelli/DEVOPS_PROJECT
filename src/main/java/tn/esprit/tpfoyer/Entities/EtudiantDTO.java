package tn.esprit.tpfoyer.Entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class EtudiantDTO {
    Long idEtudiant;
    String nomEtudiant;
    String prenomEtudiant;
    Long cinEtudiant;
    Date dateNaissance;
    List<String> idReservations = new ArrayList<>();

}