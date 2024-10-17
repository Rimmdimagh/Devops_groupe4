package tn.esprit.spring.kaddem;

import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Departement;

import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartementServiceImplMockTest {

    @Mock
    private DepartementRepository departementRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;


    @Test
    void testRetrieveAllDepartements() {
        // Préparation des données de test
        Departement departement1 = new Departement("Marketing");
        Departement departement2 = new Departement("électronique");
        List<Departement> expectedDepartements = Arrays.asList(departement1, departement2);

        // Simulation du comportement du repository avec Mockito
        when(departementRepository.findAll()).thenReturn(expectedDepartements);

        // Appel de la méthode de service pour récupérer les départements
        List<Departement> actualDepartements = departementService.retrieveAllDepartements();

        // Affichage détaillé des départements récupérés
        System.out.println("Liste des départements récupérés :");
        actualDepartements.forEach(departement ->
                System.out.println("Nom du département : " + departement.getNomDepart())
        );

        // Vérification du résultat
        assertEquals(expectedDepartements, actualDepartements, "Les départements récupérés devraient correspondre aux départements attendus.");

        // Vérification que le repository a bien été appelé une seule fois
        verify(departementRepository, times(1)).findAll();
    }

    // Test de l'affectation d'un département à plusieurs étudiants
    @Test
    void testAffectDepartementToEtudiants() {
        // Préparation des données de test
        Integer departementId = 0; // Changez cette valeur pour tester différents scénarios
        List<Integer> etudiantIds = Arrays.asList(1, 2, 3);

        // Vérifiez si le département ID est valide ou non
        if (departementId == 0) {
            // Test avec un id de département invalide
            Exception exception = assertThrows(RuntimeException.class, () -> {
                departementService.affectDepartementToEtudiants(departementId, etudiantIds);
            });

            // Vérification que l'exception contient le bon message
            assertEquals("Département non trouvé", exception.getMessage());
            System.out.println("Test échoué car le département ID est 0 : " + exception.getMessage());
        } else {
            // Préparation des objets de test pour le cas où departementId est valide
            Departement departementInformatique = new Departement(departementId, "Informatique");
            Etudiant etudiant1 = new Etudiant("Rim", "Mdimagh");
            Etudiant etudiant2 = new Etudiant("Amel", "Khelil");
            Etudiant etudiant3 = new Etudiant("Ryhem", "Mdimagh");

            // Simulation du comportement des repositories pour le département et les étudiants
            when(departementRepository.findById(departementId)).thenReturn(Optional.of(departementInformatique));
            when(etudiantRepository.findById(1)).thenReturn(Optional.of(etudiant1));
            when(etudiantRepository.findById(2)).thenReturn(Optional.of(etudiant2));
            when(etudiantRepository.findById(3)).thenReturn(Optional.of(etudiant3));

            // Appel de la méthode du service qui affecte les étudiants au département
            departementService.affectDepartementToEtudiants(departementId, etudiantIds);

            // Vérification que chaque étudiant a bien été affecté au département "Informatique"
            assertEquals(departementInformatique, etudiant1.getDepartement());
            assertEquals(departementInformatique, etudiant2.getDepartement());
            assertEquals(departementInformatique, etudiant3.getDepartement());

            System.out.println("Les étudiants ont été correctement affectés au département : " + departementInformatique.getNomDepart());
        }
    }





    // Test de la dissociation d'un étudiant de son département
    @Test
    public void testRemoveEtudiantFromDepartement() {
        // Préparation des données de test
        Integer etudiantId = 100;

        // Création d'un étudiant avec un département déjà associé
        Etudiant etudiant = new Etudiant();
        etudiant.setIdEtudiant(etudiantId);
        Departement departement = new Departement(); // Le département initial de l'étudiant
        etudiant.setDepartement(departement);

        // Simulation du comportement du repository pour l'étudiant
        when(etudiantRepository.findById(etudiantId)).thenReturn(Optional.of(etudiant));

        // Appel de la méthode du service qui retire l'étudiant de son département
        departementService.removeEtudiantFromDepartement(etudiantId);

        // Vérification que le département de l'étudiant est maintenant "null" (dissocié)
        assertNull(etudiant.getDepartement());

        // Vérification que l'étudiant a bien été sauvegardé après la dissociation
        verify(etudiantRepository, times(1)).save(etudiant);
    }




    @Test
    void testCountEtudiantsInDepartement() {
        // Préparation des données de test
        Integer departementId = 2; // Changez cette valeur pour tester différents scénarios

        if (departementId == 0) {
            // Test avec un id de département invalide
            Exception exception = assertThrows(RuntimeException.class, () -> {
                departementService.countEtudiantsInDepartement(departementId);
            });

            // Vérification que l'exception contient le bon message
            assertEquals("Aucun département trouvé", exception.getMessage());
            System.out.println("Test échoué car le département ID est 0 : " + exception.getMessage());
        } else {
            // Créer un département avec des étudiants simulés
            Departement departement = new Departement(departementId, "Informatique");

            // Création d'étudiants simulés et ajout au département
            Etudiant etudiant1 = new Etudiant("Rim", "Mdimagh");
            Etudiant etudiant2 = new Etudiant("Amel", "Khelil");

            Set<Etudiant> etudiants = new HashSet<>(Arrays.asList(etudiant1, etudiant2));  // 2 étudiants
            departement.setEtudiants(etudiants);

            // Simulation du repository pour retourner le département avec ses étudiants
            when(departementRepository.findById(departementId)).thenReturn(Optional.of(departement));

            // Appel de la méthode du service pour compter les étudiants
            Integer nombreEtudiants = departementService.countEtudiantsInDepartement(departementId);

            // Vérification que la méthode retourne bien le nombre correct d'étudiants (ici 2)
            assertEquals(departement.getEtudiants().size(), nombreEtudiants); // Utilise la taille de la collection


            // Vérification que le repository a bien été appelé une fois pour récupérer le département
            verify(departementRepository, times(1)).findById(departementId);

            System.out.println("Le nombre d'étudiants dans le département " + departement.getNomDepart() + " est : " + nombreEtudiants);
        }
    }


}
