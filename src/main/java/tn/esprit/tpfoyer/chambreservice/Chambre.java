package tn.esprit.tpfoyer.chambreservice;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)

public class Chambre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idChambre;

    Long numeroChambre;

    @Enumerated(EnumType.STRING)
    TypeChambre typeC;



    @ElementCollection
    List<String> idReservations;

    Long idBloc;
}
