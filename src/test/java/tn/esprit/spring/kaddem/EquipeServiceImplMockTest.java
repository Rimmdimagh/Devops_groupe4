package tn.esprit.spring.kaddem;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EquipeServiceImplMockTest {

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @Mock
    private EquipeRepository equipeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveAllEquipes() {
        // Arrange
        List<Equipe> equipes = new ArrayList<>();
        equipes.add(new Equipe("Equipe A", Niveau.JUNIOR));
        equipes.add(new Equipe("Equipe B", Niveau.SENIOR));

        when(equipeRepository.findAll()).thenReturn(equipes);

        // Act
        List<Equipe> result = equipeService.retrieveAllEquipes();

        // Assert
        assertEquals(2, result.size());
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    void testAddEquipe() {
        // Arrange
        Equipe equipe = new Equipe("Equipe C", Niveau.JUNIOR);
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Act
        Equipe result = equipeService.addEquipe(equipe);

        // Assert
        assertNotNull(result);
        assertEquals("Equipe C", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testDeleteEquipe() {
        // Arrange
        Equipe equipe = new Equipe("Equipe D", Niveau.SENIOR);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Act
        equipeService.deleteEquipe(1);

        // Assert
        verify(equipeRepository, times(1)).delete(equipe);
    }

    @Test
    void testRetrieveEquipe() {
        // Arrange
        Equipe equipe = new Equipe("Equipe E", Niveau.EXPERT);
        when(equipeRepository.findById(1)).thenReturn(Optional.of(equipe));

        // Act
        Equipe result = equipeService.retrieveEquipe(1);

        // Assert
        assertNotNull(result);
        assertEquals("Equipe E", result.getNomEquipe());
        verify(equipeRepository, times(1)).findById(1);
    }

    @Test
    void testUpdateEquipe() {
        // Arrange
        Equipe equipe = new Equipe("Equipe F", Niveau.JUNIOR);
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // Act
        Equipe result = equipeService.updateEquipe(equipe);

        // Assert
        assertNotNull(result);
        assertEquals("Equipe F", result.getNomEquipe());
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    void testEvoluerEquipes() {
        // Arrange
        Equipe equipe = new Equipe("Equipe G", Niveau.JUNIOR);
        equipe.setEtudiants(new HashSet<>());
        when(equipeRepository.findAll()).thenReturn(List.of(equipe));

        // Mocking the active contract check
        // Simulating the case of having 3 active contracts
        // This part would require more setup if you want to make it realistic

        // Act
        equipeService.evoluerEquipes();

        // Assert
        assertEquals(Niveau.SENIOR, equipe.getNiveau());
        verify(equipeRepository, times(1)).save(equipe);
    }
}

