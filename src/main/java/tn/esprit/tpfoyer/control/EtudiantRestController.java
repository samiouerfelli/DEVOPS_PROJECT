package tn.esprit.tpfoyer.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.List;

@RestController
@RequestMapping("/etudiant")
public class EtudiantRestController {

    @Autowired
    private IEtudiantService etudiantService;

    @PostMapping("/add-etudiant")
    public ResponseEntity<Etudiant> addEtudiant(@RequestBody Etudiant etudiant) {
        // Validate input
        if (etudiant.getNomEtudiant() == null || etudiant.getNomEtudiant().trim().isEmpty() ||
                etudiant.getPrenomEtudiant() == null || etudiant.getPrenomEtudiant().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEtudiant);
    }

    @GetMapping("/retrieve-all-etudiants")
    public ResponseEntity<List<Etudiant>> retrieveAllEtudiants() {
        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();
        return ResponseEntity.ok(etudiants);
    }

    @GetMapping("/retrieve-etudiant/{id}")
    public ResponseEntity<Etudiant> retrieveEtudiant(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.retrieveEtudiant(id);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(etudiant);
    }

    @GetMapping("/retrieve-etudiant-cin/{cin}")
    public ResponseEntity<Etudiant> retrieveEtudiantByCin(@PathVariable Long cin) {
        Etudiant etudiant = etudiantService.recupererEtudiantParCin(cin);
        return ResponseEntity.ok(etudiant);
    }

    @DeleteMapping("/remove-etudiant/{id}")
    public ResponseEntity<Void> removeEtudiant(@PathVariable Long id) {
        etudiantService.removeEtudiant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/modify-etudiant")
    public ResponseEntity<Etudiant> modifyEtudiant(@RequestBody Etudiant etudiant) {
        Etudiant updatedEtudiant = etudiantService.modifyEtudiant(etudiant);
        return ResponseEntity.ok(updatedEtudiant);
    }
}