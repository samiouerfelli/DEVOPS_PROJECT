package tn.esprit.tpfoyer.foyerservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FoyerServiceImpl   {

    FoyerRepository foyerRepository;
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }
    public Foyer retrieveFoyer(Long foyerId) {
        return foyerRepository.findById(foyerId).get();
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