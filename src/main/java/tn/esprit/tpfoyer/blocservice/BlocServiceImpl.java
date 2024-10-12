package tn.esprit.tpfoyer.blocservice;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BlocServiceImpl {
    BlocRepository blocRepository;

    public List<Bloc> retrieveAllBlocs() {

        List<Bloc> listB = blocRepository.findAll();
        log.info("taille totale : " + listB.size());
        for (Bloc b: listB) {
            log.info("Bloc : " + b);
        }

        return listB;
    }
    @Transactional
    public List<Bloc> retrieveBlocsSelonCapacite(long c) {

        List<Bloc> listB = blocRepository.findAll();
        List<Bloc> listBselonC = new ArrayList<>();

        for (Bloc b: listB) {
            if (b.getCapaciteBloc()>=c)
                listBselonC.add(b);
        }

        return listBselonC;
    }
    @Transactional
    public Bloc retrieveBloc(Long blocId) {

        return blocRepository.findById(blocId).get();
    }
    public Bloc addBloc(Bloc c) {

        return blocRepository.save(c);
    }
    public Bloc modifyBloc(Bloc bloc) {
        return blocRepository.save(bloc);
    }

    public void removeBloc(Long blocId) {
        blocRepository.deleteById(blocId);
    }
    public List<Bloc> trouverBlocsParNomEtCap(String nb, long c) {
        return blocRepository.findAllByNomBlocAndCapaciteBloc(nb,  c);
    }

    public List<Bloc> trouverBlocsSansFoyer() {
        return blocRepository.findAllByIdFoyerIsNull();
    }

}
