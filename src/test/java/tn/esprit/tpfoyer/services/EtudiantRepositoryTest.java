package tn.esprit.tpfoyer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import org.junit.jupiter.api.Test;

import tn.esprit.tpfoyer.entity.Etudiant;

import tn.esprit.tpfoyer.repository.EtudiantRepository;


import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class EtudiantRepositoryTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Test
    void testFindEtudiantByCinEtudiant() {
        // Arrange
        long cin = 12345678L;
        Etudiant etudiant = new Etudiant();
        etudiant.setCinEtudiant(cin);
        etudiantRepository.save(etudiant);

        // Act
        Etudiant foundEtudiant = etudiantRepository.findEtudiantByCinEtudiant(cin);

        // Assert
        assertNotNull(foundEtudiant);
        assertEquals(cin, foundEtudiant.getCinEtudiant());
    }
}