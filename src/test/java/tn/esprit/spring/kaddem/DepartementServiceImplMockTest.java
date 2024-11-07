package tn.esprit.spring.kaddem;


import tn.esprit.spring.kaddem.entities.Departement;

import tn.esprit.spring.kaddem.entities.Etudiant;
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
 class DepartementServiceImplMockTest {

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
        Departement departement2 = new Departement("electronique");
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
     void testRemoveEtudiantFromDepartement() {
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

    @Test
     void testIsEtudiantInDepartement() {
        Integer etudiantId1 = 1;
        Integer etudiantId2 = 2; // Étudiant dans un autre département
        Integer etudiantIdNotFound = 3; // Étudiant non trouvé
        Integer departementId = 1; // ID du département

        // Cree un département
        Departement departement = new Departement(departementId, "Marketing");

        // cree un étudiant  qui appartient au département
        Etudiant etudiant1 = new Etudiant("Rim", "Mdimagh");
        etudiant1.setDepartement(departement); // L'étudiant appartient au département

        // Créer un étudiant n'appartient pas au depart
        Etudiant etudiant2 = new Etudiant("Amel", "Khelil");
        etudiant2.setDepartement(new Departement(2, "Design")); // L'étudiant appartient à un autre département

        // Simulation du comportement du repository
        when(etudiantRepository.findById(etudiantId1)).thenReturn(Optional.of(etudiant1));
        when(etudiantRepository.findById(etudiantId2)).thenReturn(Optional.of(etudiant2));
        when(etudiantRepository.findById(etudiantIdNotFound)).thenReturn(Optional.empty());

        // Test du premier cas etudiant  dans le département
        boolean result1 = departementService.isEtudiantInDepartement(etudiantId1, departementId);
        System.out.println("Test 1 - Étudiant ID " + etudiantId1 + ": " + result1);
        assertTrue(result1, "L'étudiant doit être dans le département");

        // Test du deuxième cas etudiant dans un autre département
        boolean result2 = departementService.isEtudiantInDepartement(etudiantId2, departementId);
        System.out.println("Test 2 - Étudiant ID " + etudiantId2 + ": " + result2);
        assertFalse(result2, "L'étudiant ne doit pas être dans le département");

        // Test du troisième cas Étudiant non trouvé
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            departementService.isEtudiantInDepartement(etudiantIdNotFound, departementId);
        });
        System.out.println("Test 3 - Étudiant ID " + etudiantIdNotFound + ": Exception attendue");
        assertEquals("L'étudiant n'existe pas", exception.getMessage());
    }



}
