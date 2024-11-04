package tn.esprit.tpfoyer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idEtudiant;

    @NotBlank(message = "Name cannot be blank")
    String nomEtudiant;

    @NotBlank(message = "Surname cannot be blank")
    String prenomEtudiant;

    @NotBlank(message = "CIN cannot be blank")
    String cinEtudiant;

    @Temporal(TemporalType.DATE)
    @Past(message = "Birth date must be in the past")
    Date dateNaissance;

    String profilePicture;

    @ManyToMany(mappedBy = "etudiants")
    Set<Reservation> reservations;
}

