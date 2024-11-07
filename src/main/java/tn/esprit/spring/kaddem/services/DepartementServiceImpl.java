package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class DepartementServiceImpl implements IDepartementService {

    // Constant for error messages
    private static final String DEPARTEMENT_NOT_FOUND_MSG = "Département non trouvé";

    @Autowired
    DepartementRepository departementRepository;

    @Autowired
    EtudiantRepository etudiantRepository;

    // Récupérer tous les départements
    public List<Departement> retrieveAllDepartements() {
        return (List<Departement>) departementRepository.findAll();
    }

    // Ajouter un département
    public Departement addDepartement(Departement d) {
        return departementRepository.save(d);
    }

    // Mettre à jour un département
    public Departement updateDepartement(Departement d) {
        return departementRepository.save(d);
    }

    // Récupérer un département par son ID
    public Departement retrieveDepartement(Integer idDepart) {
        return departementRepository.findById(idDepart).orElseThrow(() ->
                new DepartementNotFoundException(DEPARTEMENT_NOT_FOUND_MSG));
    }

    // Supprimer un département
    public void deleteDepartement(Integer idDepartement) {
        Departement d = retrieveDepartement(idDepartement);
        departementRepository.delete(d);
    }

    // Ajouter un département aux étudiants
    public void affectDepartementToEtudiants(Integer departementId, List<Integer> etudiantIds) {
        if (departementId == 0) {
            throw new DepartementNotFoundException(DEPARTEMENT_NOT_FOUND_MSG);
        }

        // Chercher le département
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new DepartementNotFoundException(DEPARTEMENT_NOT_FOUND_MSG));

        Set<Etudiant> etudiantsSet = new HashSet<>();
        for (Integer etudiantId : etudiantIds) {
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new DepartementNotFoundException("Étudiant non trouvé"));

            etudiant.setDepartement(departement);
            etudiantsSet.add(etudiant);
        }

        departement.setEtudiants(etudiantsSet);
        departementRepository.save(departement);
    }

    // Supprimer un étudiant d'un département
    public void removeEtudiantFromDepartement(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new DepartementNotFoundException("L'étudiant n'existe pas"));

        etudiant.setDepartement(null);
        etudiantRepository.save(etudiant);
    }

    // Compter les étudiants dans un département
    public Integer countEtudiantsInDepartement(Integer departementId) {
        if (departementId == null || departementId <= 0) {
            throw new DepartementNotFoundException("Aucun département trouvé");
        }

        Departement departement = retrieveDepartement(departementId);

        Integer nombreEtudiants = (departement.getEtudiants() != null) ?
                departement.getEtudiants().size() : 0;

        log.info("Le département avec l'ID {} contient {} étudiant(s)", departementId, nombreEtudiants);

        return nombreEtudiants;
    }

    // Vérifier si un étudiant est dans un département
    public boolean isEtudiantInDepartement(Integer etudiantId, Integer departementId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new DepartementNotFoundException("L'étudiant n'existe pas"));

        return etudiant.getDepartement() != null && etudiant.getDepartement().getIdDepart().equals(departementId);
    }

}
