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

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = KaddemApplication.class)
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
        contrat = new Contrat();
        contrat.setDateDebutContrat(new Date()); // Set start date to now
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30))); // 30 days later
        contrat.setSpecialite(Specialite.IA); // Assign specialite
        contrat.setMontantContrat(1000); // Assign montant
        contrat.setArchive(false); // Initially not archived

        etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");
        etudiant.setContrats(new HashSet<>()); // Initialize empty contracts set
    }

    @Test
    @Transactional
    void testAddContrat() {
        // Act: Add a new contract
        Contrat newContrat = contratService.addContrat(contrat);

        // Assert: Verify the contract is added
        assertNotNull(newContrat, "The new contract should not be null");
        assertEquals(contrat.getSpecialite(), newContrat.getSpecialite(), "The contract specialite should match");
    }

    @Test
    @Transactional
    void testRetrieveContrat() {
        // Arrange: Save the contract
        Contrat savedContrat = contratService.addContrat(contrat);

        // Act: Retrieve the contract
        Contrat retrievedContrat = contratService.retrieveContrat(savedContrat.getIdContrat());

        // Assert: Verify the contract is retrieved successfully
        assertNotNull(retrievedContrat, "The retrieved contract should not be null");
        assertEquals(savedContrat.getIdContrat(), retrievedContrat.getIdContrat(), "The contract ID should match");
    }

    @Test
    @Transactional
    void testRemoveContrat() {
        // Arrange: Save the contract
        Contrat savedContrat = contratService.addContrat(contrat);

        // Act: Remove the contract
        contratService.removeContrat(savedContrat.getIdContrat());

        // Assert: Verify the contract is removed
        Contrat removedContrat = contratService.retrieveContrat(savedContrat.getIdContrat());
        assertNull(removedContrat, "The contract should be null after removal");
    }

    @Test
    @Transactional
    void testAffectContratToEtudiant() {
        // Arrange: Save the student and contract
        etudiantRepository.save(etudiant);
        Contrat savedContrat = contratService.addContrat(contrat);

        // Act: Assign the contract to the student
        Contrat updatedContrat = contratService.affectContratToEtudiant(
                savedContrat.getIdContrat(),
                etudiant.getNomE(),
                etudiant.getPrenomE()
        );

        // Assert: Verify the contract is assigned to the student
        assertNotNull(updatedContrat.getEtudiant(), "The student should be assigned to the contract");
        assertEquals(etudiant.getNomE(), updatedContrat.getEtudiant().getNomE(), "The student's name should match");
    }

    @Test
    @Transactional
    void testNbContratsValides() {
        // Arrange: Save the contract within a valid date range
        contrat.setDateDebutContrat(new Date());
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 10))); // 10 days later
        contratService.addContrat(contrat);

        // Act: Retrieve number of valid contracts
        Integer validContracts = contratService.nbContratsValides(
                new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24)), // Yesterday
                new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 15)) // 15 days later
        );

        // Assert: Verify the number of valid contracts
        assertEquals(1, validContracts, "The number of valid contracts should be 1");
    }


    @Test
    @Transactional
    void testRetrieveAndUpdateStatusContrat() {
        // Arrange: Create and save a contract with an expired date
        Contrat expiredContrat = new Contrat();
        expiredContrat.setDateDebutContrat(new Date());
        expiredContrat.setDateFinContrat(new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))); // 1 day past expiry
        expiredContrat.setSpecialite(Specialite.IA);
        expiredContrat.setArchive(false);
        expiredContrat.setMontantContrat(1000);
        contratRepository.save(expiredContrat);

        // Act: Call the method to update contract status
        contratService.retrieveAndUpdateStatusContrat();

        // Assert: Verify the contract is archived
        Contrat updatedContrat = contratRepository.findById(expiredContrat.getIdContrat()).orElse(null);
        assertNotNull(updatedContrat, "The contract should exist");
        assertTrue(updatedContrat.getArchive(), "The contract should be archived");
    }


    @Test
    @Transactional
    void testGetChiffreAffaireEntreDeuxDates() {
        // Arrange: Create and save contracts
        Contrat contratIA = new Contrat();
        contratIA.setDateDebutContrat(new Date());
        contratIA.setDateFinContrat(new Date());
        contratIA.setSpecialite(Specialite.IA);
        contratIA.setArchive(false);
        contratIA.setMontantContrat(1000);
        contratRepository.save(contratIA);

        Date startDate = new Date(System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 30)); // 1 month ago
        Date endDate = new Date();

        // Act: Calculate revenue
        float revenue = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // Assert: Verify revenue is correct
        assertEquals(300.0f, revenue, "The revenue for one IA contract for one month should be 300.0");
    }

}
