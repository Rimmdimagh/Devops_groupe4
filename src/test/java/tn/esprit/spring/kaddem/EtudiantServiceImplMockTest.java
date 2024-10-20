package tn.esprit.spring.kaddem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.Arrays; // Importer Arrays pour utiliser Arrays.asList
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceImplMockTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @BeforeEach
    void setUp() {
        // Initialisation si nécessaire
    }

    @Test
    public void testAssignEtudiantsToEquipe() {
        // Données de test
        List<Integer> etudiantIds = Arrays.asList(1, 2); // Utiliser Arrays.asList
        Integer equipeId = 1;

        // Création d'une équipe et simulation de son comportement
        Equipe equipe = new Equipe();
        equipe.setEtudiants(new HashSet<>());

        // Simulation de la recherche de l'équipe
        when(equipeRepository.findById(equipeId)).thenReturn(Optional.of(equipe));

        // Simulation de la recherche des étudiants
        when(etudiantRepository.findById(1)).thenReturn(Optional.of(new Etudiant("Hbbib", "Brahem")));
        when(etudiantRepository.findById(2)).thenReturn(Optional.of(new Etudiant("Amel", "Amel")));

        // Affichage d'information
        System.out.println("Assignation des étudiants à l'équipe avec ID: " + equipeId);

        // Appel de la méthode
        etudiantService.assignEtudiantsToEquipe(etudiantIds, equipeId);

        // Vérification des résultats
        assertEquals(2, equipe.getEtudiants().size(), "Le nombre d'étudiants dans l'équipe devrait être 2 après l'assignation.");
        verify(equipeRepository).save(equipe);
        System.out.println("L'équipe a été sauvegardée avec succès dans le dépôt.");
    }



/*
    @Test
    public void testFindEtudiantsByDepartement_ValidDepartmentId() {
        // Données de test
        Integer departementId =5; // ID valide
        List<Etudiant> etudiants = Arrays.asList(new Etudiant("Brahem", "Hbib"), new Etudiant("Khelil", "Amel"));

        // Simulation du comportement de l'entrepôt
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(departementId)).thenReturn(etudiants);

        // Affichage d'information
        System.out.println("Recherche des étudiants pour le département avec ID: " + departementId);

        // Appel de la méthode
        List<Etudiant> result = etudiantService.findEtudiantsByDepartement(departementId);

        // Vérification des résultats
        assertEquals(2, result.size(), "Le nombre d'étudiants trouvés devrait être 2 pour le département donné.");
        assertEquals("Brahem", result.get(0).getNomE(), "Le premier étudiant trouvé devrait être 'Brahem'.");
        assertEquals("Khelil", result.get(1).getNomE(), "Le deuxième étudiant trouvé devrait être 'Khelil'.");

        verify(etudiantRepository).findEtudiantsByDepartement_IdDepart(departementId);
        System.out.println("Les étudiants ont été trouvés avec succès pour le département.");
    }

*/

    @Test
    public void testCountEtudiantsInDepartement_ValidDepartmentId() {
        // Données de test
        Integer departementId = 1; // ID valide
        long expectedCount = 5; // Attendu : 5 étudiants trouvés

        // Simulation du comportement du repository
        when(etudiantRepository.countEtudiantsByDepartement_IdDepart(departementId)).thenReturn(expectedCount);

        // Appel de la méthode
        long actualCount = etudiantService.countEtudiantsInDepartement(departementId);

        // Vérification des résultats
        assertEquals(expectedCount, actualCount);

        // Message affiché si des étudiants sont trouvés
        if (actualCount > 0) {
            System.out.println("Test réussi pour countEtudiantsInDepartement : " +
                    "Il y a " + actualCount + " étudiant(s) dans le département ID " + departementId + ".");
        } else {
            System.out.println("Test réussi pour countEtudiantsInDepartement : " +
                    "Aucun étudiant trouvé dans le département ID " + departementId + ".");
        }

        // Vérification que la méthode a été appelée
        verify(etudiantRepository).countEtudiantsByDepartement_IdDepart(departementId);
    }



    @Test
    public void testFindEtudiantInDepartement_ValidStudentAndDepartment() {
        // Données de test
        Integer departementId = 1; // ID valide pour le département
        String studentName = "Brahem"; // Nom de l'étudiant à rechercher
        List<Etudiant> etudiants = Arrays.asList(
                new Etudiant("Brahem", "Hbib"),
                new Etudiant("Khelil", "Amel")
        );

        // Simulation du comportement de l'entrepôt
        when(etudiantRepository.findEtudiantsByDepartement_IdDepart(departementId)).thenReturn(etudiants);

        // Affichage d'information
        System.out.println("Recherche de l'étudiant '" + studentName + "' dans le département avec ID: " + departementId);

        // Appel de la méthode
        List<Etudiant> result = etudiantService.findEtudiantsByDepartement(departementId);

        // Vérification de l'existence de l'étudiant
        boolean exists = result.stream().anyMatch(etudiant -> etudiant.getNomE().equals(studentName));

        // Vérification des résultats
        assertTrue(exists, "L'étudiant '" + studentName + "' devrait exister dans le département.");

        verify(etudiantRepository).findEtudiantsByDepartement_IdDepart(departementId);
        System.out.println("L'étudiant a été trouvé avec succès dans le département.");
    }


}