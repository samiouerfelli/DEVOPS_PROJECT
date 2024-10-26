package tn.esprit.tpfoyer;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "bloc-service")
public interface BlocClient {

    @DeleteMapping("/api/v1/blocs/delete-by-foyer/{idFoyer}")
    void deleteBlocsByFoyerId(@PathVariable("idFoyer") Long idFoyer);



}
