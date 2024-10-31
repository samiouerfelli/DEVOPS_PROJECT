package tn.esprit.tpfoyer.service;

import tn.esprit.tpfoyer.entity.Etudiant;

import java.util.List;

public interface IEtudiantService {

    List<Etudiant> retrieveAllEtudiants();

    Etudiant retrieveEtudiant(Long etudiantId);

    Etudiant addEtudiant(Etudiant etudiant);

    void removeEtudiant(Long etudiantId);

    Etudiant modifyEtudiant(Etudiant etudiant);

    Etudiant recupererEtudiantParCin(long cin);
}
