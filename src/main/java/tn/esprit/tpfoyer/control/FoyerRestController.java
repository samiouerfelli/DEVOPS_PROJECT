package tn.esprit.tpfoyer.control;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.service.IFoyerService;

import java.util.List;

@RestController
@RequestMapping("/foyer")
public class FoyerRestController {

    private IFoyerService foyerService;

    @PostMapping("/add-foyer")
    public ResponseEntity<Foyer> addFoyer(@RequestBody Foyer foyer) {
        // Validate input
        if (foyer.getNomFoyer() == null || foyer.getNomFoyer().trim().isEmpty() ||
                foyer.getCapaciteFoyer() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Foyer savedFoyer = foyerService.addFoyer(foyer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFoyer);
    }

    @GetMapping("/retrieve-all-foyers")
    public ResponseEntity<List<Foyer>> getFoyers() {
        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        return ResponseEntity.ok(foyers);
    }

    @GetMapping("/retrieve-foyer/{foyer-id}")
    public ResponseEntity<Foyer> retrieveFoyer(@PathVariable("foyer-id") Long fId) {
        Foyer foyer = foyerService.retrieveFoyer(fId);
        if (foyer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(foyer);
    }

    @DeleteMapping("/remove-foyer/{foyer-id}")
    public ResponseEntity<Void> removeFoyer(@PathVariable("foyer-id") Long fId) {
        foyerService.removeFoyer(fId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/modify-foyer")
    public ResponseEntity<Foyer> modifyFoyer(@RequestBody Foyer foyer) {
        Foyer updatedFoyer = foyerService.modifyFoyer(foyer);
        if (updatedFoyer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedFoyer);
    }
}
