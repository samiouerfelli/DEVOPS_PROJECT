package tn.esprit.tpfoyer;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/etudiants")
public class EtudiantRestController {

    EtudiantServiceImpl etudiantService;


    ///////////// Etudiant //////////////////


    @PostMapping("/add-etudiant")
    public ResponseEntity<Etudiant> addEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant createdEtudiant = etudiantService.addEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEtudiant);
    }

    @GetMapping("/retrieve-etudiant/{idEtudiant}")
    public ResponseEntity<Etudiant> getEtudiantById(@PathVariable Long idEtudiant) {
        Etudiant etudiant = etudiantService.getEtudiantById(idEtudiant);
        return ResponseEntity.ok(etudiant);
    }

    @GetMapping("/retrieve-all-etudiants")
    public ResponseEntity<List<Etudiant>> getAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.getAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @PutMapping("/update-etudiant/{idEtudiant}")
    public ResponseEntity<Etudiant> updateEtudiant(@PathVariable Long idEtudiant, @RequestBody Etudiant updatedEtudiant) {
        Etudiant etudiant = etudiantService.updateEtudiant(idEtudiant, updatedEtudiant);
        return ResponseEntity.ok(etudiant);
    }

    @DeleteMapping("/delete-etudiant/{idEtudiant}")
    public ResponseEntity<String> deleteEtudiant(@PathVariable Long idEtudiant) {
        etudiantService.deleteEtudiant(idEtudiant);
        return ResponseEntity.ok("Etudiant with ID " + idEtudiant + " has been deleted successfully.");
    }





}
