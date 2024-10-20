package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Option;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class EtudiantServiceImplTest {
    @Autowired
    private EtudiantServiceImpl etudiantService;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private EquipeRepository equipeRepository;
    @Autowired
    private DepartementRepository departementRepository;



    @Test
    @Transactional
    void testCountEtudiantsInDepartement() {
        // Arrange : Créer un département avec des étudiants
        Departement departement = new Departement("Informatique");
        departementRepository.save(departement);

        Etudiant etudiant1 = new Etudiant("Hbib", "Brahem");
        Etudiant etudiant2 = new Etudiant("Ryhem", "Mdimagh");

        etudiant1.setDepartement(departement);
        etudiant2.setDepartement(departement);

        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);

        // Act : Compter les étudiants dans le département (utiliser long au lieu de Integer)
        long nombreEtudiants = etudiantService.countEtudiantsInDepartement(departement.getIdDepart());

        // Assert : Vérifier que le nombre d'étudiants est correct
        assertEquals(2, nombreEtudiants, "Le nombre d'étudiants dans le département est incorrect");

        // Nettoyage
        etudiantRepository.deleteAll();
        departementRepository.delete(departement);

        // Message final
        System.out.println("Test réussi : " + nombreEtudiants + " étudiants trouvés dans le département.");
    }


    @Test
    public void testFindEtudiantInDepartement() {
        // Identifiants de test
        Integer departementId = 1; // ID du département 'informatique'
        Integer etudiantId = 56; // ID de l'étudiant à vérifier

        // Vérifiez d'abord si le département existe
        Departement departement = departementRepository.findById(departementId).orElse(null);
        assertNotNull(departement, "Le département avec l'ID " + departementId + " doit exister.");

        // Rechercher l'étudiant par son ID
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElse(null);

        // Vérifiez si l'étudiant existe dans le département
        if (etudiant != null && etudiant.getDepartement().getIdDepart().equals(departementId)) {
            System.out.println("L'étudiant " + etudiant.getNomE() + " est dans le département " + departement.getNomDepart());
        } else {
            System.out.println("L'étudiant avec l'ID " + etudiantId + " n'est pas disponible dans le département avec l'ID " + departementId);
        }

        // Assert pour vérifier l'existence dans le test
        if (etudiant != null) {
            assertEquals(departementId, etudiant.getDepartement().getIdDepart(),
                    "L'étudiant doit appartenir au département avec l'ID " + departementId);
        } else {
            assertNull(etudiant, "L'étudiant avec l'ID " + etudiantId + " doit être nul.");
        }
    }



}
