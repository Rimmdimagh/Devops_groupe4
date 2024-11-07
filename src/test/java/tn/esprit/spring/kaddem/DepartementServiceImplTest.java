package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.DepartementServiceImpl;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class DepartementServiceImplTest {

    @Autowired
    private DepartementServiceImpl departementService;

    @Autowired
    private DepartementRepository departementRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;


    @Test
    @Transactional
    void testAffectDepartementToEtudiants() {
        // Arrange : Initialiser le département et les étudiants
        Departement departement1 = new Departement("Marketing");

        Etudiant etudiant1 = new Etudiant("Rim", "Mdimagh");
        Etudiant etudiant2 = new Etudiant("Amel", "Khelil");

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);

        // Sauvegarder le département et les étudiants
        departement1.setEtudiants(etudiants);
        departementRepository.save(departement1);
        etudiantRepository.saveAll(etudiants);

        // Act : Vérifier que le département contient bien le nombre d'étudiants affectés
        Departement savedDepartement = departementService.retrieveDepartement(departement1.getIdDepart());

        // Assert : Vérifier que le département contient bien le nombre d'étudiants
        assertEquals(etudiants.size(), savedDepartement.getEtudiants().size(),
                "Le nombre d'étudiants dans le département est incorrect");

        // Nettoyage
        departementRepository.delete(savedDepartement);

        // Message final
        System.out.println("Test terminé : Les étudiants ont bien été affectés au département.");
    }

    @Test
    @Transactional
    void testRemoveEtudiantFromDepartement() {
        // Arrange : Créer un département et un étudiant
        Departement departement1 = new Departement("Informatique");
        departementRepository.save(departement1);  // Sauvegarder le département

        Etudiant etudiant = new Etudiant("Rim", "Mdimagh");
        etudiant.setDepartement(departement1);
        etudiantRepository.save(etudiant);  // Sauvegarder l'étudiant

        // Assurer que l'étudiant est bien affecté à un département
        assertNotNull(etudiant.getDepartement(), "L'étudiant devrait être affecté à un département.");

        // Act : Retirer l'étudiant du département
        departementService.removeEtudiantFromDepartement(etudiant.getIdEtudiant());

        // Recharger l'étudiant pour vérifier les modifications
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getIdEtudiant()).orElseThrow(() ->
                new RuntimeException("L'étudiant n'existe pas après suppression"));

        // Assert : Vérifier que l'étudiant n'est plus affecté à un département
        assertNull(updatedEtudiant.getDepartement(), "L'étudiant ne devrait plus être affecté à un département.");

        // Nettoyage
        etudiantRepository.delete(updatedEtudiant);
        departementRepository.delete(departement1);

        // Message final
        System.out.println("Test terminé : L'étudiant a bien été retiré du département.");
    }

    @Test
    @Transactional
    void testCountEtudiantsInDepartement() {
        // Arrange : Créer un département avec des étudiants
        Departement departement1 = new Departement("Informatique");

        Etudiant etudiant1 = new Etudiant("Ahmed", "Ahmed");
        Etudiant etudiant2 = new Etudiant("Ryhem", "Mdimagh");

        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(etudiant1);
        etudiants.add(etudiant2);

        departement1.setEtudiants(etudiants);
        Departement savedDepartement = departementService.addDepartement(departement1);

        // Act : Compter les étudiants dans le département
        Integer nombreEtudiants = departementService.countEtudiantsInDepartement(savedDepartement.getIdDepart());

        // Assert : Vérifier que le nombre d'étudiants est correct
        assertEquals(etudiants.size(), nombreEtudiants, "Le nombre d'étudiants dans le département est incorrect");

        // Nettoyage
        departementRepository.delete(savedDepartement);

        // Message final
        System.out.println("Test réussi : " + nombreEtudiants + " étudiants trouvés dans le département.");
    }






}
