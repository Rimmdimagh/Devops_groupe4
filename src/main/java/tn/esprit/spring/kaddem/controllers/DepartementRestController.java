package tn.esprit.spring.kaddem.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.DepartementDTO;
import tn.esprit.spring.kaddem.services.IDepartementService;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/departement")
public class DepartementRestController {
	IDepartementService departementService;
	// http://localhost:8089/Kaddem/departement/retrieve-all-departements
	@GetMapping("/retrieve-all-departements")
	public List<Departement> getDepartements() {
		List<Departement> listDepartements = departementService.retrieveAllDepartements();
		return listDepartements;
	}
	// http://localhost:8089/Kaddem/departement/retrieve-departement/8
	@GetMapping("/retrieve-departement/{departement-id}")
	public Departement retrieveDepartement(@PathVariable("departement-id") Integer departementId) {
		return departementService.retrieveDepartement(departementId);
	}

	// http://localhost:8089/Kaddem/departement/add-departement
	@PostMapping("/add-departement")
	public Departement addDepartement(@RequestBody DepartementDTO DepartementDTO) {

		Departement departement = new Departement();
		departement.setNomDepart(DepartementDTO.getNomDepart());
		return departementService.addDepartement(departement);
	}

	// http://localhost:8089/Kaddem/departement/remove-departement/1
	@DeleteMapping("/remove-departement/{departement-id}")
	public void removeDepartement(@PathVariable("departement-id") Integer departementId) {
		departementService.deleteDepartement(departementId);
	}

	// http://localhost:8089/Kaddem/departement/update-departement
	@PutMapping("/update-departement")
	public Departement updateDepartement(@Valid @RequestBody DepartementDTO DepartementDTO) {
		Departement departement = new Departement();
		departement.setIdDepart(DepartementDTO.getIdDepart());
		departement.setNomDepart(DepartementDTO.getNomDepart());
		return departementService.updateDepartement(departement);
	}

}


