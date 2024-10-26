package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FoyerServiceImpl   {

    FoyerRepository foyerRepository;
    UniversiteClient universiteClient;
    BlocClient blocClient;


    private FoyerDTO convertToDto(Foyer foyer) {
        FoyerDTO foyerDTO = new FoyerDTO();
        foyerDTO.setIdFoyer(foyer.getIdFoyer());
        foyerDTO.setNomFoyer(foyer.getNomFoyer());
        foyerDTO.setCapaciteFoyer(foyer.getCapaciteFoyer());
        foyerDTO.setIdUniversite(foyer.getIdUniversite());
        foyerDTO.setIdBlocs(foyer.getIdBlocs());
        return foyerDTO;
    }

    ///////////////          Foyers          ////////////////////////

    public Foyer addFoyerAndAssignToUniversite(Foyer foyer, Long idUniversite) {
        UniversiteDTO universite = universiteClient.retrieveUniversite(idUniversite);
        if (universite == null) {
            throw new RuntimeException("Universite not found");
        }

        if (universite.getIdFoyer() != null) {
            throw new RuntimeException("This Universite already has a Foyer assigned.");
        }

        foyer.setIdUniversite(idUniversite);
        Foyer savedFoyer = foyerRepository.save(foyer);

        universiteClient.assignFoyerToUniversite(idUniversite, savedFoyer.getIdFoyer());

        return savedFoyer;
    }
    public FoyerDTO retrieveFoyer(Long idFoyer) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));
        return convertToDto(foyer);
    }

    public List<FoyerDTO> retrieveAllFoyers() {
        return foyerRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }




    public void unassignUniversiteFromFoyer(Long idFoyer) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));
        foyer.setIdUniversite(null);
        foyerRepository.save(foyer);
    }

    public void deleteFoyer(Long idFoyer) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));

        // If Foyer has an associated Universite, unassign it
        if (foyer.getIdUniversite() != null) {
            universiteClient.unassignFoyerFromUniversite(foyer.getIdUniversite());
        }

        // Delete the Foyer
        foyerRepository.deleteById(idFoyer);
    }



    ///////////////////          Blocs          ///////////////////////


    public void addBlocToFoyer(Long idFoyer, Long idBloc) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));

        foyer.getIdBlocs().add(idBloc);
        foyerRepository.save(foyer);
    }


    public void deleteFoyerAndBlocs(Long idFoyer) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));

        blocClient.deleteBlocsByFoyerId(idFoyer);

        if (foyer.getIdUniversite() != null) {
            universiteClient.unassignFoyerFromUniversite(foyer.getIdUniversite());
        }

        foyerRepository.deleteById(idFoyer);
    }

    public void removeBlocFromFoyer(Long idFoyer, Long idBloc) {
        Foyer foyer = foyerRepository.findById(idFoyer)
                .orElseThrow(() -> new RuntimeException("Foyer not found"));

        foyer.getIdBlocs().remove(idBloc);
        foyerRepository.save(foyer);
    }

    public List<FoyerDTO> getFoyersWithoutBlocs() {
        return foyerRepository.findByIdBlocsIsEmpty().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }






















}