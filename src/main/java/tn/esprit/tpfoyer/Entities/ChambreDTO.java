package tn.esprit.tpfoyer.Entities;

import lombok.Data;

@Data
public class ChambreDTO {
    Long idChambre;
    Long numeroChambre;
    TypeChambre typeChambre;
    Long idBloc;
    Boolean isReserved;

}
