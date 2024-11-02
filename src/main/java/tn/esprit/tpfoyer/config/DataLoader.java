package tn.esprit.tpfoyer.config;


import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.tpfoyer.entity.*;
import tn.esprit.tpfoyer.repository.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {


    private BlocRepository blocRepository;

    private ChambreRepository chambreRepository;

    private EtudiantRepository etudiantRepository;


    private ReservationRepository reservationRepository;

    private UniversiteRepository universiteRepository;
    private final Faker faker = new Faker();

    @Override
    @Transactional
    public void run(String... args) {
        // Create a Universite
        Universite universite = new Universite();
        universite.setNomUniversite(faker.university().name());
        universite.setAdresse(faker.address().fullAddress());

        // Create a Foyer
        Foyer foyer = new Foyer();
        foyer.setNomFoyer(faker.lordOfTheRings().character());
        foyer.setCapaciteFoyer(faker.number().numberBetween(50, 150));
        foyer.setUniversite(universite); // set the relationship

        // Save Universite and Foyer
        universite.setFoyer(foyer); // set the reverse relationship
        universiteRepository.save(universite); // Save Universite which cascades to Foyer

        // Create Blocs for the Foyer
        List<Bloc> blocs = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Bloc bloc = new Bloc();
            bloc.setNomBloc(faker.lordOfTheRings().location());
            bloc.setCapaciteBloc(faker.number().numberBetween(10, 30));
            bloc.setFoyer(foyer); // set the relationship
            blocs.add(bloc);
        }
        blocRepository.saveAll(blocs); // Save all Blocs

        // Create Chambres for each Bloc
        for (Bloc bloc : blocs) {
            List<Chambre> chambres = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                Chambre chambre = new Chambre();
                chambre.setNumeroChambre(faker.number().randomDigitNotZero());
                chambre.setTypeC(TypeChambre.values()[faker.number().numberBetween(0, TypeChambre.values().length)]);
                chambre.setBloc(bloc); // set the relationship
                chambres.add(chambre);
            }
            chambreRepository.saveAll(chambres); // Save all Chambres
        }

        for (int i = 0; i < 5; i++) {
            Reservation reservation = new Reservation();
            reservation.setIdReservation(faker.name().username());
            reservation.setAnneeUniversitaire(faker.date().birthday());
            reservation.setEstValide(true);

            // Create a set of students for this reservation
            Set<Etudiant> etudiantSet = new HashSet<>();
            for (int j = 0; j < 5; j++) {
                Etudiant etudiant = new Etudiant();

                etudiant.setDateNaissance(faker.date().birthday());
                etudiant.setNomEtudiant(faker.name().lastName());
                etudiant.setPrenomEtudiant(faker.name().firstName());

                // Save each Etudiant instance
                etudiantRepository.save(etudiant);
                etudiantSet.add(etudiant); // Add the saved Etudiant to the set
            }

            reservation.setEtudiants(etudiantSet); // Set the etudiants for the reservation
            reservationRepository.save(reservation); // Save the Reservation
        }
    }
}