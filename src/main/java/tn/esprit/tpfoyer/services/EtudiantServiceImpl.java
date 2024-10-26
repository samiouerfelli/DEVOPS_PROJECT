package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.entities.EtudiantDTO;
import tn.esprit.tpfoyer.exception.EtudiantNotFoundException;
import tn.esprit.tpfoyer.repository.EtudiantRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl {

    private final EtudiantRepository etudiantRepository;
    private static final String ETUDIANT_NOT_FOUND_MESSAGE = "Etudiant not found with ID: ";

    public EtudiantDTO convertToDto(Etudiant etudiant) {
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.setIdEtudiant(etudiant.getIdEtudiant());
        etudiantDTO.setNomEtudiant(etudiant.getNomEtudiant());
        etudiantDTO.setPrenomEtudiant(etudiant.getPrenomEtudiant());
        etudiantDTO.setCinEtudiant(etudiant.getCinEtudiant());
        etudiantDTO.setDateNaissance(etudiant.getDateNaissance());
        etudiantDTO.setIdReservations(etudiant.getIdReservations());
        return etudiantDTO;
    }

    ///////////////// Etudiant //////////////////

    public Etudiant addEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public Etudiant getEtudiantById(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new EtudiantNotFoundException(ETUDIANT_NOT_FOUND_MESSAGE + idEtudiant));
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant updateEtudiant(Long idEtudiant, Etudiant updatedEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new EtudiantNotFoundException(ETUDIANT_NOT_FOUND_MESSAGE + idEtudiant));

        etudiant.setNomEtudiant(updatedEtudiant.getNomEtudiant());
        etudiant.setPrenomEtudiant(updatedEtudiant.getPrenomEtudiant());
        etudiant.setCinEtudiant(updatedEtudiant.getCinEtudiant());
        etudiant.setDateNaissance(updatedEtudiant.getDateNaissance());
        etudiant.setIdReservations(updatedEtudiant.getIdReservations());

        return etudiantRepository.save(etudiant);
    }

    public void deleteEtudiant(Long idEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new EtudiantNotFoundException(ETUDIANT_NOT_FOUND_MESSAGE + idEtudiant));

        etudiantRepository.delete(etudiant);
    }

    public void updateEtudiantReservations(Long idEtudiant, List<String> idReservations) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new EtudiantNotFoundException(ETUDIANT_NOT_FOUND_MESSAGE + idEtudiant));

        etudiant.setIdReservations(idReservations);
        etudiantRepository.save(etudiant);
    }
}
