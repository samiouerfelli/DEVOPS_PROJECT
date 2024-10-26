package tn.esprit.tpfoyer.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.FoyerDTO;

@FeignClient(name = "foyer-service")
public interface FoyerClient {

    @GetMapping("/api/v1/foyers/retrieve-foyer/{idFoyer}")
    FoyerDTO retrieveFoyer(@PathVariable("idFoyer") Long idFoyer);
    @PutMapping("/api/v1/foyers/{idFoyer}/add-bloc")
    void addBlocToFoyer(@PathVariable("idFoyer") Long idFoyer, @RequestParam Long idBloc);
    @PutMapping("/api/v1/foyers/{idFoyer}/remove-bloc")
    void removeBlocFromFoyer(@PathVariable("idFoyer") Long idFoyer, @RequestParam Long idBloc);


}
