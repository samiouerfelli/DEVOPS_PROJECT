package tn.esprit.tpfoyer.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "chambre-service")
public interface ChambreClient {

    @DeleteMapping("/api/v1/chambres/delete-by-bloc/{idBloc}")
    void deleteChambresByBlocId(@PathVariable("idBloc") Long idBloc);

}
