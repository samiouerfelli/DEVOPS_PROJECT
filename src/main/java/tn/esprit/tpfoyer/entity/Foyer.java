package tn.esprit.tpfoyer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Foyer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idFoyer;

    String nomFoyer;
    long capaciteFoyer;

    @OneToOne(mappedBy = "foyer", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude // Prevent circular reference in toString
    Universite universite;

    @OneToMany(mappedBy = "foyer", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude // Prevent circular reference in toString
    Set<Bloc> blocs;

}
