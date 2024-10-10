import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EquipeServiceJUnitTest {

    private EquipeServiceImpl equipeService;
    private EquipeRepository equipeRepository;

    @BeforeEach
    public void setUp() {
        equipeRepository = new EquipeRepository(); // Pas de Mock, implémentation réelle
        equipeService = new EquipeServiceImpl(equipeRepository);
    }

    @Test
    public void testAddEquipe() {
        Equipe equipe = new Equipe("Team A", Niveau.JUNIOR);
        Equipe addedEquipe = equipeService.addEquipe(equipe);

        assertNotNull(addedEquipe);
        assertEquals("Team A", addedEquipe.getNomEquipe());
        assertEquals(Niveau.JUNIOR, addedEquipe.getNiveau());
    }

    @Test
    public void testRetrieveEquipe() {
        Equipe equipe = new Equipe("Team A", Niveau.JUNIOR);
        equipeService.addEquipe(equipe);

        Equipe retrievedEquipe = equipeService.retrieveEquipe(1);
        assertNotNull(retrievedEquipe);
        assertEquals("Team A", retrievedEquipe.getNomEquipe());
    }

    @Test
    public void testDeleteEquipe() {
        Equipe equipe = new Equipe("Team A", Niveau.JUNIOR);
        equipeService.addEquipe(equipe);

        equipeService.deleteEquipe(1);
        assertNull(equipeService.retrieveEquipe(1));
    }

    @Test
    public void testUpdateEquipe() {
        Equipe equipe = new Equipe("Team A", Niveau.JUNIOR);
        equipeService.addEquipe(equipe);

        equipe.setNomEquipe("Updated Team");
        Equipe updatedEquipe = equipeService.updateEquipe(equipe);

        assertEquals("Updated Team", updatedEquipe.getNomEquipe());
    }
}
