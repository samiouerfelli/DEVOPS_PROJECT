package tn.esprit.tpfoyer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Chambre {
    private long idChambre = 0L;
    private long numeroChambre = 0L;
    private TypeChambre typeC;
    private Set<Reservation> reservations = new HashSet<>(); // Initialize here
    private Bloc bloc;

    @Override
    public String toString() {
        return "Chambre{" +
                "idChambre=" + idChambre +
                ", numeroChambre=" + numeroChambre +
                ", typeC=" + typeC +
                ", reservations=" + reservations +
                ", bloc=" + bloc +
                '}';
    }
}
