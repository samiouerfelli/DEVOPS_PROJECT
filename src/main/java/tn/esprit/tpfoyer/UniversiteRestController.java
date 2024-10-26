package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/universites")
public class UniversiteRestController {

    UniversiteServiceImpl universiteService;


    @PostMapping("/add-universite")
    public ResponseEntity<Universite> addUniversite(@RequestBody Universite universite) {
        Universite createdUniversite = universiteService.addUniversite(universite);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUniversite);
    }

    @PutMapping("/{idUniversite}/assign-foyer")
    public ResponseEntity<Void> assignFoyerToUniversite(@PathVariable("idUniversite") Long idUniversite, @RequestParam Long idFoyer) {
        universiteService.assignFoyerToUniversite(idUniversite, idFoyer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/retrieve-universite/{idUniversite}")
    public ResponseEntity<UniversiteDTO> retrieveUniversite(@PathVariable("idUniversite") Long idUniversite) {
        UniversiteDTO universite = universiteService.retrieveUniversite(idUniversite);
        return ResponseEntity.ok(universite);
    }

    @GetMapping("/retrieve-all-universites")
    public ResponseEntity<List<UniversiteDTO>> retrieveAllUniversites() {
        List<UniversiteDTO> universites = universiteService.retrieveAllUniversites();
        return ResponseEntity.ok(universites);
    }

    @GetMapping("/retrieve-universites-without-foyer")
    public ResponseEntity<List<UniversiteDTO>> getUniversitesWithoutFoyer() {
        List<UniversiteDTO> universites = universiteService.getUniversitesWithoutFoyer();
        return ResponseEntity.ok(universites);
    }

    @PutMapping("/update-universite/{idUniversite}")
    public ResponseEntity<UniversiteDTO> updateUniversite(
            @PathVariable Long idUniversite,
            @RequestBody UniversiteDTO universiteDTO) {
        UniversiteDTO updatedUniversite = universiteService.updateUniversite(idUniversite, universiteDTO);
        return ResponseEntity.ok(updatedUniversite);
    }



    @DeleteMapping("/delete-universite/{idUniversite}")
    public ResponseEntity<String> deleteUniversite(@PathVariable Long idUniversite) {
        universiteService.deleteUniversite(idUniversite);
        return ResponseEntity.ok("Universite with ID " + idUniversite + " has been deleted successfully.");
    }


    @PutMapping("/unassign-foyer/{idUniversite}")
    public ResponseEntity<Void> unassignFoyerFromUniversite(@PathVariable Long idUniversite) {
        universiteService.unassignFoyerFromUniversite(idUniversite);
        return ResponseEntity.noContent().build();
    }












}
