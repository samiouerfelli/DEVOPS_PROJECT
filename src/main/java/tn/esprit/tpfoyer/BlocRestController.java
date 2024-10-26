package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/blocs")
public class BlocRestController {

    private final BlocServiceImpl blocService;

    ////////////////////// Foyers ////////////////////////

    @PostMapping("/add-bloc/{idFoyer}")
    public ResponseEntity<Bloc> addBlocAndAssignToFoyer(@RequestBody Bloc bloc, @PathVariable("idFoyer") Long idFoyer) {
        Bloc savedBloc = blocService.addBlocAndAssignToFoyer(bloc, idFoyer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBloc);
    }

    @DeleteMapping("/delete-by-foyer/{idFoyer}")
    public ResponseEntity<String> deleteBlocsByFoyerId(@PathVariable Long idFoyer) {
        blocService.deleteBlocsByFoyerId(idFoyer);
        return ResponseEntity.ok("All blocs associated with Foyer ID " + idFoyer + " have been deleted successfully.");
    }

    @DeleteMapping("/delete-bloc-foyer/{idBloc}")
    public ResponseEntity<String> deleteBlocAndRemoveFromFoyer(@PathVariable Long idBloc) {
        blocService.deleteBlocAndRemoveFromFoyer(idBloc);
        return ResponseEntity.ok("Bloc with ID " + idBloc + " has been deleted successfully, and it was removed from the associated Foyer.");
    }
    @GetMapping("/retrieve-by-foyer/{idFoyer}")
    public ResponseEntity<List<BlocDTO>> getBlocsByFoyerId(@PathVariable Long idFoyer) {
        List<BlocDTO> blocs = blocService.getBlocsByFoyerId(idFoyer);
        return ResponseEntity.ok(blocs);
    }

    @GetMapping("/retrieve-bloc/{idBloc}")
    public ResponseEntity<BlocDTO> getBlocById(@PathVariable Long idBloc) {
        BlocDTO bloc = blocService.retrieveBloc(idBloc);
        return ResponseEntity.ok(bloc);
    }


    ////////////////// Chambres ////////////////////////


    @PutMapping("/{idBloc}/add-chambre/{idChambre}")
    public ResponseEntity<Void> addChambreToBloc(@PathVariable("idBloc") Long idBloc, @PathVariable ("idChambre") Long idChambre) {
        blocService.addChambreToBloc(idBloc, idChambre);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/delete-bloc-chambres/{idBloc}")
    public ResponseEntity<String> deleteBlocAndChambres(@PathVariable Long idBloc) {
        blocService.deleteBlocAndChambres(idBloc);
        return ResponseEntity.ok("Bloc with ID " + idBloc + " and all associated chambres have been deleted successfully.");
    }
    @PutMapping("/{idBloc}/remove-chambre/{idChambre}")
    public ResponseEntity<Void> removeChambreFromBloc(@PathVariable("idBloc") Long idBloc, @PathVariable ("idChambre") Long idChambre) {
        blocService.removeChambreFromBloc(idBloc, idChambre);
        return ResponseEntity.noContent().build();
    }








}
