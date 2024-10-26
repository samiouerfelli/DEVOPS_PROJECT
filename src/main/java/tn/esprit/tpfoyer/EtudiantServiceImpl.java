package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EtudiantServiceImpl {


    EtudiantRepository etudiantRepository;

    private EtudiantDTO convertToDto(Etudiant etudiant) {
        EtudiantDTO etudiantDTO = new EtudiantDTO();
        etudiantDTO.setIdEtudiant(etudiant.getIdEtudiant());
        etudiantDTO.setNomEtudiant(etudiant.getNomEtudiant());
        etudiantDTO.setPrenomEtudiant(etudiant.getPrenomEtudiant());
        etudiantDTO.setCinEtudiant(etudiant.getCinEtudiant());
        etudiantDTO.setDateNaissance(etudiant.getDateNaissance());

        for (String idReservation : etudiant.getIdReservations()) {
            etudiantDTO.getIdReservations().add(idReservation);
        }
        return etudiantDTO;
    }

    ///////////////// Etudiant //////////////////

    public Etudiant addEtudiant(Etudiant etudiant) {
        return etudiantRepository.save(etudiant);
    }

    public Etudiant getEtudiantById(Long idEtudiant) {
        return etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));
    }

    public List<Etudiant> getAllEtudiants() {
        return etudiantRepository.findAll();
    }

    public Etudiant updateEtudiant(Long idEtudiant, Etudiant updatedEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        etudiant.setNomEtudiant(updatedEtudiant.getNomEtudiant());
        etudiant.setPrenomEtudiant(updatedEtudiant.getPrenomEtudiant());
        etudiant.setCinEtudiant(updatedEtudiant.getCinEtudiant());
        etudiant.setDateNaissance(updatedEtudiant.getDateNaissance());
        etudiant.setIdReservations(updatedEtudiant.getIdReservations());

        return etudiantRepository.save(etudiant);
    }

    public void deleteEtudiant(Long idEtudiant) {
        Etudiant etudiant = etudiantRepository.findById(idEtudiant)
                .orElseThrow(() -> new RuntimeException("Etudiant not found"));

        etudiantRepository.delete(etudiant);
    }








}
