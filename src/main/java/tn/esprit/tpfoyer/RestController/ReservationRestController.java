package tn.esprit.tpfoyer.RestController;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.tpfoyer.Entities.Reservation;
import tn.esprit.tpfoyer.Exception.ReservationException;
import tn.esprit.tpfoyer.Services.ReservationServiceImpl;

import java.util.Date;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationRestController {

    ReservationServiceImpl reservationService;


    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestParam Long idEtudiant, @RequestParam Long idChambre, @RequestParam Date anneeUniversitaire) {
        try {
            Reservation reservation = reservationService.createReservation(idEtudiant, idChambre, anneeUniversitaire);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (ReservationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/cancel/{idReservation}")
    public ResponseEntity<String> cancelReservation(@PathVariable String idReservation) {
        reservationService.cancelReservation(idReservation);
        return ResponseEntity.ok("Reservation has been canceled.");
    }

    @GetMapping("/retrieve/{idReservation}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String idReservation) {
        Reservation reservation = reservationService.getReservationById(idReservation);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/retrieve-by-etudiant/{idEtudiant}")
    public ResponseEntity<List<Reservation>> getReservationsByEtudiant(@PathVariable Long idEtudiant) {
        List<Reservation> reservations = reservationService.getReservationsByEtudiant(idEtudiant);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/retrieve-by-chambre-year/{idChambre}")
    public ResponseEntity<List<Reservation>> getReservationsByChambreAndAnnee(@PathVariable Long idChambre, @RequestParam Date anneeUniversitaire) {
        List<Reservation> reservations = reservationService.getReservationsByChambreAndAnnee(idChambre, anneeUniversitaire);
        return ResponseEntity.ok(reservations);
    }



}
