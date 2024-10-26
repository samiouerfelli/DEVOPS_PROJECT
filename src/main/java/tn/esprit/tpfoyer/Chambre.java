package tn.esprit.tpfoyer;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

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
    Boolean isReserved ;

    Long idBloc;
}
