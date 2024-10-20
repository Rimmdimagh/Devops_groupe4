package tn.esprit.spring.kaddem.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Option; // Make sure to import the Option enum

import java.util.List;

@Repository
public interface EtudiantRepository extends CrudRepository<Etudiant, Integer> {

    List<Etudiant> findEtudiantsByDepartement_IdDepart(Integer idDepart);

    @Query("SELECT e FROM Etudiant e WHERE e.nomE = :nomE AND e.prenomE = :prenomE")
    Etudiant findByNomEAndPrenomE(@Param("nomE") String nomE, @Param("prenomE") String prenomE);

    // New method to find students by last name
    @Query("SELECT e FROM Etudiant e WHERE e.nomE = :nomE")
    List<Etudiant> findByNomE(@Param("nomE") String nomE);

    // New method to find students by option (using the correct type)
    @Query("SELECT e FROM Etudiant e WHERE e.op = :option")
    List<Etudiant> findByOp(@Param("option") Option option); // Updated to use Option enum

    // Method to count total students
    @Override
    long count();

    @Query("SELECT COUNT(e) FROM Etudiant e WHERE e.departement.idDepart = :departementId")
    long countEtudiantsByDepartement_IdDepart(Integer departementId);
}
