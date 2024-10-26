package tn.esprit.tpfoyer.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entities.ChambreDTO;

import java.util.List;

@FeignClient(name = "chambre-service")
public interface ChambreClient {

    @GetMapping("/api/v1/chambres/retrieve-chambre/{idChambre}")
    ChambreDTO getChambreById(@PathVariable("idChambre") Long idChambre);

    @PutMapping("/api/v1/chambres/update-availability/{idChambre}")
    void updateChambreAvailability(@PathVariable("idChambre") Long idChambre, @RequestParam boolean isReserved);

    @PutMapping("/api/v1/chambres/{idChambre}/update-reservations")
    void updateChambreReservations(@PathVariable Long idChambre, @RequestBody List<String> idReservations);



}
