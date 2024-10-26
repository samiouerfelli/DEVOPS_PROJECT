package tn.esprit.tpfoyer;

import lombok.Data;

@Data
public class UniversiteDTO {
    Long idUniversite;
    String nomUniversite;
    String adresse;
    Long idFoyer;
    FoyerDTO foyer;
}
