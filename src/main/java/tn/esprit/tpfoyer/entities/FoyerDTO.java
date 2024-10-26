package tn.esprit.tpfoyer.entities;

import lombok.Data;

import java.util.List;

@Data
public class FoyerDTO {
    Long idFoyer;
    String nomFoyer;
    Long capaciteFoyer;
    Long idUniversite;
    List<Long> idBlocs;

}
