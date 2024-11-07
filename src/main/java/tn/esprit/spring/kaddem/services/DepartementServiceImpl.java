package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
public class DepartementServiceImpl implements IDepartementService {


    DepartementRepository departementRepository;
    EtudiantRepository etudiantRepository;

    // Injection par constructeur
    @Autowired
    public DepartementServiceImpl(DepartementRepository departementRepository, EtudiantRepository etudiantRepository) {
        this.departementRepository = departementRepository;
        this.etudiantRepository = etudiantRepository;
    }

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

    public Departement retrieveDepartement(Integer idDepart) {
        return departementRepository.findById(idDepart)
                .orElseThrow(() -> new NoSuchElementException("Département avec l'ID " + idDepart + " non trouvé"));
    }
    // Supprimer un département
    public void deleteDepartement(Integer idDepartement) {
        Departement d = retrieveDepartement(idDepartement);
        departementRepository.delete(d);
    }


    //les nouveux methodes ajouter par Rim
    public void affectDepartementToEtudiants(Integer departementId, List<Integer> etudiantIds) {
        if (departementId == 0) {
            throw new RuntimeException("Département non trouvé");
        }
       // Chercher le département dans la base de données en utilisant l id  du département
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département non trouvé"));

        // Créer un ensemble vide pour stocker les étudiants qui seront liés au département
        Set<Etudiant> etudiantsSet = new HashSet<>();
        // Parcourir la liste des IDs d'étudiants pour les récupérer un par un
        for (Integer etudiantId : etudiantIds) {
            // 4. Chercher chaque étudiant dans la base de données en utilisant son ID
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant non trouvé"));
            // assigner l'étudiant récupéré au département
            etudiant.setDepartement(departement);
            // Ajouter l'étudiant à l'ensemble d'étudiants
            etudiantsSet.add(etudiant);
        }

        // Affectation des étudiants au département
        departement.setEtudiants(etudiantsSet);

        // Sauvegarde du département
        departementRepository.save(departement);
    }

    public void removeEtudiantFromDepartement(Integer etudiantId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId).orElseThrow(() ->
                new RuntimeException("L'étudiant n'existe pas"));

        // Retirer l'étudiant du département
        etudiant.setDepartement(null);
        etudiantRepository.save(etudiant); // Enregistrer les modifications
    }




    public Integer countEtudiantsInDepartement(Integer departementId) {
        // Vérifier si l'ID du département est valide (non nul et supérieur à zéro)
        if (departementId == null || departementId <= 0) {
            throw new DepartementNotFoundException("Aucun département trouvé");
        }

        // Récupérer le département en fonction de l'ID fourni
        Departement departement = retrieveDepartement(departementId);

        // Vérifier si le département existe
        if (departement == null) {
            throw new DepartementNotFoundException("Département avec l'ID " + departementId + " non trouvé");
        }

        // Compter le nombre d'étudiants associés au département
        Integer nombreEtudiants = (departement.getEtudiants() != null) ?
                departement.getEtudiants().size() : 0; // Assurez-vous de gérer le cas où la liste est nulle

        // Log du résultat pour traçabilité
        log.info("Le département avec l'ID {} contient {} étudiant(s)", departementId, nombreEtudiants);

        // Retourner le nombre d'étudiants
        return nombreEtudiants;
    }


    // Vérifier si un étudiant est dans un département
    public boolean isEtudiantInDepartement(Integer etudiantId, Integer departementId) {
        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> new RuntimeException("L'étudiant n'existe pas"));

        return etudiant.getDepartement() != null && etudiant.getDepartement().getIdDepart().equals(departementId);
    }





}

