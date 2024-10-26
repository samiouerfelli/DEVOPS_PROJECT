package tn.esprit.tpfoyer.services;

import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entities.BlocDTO;
import tn.esprit.tpfoyer.entities.Chambre;
import tn.esprit.tpfoyer.entities.ChambreDTO;
import tn.esprit.tpfoyer.feignclient.BlocClient;
import tn.esprit.tpfoyer.repository.ChambreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreServiceImpl {
    ChambreRepository chambreRepository;
    private final BlocClient blocClient;

    private static final String MESSAGE = "Chambre not found";



    private ChambreDTO convertToDto(Chambre chambre) {
        ChambreDTO chambreDTO = new ChambreDTO();
        chambreDTO.setIdChambre(chambre.getIdChambre());
        chambreDTO.setNumeroChambre(chambre.getNumeroChambre());
        chambreDTO.setTypeChambre(chambre.getTypeC());
        chambreDTO.setIdBloc(chambre.getIdBloc());
        return chambreDTO;
    }

        ////////////////////// Blocs ////////////////////////

    public Chambre addChambreAndAssignToBloc(Chambre chambre, Long idBloc) {
        BlocDTO bloc = blocClient.retrieveBloc(idBloc);
        if (bloc == null) {
            throw new RuntimeException("Bloc not found");
        }

        chambre.setIdBloc(idBloc);
        chambre.setIsReserved(false);

        Chambre savedChambre = chambreRepository.save(chambre);

        blocClient.addChambreToBloc(idBloc, savedChambre.getIdChambre());

        return savedChambre;
    }
    public void deleteChambresByBlocId(Long idBloc) {
        List<Chambre> chambres = chambreRepository.findByIdBloc(idBloc);
        chambreRepository.deleteAll(chambres);
    }

    public void deleteChambreAndRemoveFromBloc(Long idChambre) {
        Chambre chambre = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        Long idBloc = chambre.getIdBloc();

        chambreRepository.deleteById(idChambre);

        if (idBloc != null) {
            blocClient.removeChambreFromBloc(idBloc, idChambre);
        }
    }

    public List<ChambreDTO> getChambresByBlocId(Long idBloc) {
        return chambreRepository.findByIdBloc(idBloc).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    ////////////////////    Reservation    ////////////////////////

    public void updateChambreAvailability(Long idChambre, boolean isReserved) {
        Chambre chambre = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException(MESSAGE));

        chambre.setIsReserved(isReserved);

        chambreRepository.save(chambre);
    }

    public Chambre retrieveChambre(Long idChambre) {
        return chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException(MESSAGE));
    }

    public void updateChambreReservations(Long idChambre, List<String> idReservations) {
        Chambre chambre = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException(MESSAGE + " with ID: " + idChambre));

        chambre.setIdReservations(idReservations);
        chambreRepository.save(chambre);
    }














}
