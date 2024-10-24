package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.entities.Departement;

import java.util.List;

public interface IDepartementService {
    public List<Departement> retrieveAllDepartements();

    public Departement addDepartement (Departement d);

    public   Departement updateDepartement (Departement d);

    public  Departement retrieveDepartement (Integer idDepart);

    public  void deleteDepartement(Integer idDepartement);
    public void removeEtudiantFromDepartement(Integer etudiantId);
    public void affectDepartementToEtudiants(Integer departementId, List<Integer> etudiantIds);
    public Integer countEtudiantsInDepartement(Integer departementId);
    public boolean isEtudiantInDepartement(Integer etudiantId, Integer departementId);
}
