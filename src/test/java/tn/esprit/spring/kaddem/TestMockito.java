package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.ContratServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestMockito {
    @Mock
    private ContratRepository contratRepository;
    @Mock
    private EtudiantRepository etudiantRepository;
    @InjectMocks
    private ContratServiceImpl contratService;

    private Contrat contrat;
    private Etudiant etudiant;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mock objects
        contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setArchive(false);

        etudiant = new Etudiant();
        etudiant.setNomE("John");
        etudiant.setPrenomE("Doe");
        Set<Contrat> contrats = new HashSet<>();
        etudiant.setContrats(contrats);
    }
    @Test
    void testAffectContratToEtudiant() {
        // Arrange
        when(etudiantRepository.findByNomEAndPrenomE("John", "Doe")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1)).thenReturn(contrat);

        // Act
        Contrat result = contratService.affectContratToEtudiant(1, "John", "Doe");

        // Assert
        assertNotNull(result);
        assertEquals(etudiant, result.getEtudiant());
        verify(contratRepository, times(1)).save(contrat);
    }
    @Test
    void testUpdateContrat() {
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat updatedContrat = contratService.updateContrat(contrat);

        assertNotNull(updatedContrat);
        verify(contratRepository, times(1)).save(contrat);
    }
    @Test
    void testAddContrat() {
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat newContrat = contratService.addContrat(contrat);

        assertNotNull(newContrat);
        verify(contratRepository, times(1)).save(contrat);
    }
    @Test
    void testRetrieveContrat() {
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));

        Contrat retrievedContrat = contratService.retrieveContrat(1);

        assertNotNull(retrievedContrat);
        assertEquals(1, retrievedContrat.getIdContrat());
    }@Test
    void testRemoveContrat() {
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        doNothing().when(contratRepository).delete(contrat);

        contratService.removeContrat(1);

        verify(contratRepository, times(1)).delete(contrat);
    }
    @Test
    void testNbContratsValides() {
        Date startDate = new Date();
        Date endDate = new Date();
        when(contratRepository.getnbContratsValides(startDate, endDate)).thenReturn(5);

        Integer result = contratService.nbContratsValides(startDate, endDate);

        assertEquals(5, result);
        verify(contratRepository, times(1)).getnbContratsValides(startDate, endDate);
    }
    @Test
    void testRetrieveAndUpdateStatusContrat() {
        // Configuration d'un contrat dont la date de fin est aujourd'hui (0 jours restants)
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -0);  // Fixe la date de fin à aujourd'hui
        contrat.setDateFinContrat(cal.getTime());
        contrat.setArchive(false);

        // Simuler la réponse du repository en retournant ce contrat
        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat));

        // Appeler la méthode
        contratService.retrieveAndUpdateStatusContrat();

        // Vérifier que la méthode save() a bien été appelée pour archiver le contrat
        verify(contratRepository, times(1)).save(contrat);
        assertTrue(contrat.getArchive());
    }
    @Test
    void testGetChiffreAffaireEntreDeuxDates() {
        // Simulate a 30-day difference
        Calendar cal = Calendar.getInstance();
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 30);
        Date endDate = cal.getTime();

        // Set up the contract with IA specialty
        contrat.setSpecialite(Specialite.IA);

        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat));

        float result = contratService.getChiffreAffaireEntreDeuxDates(startDate, endDate);

        // For IA: 300 * 1 month = 300
        assertEquals(300.0f, result);
    }

    }
