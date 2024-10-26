package tn.esprit.tpfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "universite-service")
public interface UniversiteClient {

    @GetMapping("/api/v1/universites/retrieve-universite/{idUniversite}")
    UniversiteDTO retrieveUniversite(@PathVariable("idUniversite") Long idUniversite);

    @PutMapping("/api/v1/universites/{idUniversite}/assign-foyer")
    void assignFoyerToUniversite(@PathVariable("idUniversite") Long idUniversite, @RequestParam Long idFoyer);

    @PutMapping("/api/v1/universites/unassign-foyer/{idUniversite}")
    void unassignFoyerFromUniversite(@PathVariable("idUniversite") Long idUniversite);


}






