package tn.esprit.tpfoyer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.Entities.Etudiant;
import tn.esprit.tpfoyer.Repository.EtudiantRepository;
import tn.esprit.tpfoyer.Services.EtudiantServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EtudiantServiceImplTest {

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
    public void testAddEtudiant() {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);

        when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

        Etudiant result = etudiantService.addEtudiant(etudiant);

        assertEquals(1L, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    public void testGetEtudiantById_Found() {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant result = etudiantService.getEtudiantById(1L);

        assertEquals(1L, result.getIdEtudiant());
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetEtudiantById_NotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> etudiantService.getEtudiantById(1L));
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllEtudiants() {
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
    public void testUpdateEtudiant_Found() {
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
    public void testUpdateEtudiant_NotFound() {
        Etudiant updatedEtudiant = new Etudiant();
        updatedEtudiant.setNomEtudiant("New Name");

        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> etudiantService.updateEtudiant(1L, updatedEtudiant));
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(0)).save(any(Etudiant.class));
    }

    @Test
    public void testDeleteEtudiant_Found() {
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        doNothing().when(etudiantRepository).delete(etudiant);

        etudiantService.deleteEtudiant(1L);

        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(1)).delete(etudiant);
    }

    @Test
    public void testDeleteEtudiant_NotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> etudiantService.deleteEtudiant(1L));
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(0)).delete(any(Etudiant.class));
    }

    @Test
    public void testUpdateEtudiantReservations_Found() {
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
    public void testUpdateEtudiantReservations_NotFound() {
        List<String> reservations = List.of("R1", "R2");

        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> etudiantService.updateEtudiantReservations(1L, reservations));
        verify(etudiantRepository, times(1)).findById(1L);
        verify(etudiantRepository, times(0)).save(any(Etudiant.class));
    }
}
