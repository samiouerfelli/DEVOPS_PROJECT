package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.exception.FoyerNotFoundException;
import tn.esprit.tpfoyer.feignclient.ChambreClient;
import tn.esprit.tpfoyer.feignclient.FoyerClient;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.entities.Bloc;
import tn.esprit.tpfoyer.entities.BlocDTO;
import tn.esprit.tpfoyer.entities.FoyerDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BlocServiceImpl {
    BlocRepository blocRepository;
    FoyerClient foyerClient;
    ChambreClient chambreClient;

    private BlocDTO convertToDto(Bloc bloc) {
        BlocDTO blocDTO = new BlocDTO();
        blocDTO.setIdBloc(bloc.getIdBloc());
        blocDTO.setNomBloc(bloc.getNomBloc());
        blocDTO.setCapaciteBloc(bloc.getCapaciteBloc());
        blocDTO.setIdFoyer(bloc.getIdFoyer());
        return blocDTO;
    }

    private static final String MESSAGE = "Bloc not found";


    ////////////////////// Foyers ////////////////////////

    public Bloc addBlocAndAssignToFoyer(Bloc bloc, Long idFoyer) {
        FoyerDTO foyer = foyerClient.retrieveFoyer(idFoyer);
        if (foyer == null) {
            throw new FoyerNotFoundException("Foyer not found with ID: " + idFoyer);
        }

        bloc.setIdFoyer(idFoyer);
        Bloc savedBloc = blocRepository.save(bloc);

        foyerClient.addBlocToFoyer(idFoyer, savedBloc.getIdBloc());

        return savedBloc;
    }

    public void deleteBlocsByFoyerId(Long idFoyer) {
        List<Bloc> blocs = blocRepository.findByIdFoyer(idFoyer);
        blocRepository.deleteAll(blocs);
    }
    public void deleteBlocAndRemoveFromFoyer(Long idBloc) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        Long idFoyer = bloc.getIdFoyer();

        blocRepository.deleteById(idBloc);

        if (idFoyer != null) {
            foyerClient.removeBlocFromFoyer(idFoyer, idBloc);
        }
    }

    public List<BlocDTO> getBlocsByFoyerId(Long idFoyer) {
        return blocRepository.findByIdFoyer(idFoyer).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BlocDTO retrieveBloc(Long idBloc) {
        return blocRepository.findById(idBloc)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException(MESSAGE));
    }


    ////////////////////// Chambres ////////////////////////

    public void addChambreToBloc(Long idBloc, Long idChambre) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        if (!bloc.getIdChambres().contains(idChambre)) {
            bloc.getIdChambres().add(idChambre);
        }

        blocRepository.save(bloc);
    }



    public void deleteBlocAndChambres(Long idBloc) {
        blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        chambreClient.deleteChambresByBlocId(idBloc);

        blocRepository.deleteById(idBloc);
    }
    public void removeChambreFromBloc(Long idBloc, Long idChambre) {
        Bloc bloc = blocRepository.findById(idBloc)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        bloc.getIdChambres().remove(idChambre);
        blocRepository.save(bloc);
    }






}
