package tn.esprit.tpfoyer.reservationservice;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
    @Id
    String idReservation;


    Date anneeUniversitaire;
    boolean estValide;

    @ElementCollection
    List<Long> idEtudiants;
}
