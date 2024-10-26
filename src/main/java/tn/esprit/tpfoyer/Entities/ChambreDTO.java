package tn.esprit.tpfoyer.Entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChambreDTO {
    Long idChambre;
    Long numeroChambre;
    TypeChambre typeChambre;
    Long idBloc;
    Boolean isReserved;
    List<String> idReservations = new ArrayList<>();


}
