package tn.esprit.spring.kaddem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipeServiceImplMockTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeServiceImpl;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEvoluerEquipesToSenior() {
        // Setup data for the test
        Equipe equipeJunior = new Equipe("Junior Team", Niveau.JUNIOR);
        Etudiant etudiant = new Etudiant();
        Contrat contratActif = new Contrat();
        contratActif.setDateFinContrat(new Date(System.currentTimeMillis() - 2 * 365 * 24 * 60 * 60 * 1000L)); // Contrat de 2 ans
        contratActif.setArchive(false);
        etudiant.setContrats(Set.of(contratActif));
        equipeJunior.setEtudiants(Set.of(etudiant));

        when(equipeRepository.findAll()).thenReturn(List.of(equipeJunior));

        // Call the method under test
        equipeServiceImpl.evoluerEquipes();

        // Verify the team has evolved to SENIOR
        assertEquals(Niveau.SENIOR, equipeJunior.getNiveau());
        verify(equipeRepository).save(equipeJunior);
    }

    @Test
    void testEvoluerEquipesToExpert() {
        // Setup data for the test
        Equipe equipeSenior = new Equipe("Senior Team", Niveau.SENIOR);
        Etudiant etudiant = new Etudiant();
        Contrat contratActif = new Contrat();
        contratActif.setDateFinContrat(new Date(System.currentTimeMillis() - 3 * 365 * 24 * 60 * 60 * 1000L)); // Contrat de 3 ans
        contratActif.setArchive(false);
        etudiant.setContrats(Set.of(contratActif));
        equipeSenior.setEtudiants(Set.of(etudiant));

        when(equipeRepository.findAll()).thenReturn(List.of(equipeSenior));

        // Call the method under test
        equipeServiceImpl.evoluerEquipes();

        // Verify the team has evolved to EXPERT
        assertEquals(Niveau.EXPERT, equipeSenior.getNiveau());
        verify(equipeRepository).save(equipeSenior);
    }
}
