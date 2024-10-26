package tn.esprit.tpfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "etudiant-service")
public interface EtudiantClient {

    @GetMapping("/api/v1/etudiants/retrieve-etudiant/{idEtudiant}")
    EtudiantDTO getEtudiantById(@PathVariable("idEtudiant") Long idEtudiant);
}
