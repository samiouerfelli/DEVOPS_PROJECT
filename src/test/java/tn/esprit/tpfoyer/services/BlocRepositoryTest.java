//package tn.esprit.tpfoyer.services;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//
//import org.junit.jupiter.api.Test;
//
//import tn.esprit.tpfoyer.entity.*;
//
//import tn.esprit.tpfoyer.repository.*;
//
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//public class BlocRepositoryTest {
//
//    @Autowired
//    private BlocRepository blocRepository;
//
//    @Test
//    public void testFindAllByCapaciteBlocGreaterThan() {
//        // Arrange
//        Bloc bloc = new Bloc();
//        bloc.setNomBloc("Bloc A");
//        bloc.setCapaciteBloc(60);
//        blocRepository.save(bloc);
//
//        // Act
//        List<Bloc> result = blocRepository.findAllByCapaciteBlocGreaterThan(50);
//
//        // Assert
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    public void testFindAllByNomBlocStartingWith() {
//        // Arrange
//        Bloc bloc = new Bloc();
//        bloc.setNomBloc("BlTest");
//        bloc.setCapaciteBloc(40);
//        blocRepository.save(bloc);
//
//        // Act
//        List<Bloc> result = blocRepository.findAllByNomBlocStartingWith("Bl");
//
//        // Assert
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    public void testFindAllByNomBlocAndCapaciteBloc() {
//        // Arrange
//        Bloc bloc = new Bloc();
//        bloc.setNomBloc("Bloc B");
//        bloc.setCapaciteBloc(30);
//        blocRepository.save(bloc);
//
//        // Act
//        List<Bloc> result = blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc B", 30);
//
//        // Assert
//        assertFalse(result.isEmpty());
//    }
//
//    @Test
//    public void testFindAllByFoyerIsNull() {
//        // Arrange
//        Bloc bloc = new Bloc();
//        bloc.setNomBloc("Bloc C");
//        bloc.setCapaciteBloc(20);
//        blocRepository.save(bloc);
//
//        // Act
//        List<Bloc> result = blocRepository.findAllByFoyerIsNull();
//
//        // Assert
//        assertFalse(result.isEmpty());
//    }
//}