package tn.esprit.tpfoyer.service;

import tn.esprit.tpfoyer.entity.Etudiant;
import java.util.List;

public interface IEtudiantService {
    Etudiant addEtudiant(Etudiant etudiant);
    List<Etudiant> retrieveAllEtudiants();
    Etudiant retrieveEtudiant(Long idEtudiant);
    Etudiant modifyEtudiant(Etudiant etudiant);
    void removeEtudiant(Long idEtudiant);
    Etudiant recupererEtudiantParCin(Long cin);
}