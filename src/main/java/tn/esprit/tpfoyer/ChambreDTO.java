package tn.esprit.tpfoyer;

import lombok.Data;

@Data
public class ChambreDTO {
    Long idChambre;
    Long numeroChambre;
    TypeChambre typeChambre; // Assuming this is a string, change as needed
    Long idBloc; // Assuming a Chambre is linked to a Bloc
}
