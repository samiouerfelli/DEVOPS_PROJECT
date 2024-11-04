package tn.esprit.tpfoyer.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.service.IEtudiantService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/etudiant")
public class EtudiantRestController {

    @Autowired
    private IEtudiantService etudiantService;

    private static final String PROFILE_PICTURE_DIRECTORY = "C:/Users/Arsi/AppData/Local/Temp/tpfoyer/profile_pictures/";

    @PostMapping("/add-etudiant")
    public ResponseEntity<?> addEtudiant(
            @RequestParam("nomEtudiant") String nomEtudiant,
            @RequestParam("prenomEtudiant") String prenomEtudiant,
            @RequestParam("cinEtudiant") String cinEtudiant,
            @RequestParam("dateNaissance") String dateNaissanceStr,
            @RequestParam(value = "profilePicture", required = false) String profilePicturePath) throws IOException {

        // Check CIN length and validity
        if (cinEtudiant.length() != 8 || !cinEtudiant.matches("\\d+")) {
            return ResponseEntity.badRequest().body("CIN must be exactly 8 digits long and numeric.");
        }

        // Check for CIN uniqueness
        if (etudiantService.recupererEtudiantParCin(cinEtudiant) != null) {
            return ResponseEntity.badRequest().body("CIN already exists. Each student must have a unique CIN.");
        }

        // Create and populate Etudiant entity
        Etudiant etudiant = new Etudiant();
        etudiant.setNomEtudiant(nomEtudiant);
        etudiant.setPrenomEtudiant(prenomEtudiant);
        etudiant.setCinEtudiant(cinEtudiant);

        // Date parsing
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            etudiant.setDateNaissance(dateFormat.parse(dateNaissanceStr));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format. Use dd/MM/yyyy.");
        }

        // Set profile picture path
        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            etudiant.setProfilePicture(profilePicturePath);
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
    public ResponseEntity<Etudiant> retrieveEtudiantByCin(@PathVariable String cin) {
        Etudiant etudiant = etudiantService.recupererEtudiantParCin(cin);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(etudiant);
    }

    @DeleteMapping("/remove-etudiant/{id}")
    public ResponseEntity<Void> removeEtudiant(@PathVariable Long id) {
        etudiantService.removeEtudiant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/modify-etudiant/{idEtudiant}")
    public ResponseEntity<?> modifyEtudiant(
            @PathVariable Long idEtudiant,
            @RequestParam(value = "nomEtudiant", required = false) String nomEtudiant,
            @RequestParam(value = "prenomEtudiant", required = false) String prenomEtudiant,
            @RequestParam(value = "cinEtudiant", required = false) String cinEtudiant,
            @RequestParam(value = "dateNaissance", required = false) String dateNaissanceStr,
            @RequestParam(value = "profilePicture", required = false) String profilePicturePath) throws IOException {

        Etudiant etudiant = etudiantService.retrieveEtudiant(idEtudiant);
        if (etudiant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Etudiant not found.");
        }

        // Update only if the new value is provided
        if (nomEtudiant != null && !nomEtudiant.trim().isEmpty()) {
            etudiant.setNomEtudiant(nomEtudiant);
        }

        if (prenomEtudiant != null && !prenomEtudiant.trim().isEmpty()) {
            etudiant.setPrenomEtudiant(prenomEtudiant);
        }

        if (cinEtudiant != null && !cinEtudiant.trim().isEmpty()) {
            etudiant.setCinEtudiant(cinEtudiant);
        }

        if (dateNaissanceStr != null && !dateNaissanceStr.trim().isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date dateNaissance;
            try {
                dateNaissance = dateFormat.parse(dateNaissanceStr);
                etudiant.setDateNaissance(dateNaissance);
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Use dd/MM/yyyy.");
            }
        }

        if (profilePicturePath != null && !profilePicturePath.trim().isEmpty()) {
            etudiant.setProfilePicture(profilePicturePath);
        }

        Etudiant updatedEtudiant = etudiantService.modifyEtudiant(etudiant);
        return ResponseEntity.ok(updatedEtudiant);
    }
}

