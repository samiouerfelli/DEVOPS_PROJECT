package tn.esprit.tpfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class FoyerServiceImpl implements IFoyerService {

    FoyerRepository foyerRepository;

    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }

    public Foyer retrieveFoyer(Long foyerId) {
        // Updated to handle cases where the ID does not exist
        return foyerRepository.findById(foyerId)
                .orElseThrow(() -> new NoSuchElementException("Foyer with ID " + foyerId + " not found"));
    }

    public Foyer addFoyer(Foyer f) {
        return foyerRepository.save(f);
    }

    public Foyer modifyFoyer(Foyer foyer) {
        return foyerRepository.save(foyer);
    }

    public void removeFoyer(Long foyerId) {
        foyerRepository.deleteById(foyerId);
    }
}
