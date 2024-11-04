package tn.esprit.tpfoyer.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idChambre;

    long numeroChambre;

    @ManyToOne
    Bloc bloc; // Ensure this is a ManyToOne relationship

    @Enumerated(EnumType.STRING)
    TypeChambre typeC;

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Reservation> reservations = new HashSet<>(); // Assuming there is a Reservation class

    @Override
    public String toString() {
        return "Chambre{" +
                "idChambre=" + idChambre +
                ", numeroChambre=" + numeroChambre +
                ", typeC=" + typeC +
                ", bloc=" + (bloc != null ? bloc.getNomBloc() : "null") + // To avoid infinite recursion
                '}';
    }
}
