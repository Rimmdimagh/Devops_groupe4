package tn.esprit.spring.kaddem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipeServiceImplMockTest {
    @Mock
    private EquipeRepository equipeRepository;

    @Mock
    private EquipeServiceImpl equipeService;



    @Test
    public void testManageEtudiantsInEquipe() {
        // Création d'une nouvelle équipe
        Equipe equipe = new Equipe("Team AI", Niveau.JUNIOR);
        equipe.setEtudiants(new HashSet<>());  // Initialisation de l'ensemble des étudiants

        // Création d'un nouvel étudiant
        Etudiant etudiant1 = new Etudiant("John Doe", "Computer Science");

        // Ajout de l'étudiant à l'équipe
        equipe.getEtudiants().add(etudiant1);
        assertTrue(equipe.getEtudiants().contains(etudiant1), "L'étudiant doit être ajouté à l'équipe");

        // Suppression de l'étudiant de l'équipe
        equipe.getEtudiants().remove(etudiant1);
        assertFalse(equipe.getEtudiants().contains(etudiant1), "L'étudiant doit être retiré de l'équipe");
    }





}
