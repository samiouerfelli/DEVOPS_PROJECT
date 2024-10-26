package tn.esprit.tpfoyer;

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
