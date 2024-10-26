package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/foyers")
public class FoyerRestController {

    private final FoyerServiceImpl foyerService;

    ////////////// Foyers //////////////

    @PostMapping("/add-foyer/{idUniversite}")
    public ResponseEntity<Foyer> addFoyerAndAssignToUniversite(@RequestBody Foyer foyer, @PathVariable("idUniversite") Long idUniversite) {
        Foyer savedFoyer = foyerService.addFoyerAndAssignToUniversite(foyer, idUniversite);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoyer);
    }
    @GetMapping("/retrieve-foyer/{idFoyer}")
    public ResponseEntity<FoyerDTO> retrieveFoyer(@PathVariable("idFoyer") Long idFoyer) {
        FoyerDTO foyer = foyerService.retrieveFoyer(idFoyer);
        return ResponseEntity.ok(foyer);
    }
    @GetMapping("/retrieve-all-foyers")
    public ResponseEntity<List<FoyerDTO>> retrieveAllFoyers() {
        List<FoyerDTO> foyers = foyerService.retrieveAllFoyers();
        return ResponseEntity.ok(foyers);
    }

    @DeleteMapping("/delete-foyer/{idFoyer}")
    public ResponseEntity<String> deleteFoyer(@PathVariable Long idFoyer) {
        foyerService.deleteFoyer(idFoyer);
        return ResponseEntity.ok("Foyer with ID " + idFoyer + " has been deleted successfully.");
    }


    @PutMapping("/unassign-universite/{idFoyer}")
    public ResponseEntity<Void> unassignUniversiteFromFoyer(@PathVariable Long idFoyer) {
        foyerService.unassignUniversiteFromFoyer(idFoyer);
        return ResponseEntity.noContent().build();
    }

    ////////////// Blocs //////////////


    @PutMapping("/{idFoyer}/add-bloc")
    public ResponseEntity<Void> addBlocToFoyer(@PathVariable("idFoyer") Long idFoyer, @RequestParam Long idBloc) {
        foyerService.addBlocToFoyer(idFoyer, idBloc);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/delete-foyer-and-blocs/{idFoyer}")
    public ResponseEntity<String> deleteFoyerAndBlocs(@PathVariable Long idFoyer) {
        foyerService.deleteFoyerAndBlocs(idFoyer);
        return ResponseEntity.ok("Foyer with ID " + idFoyer + " and all associated blocs have been deleted successfully.");
    }
    @PutMapping("/{idFoyer}/remove-bloc")
    public ResponseEntity<Void> removeBlocFromFoyer(@PathVariable("idFoyer") Long idFoyer, @RequestParam Long idBloc) {
        foyerService.removeBlocFromFoyer(idFoyer, idBloc);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/retrieve-foyers-without-blocs")
    public ResponseEntity<List<FoyerDTO>> getFoyersWithoutBlocs() {
        List<FoyerDTO> foyers = foyerService.getFoyersWithoutBlocs();
        return ResponseEntity.ok(foyers);
    }







}
