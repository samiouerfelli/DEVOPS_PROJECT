package tn.esprit.tpfoyer.control;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/etudiant")
public class EtudiantRestController {

    private final IEtudiantService etudiantService;

    @GetMapping("/retrieve-all-etudiants")
    public ResponseEntity<List<Etudiant>> getEtudiants() {
        List<Etudiant> listEtudiants = etudiantService.retrieveAllEtudiants();
        return listEtudiants.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(listEtudiants);
    }

    @GetMapping("/retrieve-etudiant-cin/{cin}")
    public ResponseEntity<Etudiant> retrieveEtudiantParCin(@PathVariable("cin") Long cin) {
        Etudiant etudiant = etudiantService.recupererEtudiantParCin(cin);
        return etudiant != null ? ResponseEntity.ok(etudiant) : ResponseEntity.notFound().build();
    }

    @GetMapping("/retrieve-etudiant/{id}")
    public ResponseEntity<Etudiant> retrieveEtudiant(@PathVariable Long id) {
        Etudiant etudiant = etudiantService.retrieveEtudiant(id);
        return etudiant != null ? ResponseEntity.ok(etudiant) : ResponseEntity.notFound().build();
    }

    @PostMapping("/add-etudiant")
    public ResponseEntity<Etudiant> addEtudiant(@Valid @RequestBody Etudiant etudiant) {
        Etudiant savedEtudiant = etudiantService.addEtudiant(etudiant);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEtudiant);
    }

    @DeleteMapping("/remove-etudiant/{id}")
    public ResponseEntity<Void> removeEtudiant(@PathVariable("id") Long chId) {
        etudiantService.removeEtudiant(chId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modify-etudiant")
    public ResponseEntity<Etudiant> modifyEtudiant(@RequestBody Etudiant c) {
        Etudiant etudiant = etudiantService.modifyEtudiant(c);
        return etudiant != null ? ResponseEntity.ok(etudiant) : ResponseEntity.badRequest().build();
    }
}
