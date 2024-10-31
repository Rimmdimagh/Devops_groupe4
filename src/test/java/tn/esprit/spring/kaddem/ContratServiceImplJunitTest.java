package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;
import java.util.Optional;

import javax.transaction.Transactional;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class ContratServiceImplJunitTest {

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ContratServiceImpl contratService;

    private Contrat contrat;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        // Initialize real objects
        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setDateDebutContrat(new Date()); // Set the start date
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30))); // Set the end date (30 days later)
        contrat.setSpecialite(Specialite.IA); // Set the specialite
        contrat.setMontantContrat(1000); // Set the amount
        contrat.setArchive(false); // Set archive status

        etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");
        Set<Contrat> contrats = new HashSet<>();
        etudiant.setContrats(contrats);
    }



   /* @Test
    @Transactional
    public void testAffectContratToEtudiant() {
        // Arrange: Create and save a unique student
        Etudiant etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");
        etudiantRepository.save(etudiant);

        // Create and save a contract
        Contrat contrat = new Contrat();
        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);

        // Act: Call the method to affect the contract to the student
        Contrat result = contratService.affectContratToEtudiant(contrat.getIdContrat(), etudiant.getNomE(), etudiant.getPrenomE());

        // Assert: Check that the contract is correctly affected
        assertNotNull(result);
        assertEquals(etudiant.getNomE(), result.getEtudiant().getNomE());
    }

*/

    @Test
    @Transactional
    void testAddContrat() {
        // Act: Add a new contract
        Contrat newContrat = contratService.addContrat(contrat);

        // Assert: Verify that the contract was added
        assertNotNull(newContrat, "The new contract should not be null");
        assertEquals(contrat.getIdContrat(), newContrat.getIdContrat(), "The contract ID should match");
    }

    @Test
    @Transactional
    void testRetrieveContrat() {
        // Arrange: Save the contract
        contratRepository.save(contrat);

        // Act: Retrieve the contract
        Contrat retrievedContrat = contratService.retrieveContrat(contrat.getIdContrat());

        // Assert: Verify that the contract was retrieved correctly
        assertNotNull(retrievedContrat, "The retrieved contract should not be null");
        assertEquals(contrat.getIdContrat(), retrievedContrat.getIdContrat(), "The contract ID should match");
    }

   /* @Test
    @Transactional
    void testRemoveContrat() {
        // Arrange: Save the contract
        contratRepository.save(contrat);

        // Act: Remove the contract
        contratService.removeContrat(contrat.getIdContrat());

        // Assert: Verify that the contract was removed
        Optional<Contrat> removedContrat = contratRepository.findById(contrat.getIdContrat());
        assertTrue(removedContrat.isEmpty(), "The contract should be removed from the repository");
    }

   /* @Test
    @Transactional
    void testNbContratsValides() {
        // Arrange: Save the contract
        contratRepository.save(contrat);

        // Act: Get the number of valid contracts
        Integer result = contratService.nbContratsValides(new Date(), new Date());

        // Assert: Check that the number of valid contracts is correct
        assertEquals(1, result, "There should be 1 valid contract in the date range");
    }*/

    @Test
    @Transactional
    void testRetrieveAndUpdateStatusContrat() {
        // Arrange: Set the contract end date to today and save it
        Calendar cal = Calendar.getInstance();
        contrat.setDateFinContrat(cal.getTime());
        contratRepository.save(contrat);

        // Act: Call the method to update contract status
        contratService.retrieveAndUpdateStatusContrat();

        // Assert: Verify that the contract was archived
        Contrat updatedContrat = contratRepository.findById(contrat.getIdContrat()).get();
        assertTrue(updatedContrat.getArchive(), "The contract should be archived");
    }

    @Test
    @Transactional
    void testGetChiffreAffaireEntreDeuxDates() {
        // Arrange: Set up a date range of 30 days and assign a specialty to the contract
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date endDate = cal.getTime();

        contrat.setSpecialite(Specialite.IA);
        contratRepository.save(contrat);

        // Act: Calculate the revenue for the contract
        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert: Verify that the revenue is correctly calculated
        assertEquals(300.0f, result, "The revenue for 1 month of IA specialty should be 300.0");
    }
}
