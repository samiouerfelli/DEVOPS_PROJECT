package tn.esprit.tpfoyer.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import tn.esprit.tpfoyer.Entities.FoyerDTO;

@FeignClient(name = "foyer-service")

public interface FoyerClient {
    @GetMapping("/api/v1/foyers/retrieve-foyer/{idFoyer}")
    FoyerDTO retrieveFoyer(@PathVariable("idFoyer") Long idFoyer);
    @PutMapping("/api/v1/foyers/unassign-universite/{idFoyer}")
    void unassignFoyerFromUniversite(@PathVariable("idFoyer") Long idFoyer);


}
