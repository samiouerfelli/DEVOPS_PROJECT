package tn.esprit.tpfoyer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;

@Service
public class EtudiantServiceImpl implements IEtudiantService {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Override
    public Etudiant addEtudiant(Etudiant etudiant) {
        // Check if CIN is unique before saving
        if (etudiantRepository.findEtudiantByCinEtudiant(etudiant.getCinEtudiant()) != null) {
            throw new IllegalArgumentException("CIN already exists. Each student must have a unique CIN.");
        }
        return etudiantRepository.save(etudiant);
    }

    @Override
    public List<Etudiant> retrieveAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @Override
    public Etudiant retrieveEtudiant(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant).orElse(null);
    }

    @Override
    public Etudiant modifyEtudiant(Etudiant etudiant) {
        Etudiant existingEtudiant = etudiantRepository.findById(etudiant.getIdEtudiant())
                .orElseThrow(() -> new IllegalArgumentException("Etudiant not found"));

        // Check if CIN is unique when modifying, ignoring current student
        Etudiant etudiantWithSameCin = etudiantRepository.findEtudiantByCinEtudiant(etudiant.getCinEtudiant());
        if (etudiantWithSameCin != null && etudiantWithSameCin.getIdEtudiant() != etudiant.getIdEtudiant()) {
            throw new IllegalArgumentException("CIN already exists. Each student must have a unique CIN.");
        }

        // Set fields to update
        existingEtudiant.setNomEtudiant(etudiant.getNomEtudiant());
        existingEtudiant.setPrenomEtudiant(etudiant.getPrenomEtudiant());
        existingEtudiant.setCinEtudiant(etudiant.getCinEtudiant());
        existingEtudiant.setDateNaissance(etudiant.getDateNaissance());

        // Profile picture update if provided
        if (etudiant.getProfilePicture() != null) {
            existingEtudiant.setProfilePicture(etudiant.getProfilePicture());
        }

        return etudiantRepository.save(existingEtudiant);
    }


    @Override
    public void removeEtudiant(Long idEtudiant) {
        etudiantRepository.deleteById(idEtudiant);
    }

    @Override
    public Etudiant recupererEtudiantParCin(String cin) { // Keep as String
        return etudiantRepository.findEtudiantByCinEtudiant(cin);
    }
}
