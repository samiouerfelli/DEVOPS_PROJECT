package tn.esprit.tpfoyer;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chambres")
public class ChambreRestController {
    ChambreServiceImpl chambreService;


     ////////////////// Blocs ////////////////////////


     @PostMapping("/add-chambre/{idBloc}")
     public ResponseEntity<Chambre> addChambreAndAssignToBloc(@RequestBody Chambre chambre, @PathVariable("idBloc") Long idBloc) {
         Chambre savedChambre = chambreService.addChambreAndAssignToBloc(chambre, idBloc);
         return ResponseEntity.status(HttpStatus.CREATED).body(savedChambre);
     }

    @DeleteMapping("/delete-by-bloc/{idBloc}")
    public ResponseEntity<String> deleteChambresByBlocId(@PathVariable Long idBloc) {
        chambreService.deleteChambresByBlocId(idBloc);
        return ResponseEntity.ok("All Chambres associated with Bloc ID " + idBloc + " have been deleted successfully.");
    }


    @DeleteMapping("/delete-chambre/{idChambre}")
    public ResponseEntity<String> deleteChambreAndRemoveFromBloc(@PathVariable Long idChambre) {
        chambreService.deleteChambreAndRemoveFromBloc(idChambre);
        return ResponseEntity.ok("Chambre with ID " + idChambre + " has been deleted successfully, and it was removed from the associated Bloc.");
    }
    @GetMapping("/retrieve-by-bloc/{idBloc}")
    public ResponseEntity<List<ChambreDTO>> getChambresByBlocId(@PathVariable Long idBloc) {
        List<ChambreDTO> chambres = chambreService.getChambresByBlocId(idBloc);
        return ResponseEntity.ok(chambres);
    }

    ////////////////////    Reservations    ////////////////////////

    @PutMapping("/update-availability/{idChambre}")
    public void updateChambreAvailability(@PathVariable("idChambre") Long idChambre, @RequestParam boolean isReserved) {
        chambreService.updateChambreAvailability(idChambre, isReserved);
    }

    @GetMapping("/retrieve-chambre/{idChambre}")
    public ResponseEntity<Chambre> getChambreById(@PathVariable Long idChambre) {
        Chambre chambre = chambreService.retrieveChambre(idChambre);
        return ResponseEntity.ok(chambre);
    }



}
