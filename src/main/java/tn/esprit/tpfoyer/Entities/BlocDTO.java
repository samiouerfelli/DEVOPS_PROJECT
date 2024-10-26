package tn.esprit.tpfoyer.Entities;

import lombok.Data;

import java.util.List;

@Data
public class BlocDTO {
    Long idBloc;
    String nomBloc;
    Long capaciteBloc;
    Long idFoyer;
    List<Long> idChambres;
}
