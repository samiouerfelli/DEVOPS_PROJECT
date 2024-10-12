package tn.esprit.tpfoyer.blocservice;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@FeignClient(
        name = "foyer-service",
        url = "${application.config.foyer-url}"
)
public interface Foyer {

//    @GetMapping("/retrieve-all-foyers")
//    Optional<FoyerResponse>
}
