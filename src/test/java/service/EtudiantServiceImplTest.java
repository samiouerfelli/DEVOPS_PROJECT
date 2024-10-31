    package service;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;
    import tn.esprit.tpfoyer.entity.Etudiant;
    import tn.esprit.tpfoyer.repository.EtudiantRepository;
    import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

    import java.util.Arrays;
    import java.util.List;
    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.Mockito.*;

    class EtudiantServiceImplTest {

        @Mock
        private EtudiantRepository etudiantRepository;

        @InjectMocks
        private EtudiantServiceImpl etudiantService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testAddEtudiant() {
            Etudiant etudiant = new Etudiant();
            when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

            Etudiant result = etudiantService.addEtudiant(etudiant);
            assertNotNull(result);
            verify(etudiantRepository, times(1)).save(etudiant);
        }

        @Test
        void testRetrieveAllEtudiants() {
            Etudiant etudiant1 = new Etudiant();
            Etudiant etudiant2 = new Etudiant();
            List<Etudiant> etudiants = Arrays.asList(etudiant1, etudiant2);

            when(etudiantRepository.findAll()).thenReturn(etudiants);

            List<Etudiant> result = etudiantService.retrieveAllEtudiants();
            assertEquals(2, result.size());
            verify(etudiantRepository, times(1)).findAll();
        }

        @Test
        void testRetrieveEtudiant() {
            Etudiant etudiant = new Etudiant();
            when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

            Etudiant result = etudiantService.retrieveEtudiant(1L);
            assertNotNull(result);
            verify(etudiantRepository, times(1)).findById(1L);
        }

        @Test
        void testRetrieveNonExistentEtudiant() {
            when(etudiantRepository.findById(99L)).thenReturn(Optional.empty());

            Etudiant result = etudiantService.retrieveEtudiant(99L);
            assertNull(result);
            verify(etudiantRepository, times(1)).findById(99L);
        }

        @Test
        void testUpdateEtudiant() {
            Etudiant etudiant = new Etudiant();
            when(etudiantRepository.save(any(Etudiant.class))).thenReturn(etudiant);

            Etudiant result = etudiantService.modifyEtudiant(etudiant);
            assertNotNull(result);
            verify(etudiantRepository, times(1)).save(etudiant);
        }

        @Test
        void testDeleteEtudiant() {
            Long id = 1L;
            doNothing().when(etudiantRepository).deleteById(id);

            etudiantService.removeEtudiant(id);
            verify(etudiantRepository, times(1)).deleteById(id);
        }

        @Test
        void testRetrieveEtudiantByCin() {
            Etudiant etudiant = new Etudiant();
            long cin = 12345678L;
            when(etudiantRepository.findEtudiantByCinEtudiant(cin)).thenReturn(etudiant);

            Etudiant result = etudiantService.recupererEtudiantParCin(cin);
            assertNotNull(result);
            verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant(cin);
        }

        @Test
        void testRetrieveEtudiantByInvalidCin() {
            long invalidCin = 0L;
            when(etudiantRepository.findEtudiantByCinEtudiant(invalidCin)).thenReturn(null);

            Etudiant result = etudiantService.recupererEtudiantParCin(invalidCin);
            assertNull(result);
            verify(etudiantRepository, times(1)).findEtudiantByCinEtudiant(invalidCin);
        }
    }
