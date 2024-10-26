package tn.esprit.tpfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bloc-service")
public interface BlocClient {
    @GetMapping("/api/v1/blocs/retrieve-bloc/{idBloc}")
    BlocDTO retrieveBloc(@PathVariable("idBloc") Long idBloc);

    @PutMapping("/api/v1/blocs/{idBloc}/add-chambre/{idChambre}")
    void addChambreToBloc(@PathVariable("idBloc") Long idBloc, @PathVariable ("idChambre") Long idChambre);

    @PutMapping("/api/v1/blocs/{idBloc}/remove-chambre/{idChambre}")
    void removeChambreFromBloc(@PathVariable("idBloc") Long idBloc, @PathVariable ("idChambre") Long idChambre);

}
