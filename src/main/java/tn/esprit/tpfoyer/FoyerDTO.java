package tn.esprit.tpfoyer;

import jakarta.persistence.ElementCollection;
import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;

import java.util.List;

@Data
public class FoyerDTO {
    Long idFoyer;
    String nomFoyer;
    Long capaciteFoyer;
    Long idUniversite;
    List<Long> idBlocs;

}
