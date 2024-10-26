package tn.esprit.tpfoyer;

import lombok.Data;

@Data
public class BlocDTO {
    Long idBloc;
    String nomBloc;
    Long capaciteBloc;
    Long idFoyer; // Assuming a Bloc can be assigned to a Foyer
}
