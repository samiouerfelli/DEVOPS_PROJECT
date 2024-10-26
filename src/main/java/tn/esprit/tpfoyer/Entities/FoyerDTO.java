package tn.esprit.tpfoyer.Entities;

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
