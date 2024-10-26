package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entities.Etudiant;
import tn.esprit.tpfoyer.exception.EtudiantNotFoundException;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.services.EtudiantServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EtudiantServiceImplTest {

   @Mock
   private EtudiantRepository etudiantRepository;

   @InjectMocks
   private EtudiantServiceImpl etudiantService;

   @BeforeEach
   public void setUp() {
      MockitoAnnotations.openMocks(this);
   }

   ///////////// Etudiant //////////////////

   @Test
   void testAddEtudiant() {
      Etudiant etudiant = new Etudiant();
      etudiant.setIdEtudiant(1L);

      when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

      Etudiant result = etudiantService.addEtudiant(etudiant);

      assertEquals(1L, result.getIdEtudiant());
      verify(etudiantRepository, times(1)).save(etudiant);
   }

   @Test
   void testGetEtudiantById_Found() {
      Etudiant etudiant = new Etudiant();
      etudiant.setIdEtudiant(1L);

      when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

      Etudiant result = etudiantService.getEtudiantById(1L);

      assertEquals(1L, result.getIdEtudiant());
      verify(etudiantRepository, times(1)).findById(1L);
   }

   @Test
   void testGetEtudiantById_NotFound() {
      when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

      EtudiantNotFoundException exception = assertThrows(
              EtudiantNotFoundException.class,
              () -> etudiantService.getEtudiantById(1L)
      );

      assertEquals("Etudiant not found with ID: 1", exception.getMessage());
      verify(etudiantRepository, times(1)).findById(1L);
   }

   @Test
   void testGetAllEtudiants() {
      List<Etudiant> etudiants = new ArrayList<>();
      Etudiant etudiant = new Etudiant();
      etudiant.setIdEtudiant(1L);
      etudiants.add(etudiant);

      when(etudiantRepository.findAll()).thenReturn(etudiants);

      List<Etudiant> result = etudiantService.getAllEtudiants();

      assertEquals(1, result.size());
      assertEquals(1L, result.get(0).getIdEtudiant());
      verify(etudiantRepository, times(1)).findAll();
   }

   @Test
   void testUpdateEtudiant_Found() {
      Etudiant existingEtudiant = new Etudiant();
      existingEtudiant.setIdEtudiant(1L);
      existingEtudiant.setNomEtudiant("Old Name");

      Etudiant updatedEtudiant = new Etudiant();
      updatedEtudiant.setNomEtudiant("New Name");

      when(etudiantRepository.findById(1L)).thenReturn(Optional.of(existingEtudiant));
      when(etudiantRepository.save(any(Etudiant.class))).thenReturn(existingEtudiant);

      Etudiant result = etudiantService.updateEtudiant(1L, updatedEtudiant);

      assertEquals("New Name", result.getNomEtudiant());
      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(1)).save(existingEtudiant);
   }

   @Test
   void testUpdateEtudiant_NotFound() {
      Etudiant updatedEtudiant = new Etudiant();
      updatedEtudiant.setNomEtudiant("New Name");

      when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

      EtudiantNotFoundException exception = assertThrows(
              EtudiantNotFoundException.class,
              () -> etudiantService.updateEtudiant(1L, updatedEtudiant)
      );

      assertEquals("Etudiant not found with ID: 1", exception.getMessage());
      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(0)).save(any(Etudiant.class));
   }

   @Test
   void testDeleteEtudiant_Found() {
      Etudiant etudiant = new Etudiant();
      etudiant.setIdEtudiant(1L);

      when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
      doNothing().when(etudiantRepository).delete(etudiant);

      etudiantService.deleteEtudiant(1L);

      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(1)).delete(etudiant);
   }

   @Test
   void testDeleteEtudiant_NotFound() {
      when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

      EtudiantNotFoundException exception = assertThrows(
              EtudiantNotFoundException.class,
              () -> etudiantService.deleteEtudiant(1L)
      );

      assertEquals("Etudiant not found with ID: 1", exception.getMessage());
      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(0)).delete(any(Etudiant.class));
   }

   @Test
   void testUpdateEtudiantReservations_Found() {
      Etudiant etudiant = new Etudiant();
      etudiant.setIdEtudiant(1L);

      List<String> reservations = List.of("R1", "R2");

      when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
      when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

      etudiantService.updateEtudiantReservations(1L, reservations);

      assertEquals(2, etudiant.getIdReservations().size());
      assertEquals("R1", etudiant.getIdReservations().get(0));
      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(1)).save(etudiant);
   }

   @Test
   void testUpdateEtudiantReservations_NotFound() {
      List<String> reservations = List.of("R1", "R2");

      when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

      EtudiantNotFoundException exception = assertThrows(
              EtudiantNotFoundException.class,
              () -> etudiantService.updateEtudiantReservations(1L, reservations)
      );

      assertEquals("Etudiant not found with ID: 1", exception.getMessage());
      verify(etudiantRepository, times(1)).findById(1L);
      verify(etudiantRepository, times(0)).save(any(Etudiant.class));
   }
}
