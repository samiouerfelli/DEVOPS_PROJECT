package tn.esprit.tpfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "chambre-service")
public interface ChambreClient {

    @GetMapping("/api/v1/chambres/retrieve-chambre/{idChambre}")
    ChambreDTO getChambreById(@PathVariable("idChambre") Long idChambre);

    @PutMapping("/api/v1/chambres/update-availability/{idChambre}")
    void updateChambreAvailability(@PathVariable("idChambre") Long idChambre, @RequestParam boolean isReserved);

}
