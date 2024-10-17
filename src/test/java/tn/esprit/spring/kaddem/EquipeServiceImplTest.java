package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EquipeServiceImplTest {

    @Autowired
    private EquipeServiceImpl equipeService;

    @Autowired
    private EquipeRepository equipeRepository;

    @Test
    @Transactional
    public void testRetrieveAndUpdateEquipe() {
        // Création de l'équipe pour le test
        Equipe equipe = new Equipe("Team A");
        equipe = equipeService.addEquipe(equipe);

        // Récupération et mise à jour
        Equipe found = equipeService.retrieveEquipe(equipe.getIdEquipe());
        assertNotNull(found);
        assertEquals("Team A", found.getNomEquipe());

        found.setNomEquipe("Team A Updated");
        Equipe updated = equipeService.updateEquipe(found);
        assertEquals("Team A Updated", updated.getNomEquipe());

        // Vérification après mise à jour
        Equipe rechecked = equipeService.retrieveEquipe(updated.getIdEquipe());
        assertEquals("Team A Updated", rechecked.getNomEquipe());

        // Suppression et vérification
        equipeService.deleteEquipe(rechecked.getIdEquipe());
        Exception exception = assertThrows(RuntimeException.class, () -> equipeService.retrieveEquipe(rechecked.getIdEquipe()));
        //assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    @Transactional
    public void testRetrieveAllEquipes() {


        // Préparer les données de test
        Equipe teamA = new Equipe("Team A");
        Equipe teamB = new Equipe("Team B");
        equipeService.addEquipe(teamA);
        equipeService.addEquipe(teamB);

        // Action: Récupérer toutes les équipes
        List<Equipe> equipes = equipeService.retrieveAllEquipes();
        assertNotNull(equipes, "La liste des équipes ne doit pas être null");


        // Vérifier que les données récupérées sont correctes
        assertTrue(equipes.stream().anyMatch(e -> e.getNomEquipe().equals("Team A")),
                "La liste doit contenir Team A");
        assertTrue(equipes.stream().anyMatch(e -> e.getNomEquipe().equals("Team B")),
                "La liste doit contenir Team B");


    }


}