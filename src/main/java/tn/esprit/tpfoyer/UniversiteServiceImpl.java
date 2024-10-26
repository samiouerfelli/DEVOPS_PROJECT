package tn.esprit.tpfoyer;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UniversiteServiceImpl  {

    UniversiteRepository universiteRepository;
    FoyerClient foyerClient;

    private UniversiteDTO convertToDto(Universite universite) {
        UniversiteDTO universiteDTO = new UniversiteDTO();
        universiteDTO.setIdUniversite(universite.getIdUniversite());
        universiteDTO.setNomUniversite(universite.getNomUniversite());
        universiteDTO.setAdresse(universite.getAdresse());
        universiteDTO.setIdFoyer(universite.getIdFoyer());
        return universiteDTO;
    }

    public Universite addUniversite(Universite universite) {
        return universiteRepository.save(universite);
    }

    public void assignFoyerToUniversite(Long idUniversite, Long idFoyer) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found"));

        universite.setIdFoyer(idFoyer);
        universiteRepository.save(universite);
    }

    public UniversiteDTO retrieveUniversite(Long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found"));

        UniversiteDTO universiteDTO = convertToDto(universite);

        if (universite.getIdFoyer() != null) {
            FoyerDTO foyer = foyerClient.retrieveFoyer(universite.getIdFoyer());
            universiteDTO.setFoyer(foyer);
        }

        return universiteDTO;
    }



    public List<UniversiteDTO> retrieveAllUniversites() {
        return universiteRepository.findAll().stream()
                .map(universite -> {
                    UniversiteDTO universiteDTO = convertToDto(universite);

                    // Fetch associated Foyer if idFoyer is present
                    if (universite.getIdFoyer() != null) {
                        FoyerDTO foyer = foyerClient.retrieveFoyer(universite.getIdFoyer());
                        universiteDTO.setFoyer(foyer);
                    }

                    return universiteDTO;
                })
                .collect(Collectors.toList());
    }

    public List<UniversiteDTO> getUniversitesWithoutFoyer() {
        return universiteRepository.findByIdFoyerIsNull().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public UniversiteDTO updateUniversite(Long idUniversite, UniversiteDTO universiteDTO) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found"));

        universite.setNomUniversite(universiteDTO.getNomUniversite());
        universite.setAdresse(universiteDTO.getAdresse());

        Universite updatedUniversite = universiteRepository.save(universite);

        return convertToDto(updatedUniversite);
    }




    public void deleteUniversite(Long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found"));

        if (universite.getIdFoyer() != null) {
            foyerClient.unassignFoyerFromUniversite(universite.getIdFoyer());
        }

        universiteRepository.deleteById(idUniversite);
    }
    public void unassignFoyerFromUniversite(Long idUniversite) {
        Universite universite = universiteRepository.findById(idUniversite)
                .orElseThrow(() -> new RuntimeException("Universite not found"));
        universite.setIdFoyer(null);
        universiteRepository.save(universite);
    }


















}

