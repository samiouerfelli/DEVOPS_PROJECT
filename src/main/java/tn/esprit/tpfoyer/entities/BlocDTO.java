package tn.esprit.tpfoyer.entities;

import lombok.Data;

import java.util.List;

@Data
public class BlocDTO {
    Long idBloc;
    String nomBloc;
    Long capaciteBloc;
    Long idFoyer;
    FoyerDTO foyer;
    List<Long> idChambres;
}
