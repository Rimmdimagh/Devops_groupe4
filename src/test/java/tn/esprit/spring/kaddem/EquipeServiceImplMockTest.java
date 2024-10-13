package tn.esprit.spring.kaddem;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EquipeServiceImplTest {

    @Autowired
    EquipeServiceImpl equipeService;

    @Test
    @Order(1)
    public void testAddEquipe() {
        // Create a new Equipe entity
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Dream Team");
        equipe.setNiveau(Niveau.JUNIOR);

        // Add the equipe entity
        equipeService.addEquipe(equipe);

        // Verify that the added equipe is not null by retrieving it
        Assertions.assertNotNull(equipeService.retrieveEquipe(equipe.getIdEquipe()));
    }

    @Test
    @Order(2)
    public void testRetrieveAllEquipes() {
        // Retrieve all equipes from the service
        List<Equipe> equipes = equipeService.retrieveAllEquipes();

        // Verify the size of the list is not zero
        Assertions.assertNotEquals(0, equipes.size());
    }

    @Test
    @Order(3)
    public void testUpdateEquipe() {
        // Create and add a new equipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Innovators");
        equipe.setNiveau(Niveau.SENIOR);
        equipeService.addEquipe(equipe);

        // Update the team's name and save
        equipe.setNomEquipe("Innovators Team Updated");
        Equipe updatedEquipe = equipeService.updateEquipe(equipe);

        // Verify that the update was successful
        Assertions.assertEquals("Innovators Team Updated", updatedEquipe.getNomEquipe());
    }

    @Test
    @Order(4)
    public void testDeleteEquipe() {
        // Create and add a new equipe
        Equipe equipe = new Equipe();
        equipe.setNomEquipe("Team to Delete");
        equipe.setNiveau(Niveau.JUNIOR);
        equipeService.addEquipe(equipe);

        // Delete the equipe by id
        equipeService.deleteEquipe(equipe.getIdEquipe());

        // Verify that the equipe is deleted by trying to retrieve it
        Assertions.assertThrows(Exception.class, () -> {
            equipeService.retrieveEquipe(equipe.getIdEquipe());
        });
    }
}
