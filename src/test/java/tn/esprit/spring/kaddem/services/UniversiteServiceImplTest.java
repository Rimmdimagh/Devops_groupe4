package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Universite;
import tn.esprit.spring.kaddem.repositories.DepartementRepository; // Import the DepartementRepository
import tn.esprit.spring.kaddem.repositories.UniversiteRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceImplTest {

    @Mock
    UniversiteRepository universiteRepository;

    @Mock
    DepartementRepository departementRepository; // Mock for DepartementRepository

    @InjectMocks
    UniversiteServiceImpl universiteService;

    Universite universite;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universite = new Universite(1, "Université de Test");
    }

    @Test
    void retrieveAllUniversites() {
        when(universiteRepository.findAll()).thenReturn(List.of(universite));
        List<Universite> universites = universiteService.retrieveAllUniversites();
        assertNotNull(universites);
        assertEquals(1, universites.size());
    }

    @Test
    void addUniversite() {
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);
        Universite newUniv = universiteService.addUniversite(universite);
        assertNotNull(newUniv);
        assertEquals("Université de Test", newUniv.getNomUniv());
    }

    @Test
    void updateUniversite() {
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);
        Universite updatedUniv = universiteService.updateUniversite(universite);
        assertNotNull(updatedUniv);
        assertEquals("Université de Test", updatedUniv.getNomUniv());
    }

    @Test
    void retrieveUniversite() {
        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        Universite foundUniv = universiteService.retrieveUniversite(1);
        assertNotNull(foundUniv);
        assertEquals("Université de Test", foundUniv.getNomUniv());
    }



    @Test
    void assignUniversiteToDepartement() {
        Departement departement = new Departement();
        universite.setDepartements(new HashSet<>(Set.of(departement)));

        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(any(Universite.class))).thenReturn(universite);
        when(departementRepository.findById(1)).thenReturn(Optional.of(departement)); // Mock retrieval for departement

        universiteService.assignUniversiteToDepartement(1, 1);
        assertEquals(1, universite.getDepartements().size());
    }

    @Test
    void retrieveDepartementsByUniversite() {
        Departement departement1 = new Departement();
        Departement departement2 = new Departement();
        Set<Departement> departements = new HashSet<>(Set.of(departement1, departement2));
        universite.setDepartements(departements);

        when(universiteRepository.findById(1)).thenReturn(Optional.of(universite));
        Set<Departement> foundDepartements = universiteService.retrieveDepartementsByUniversite(1);
        assertEquals(2, foundDepartements.size());
    }
}
