package tn.esprit.tpfoyer.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tn.esprit.tpfoyer.Entities.EtudiantDTO;

import java.util.List;

@FeignClient(name = "etudiant-service")
public interface EtudiantClient {

    @GetMapping("/api/v1/etudiants/retrieve-etudiant/{idEtudiant}")
    EtudiantDTO getEtudiantById(@PathVariable("idEtudiant") Long idEtudiant);

    @PutMapping("/api/v1/etudiants/{idEtudiant}/update-reservations")
    void updateEtudiantReservations(@PathVariable Long idEtudiant, @RequestBody List<String> idReservations);

}
