package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreServiceImpl {
    ChambreRepository chambreRepository;
    private final BlocClient blocClient;


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
                .orElseThrow(() -> new RuntimeException("Chambre not found"));

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
                .orElseThrow(() -> new RuntimeException("Chambre not found"));

        chambre.setIsReserved(isReserved);

        chambreRepository.save(chambre);
    }

    public Chambre retrieveChambre(Long idChambre) {
        return chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException("Chambre not found"));
    }










}
