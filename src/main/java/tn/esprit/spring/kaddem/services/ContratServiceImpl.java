package tn.esprit.spring.kaddem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ContratServiceImpl implements IContratService{
	@Autowired
	ContratRepository contratRepository;
	@Autowired
	EtudiantRepository etudiantRepository;
	public List<Contrat> retrieveAllContrats(){
		return (List<Contrat>) contratRepository.findAll();
	}

	public Contrat updateContrat (Contrat  ce){
		return contratRepository.save(ce);
	}

	public  Contrat addContrat (Contrat ce){
		return contratRepository.save(ce);
	}

	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}

	/*public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		List<Etudiant> etudiants = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);

		if (etudiants.isEmpty()) {
			throw new RuntimeException("No student found with the given name.");
		} else if (etudiants.size() > 1) {
			throw new RuntimeException("Multiple students found with the same name. Please refine the search.");
		}

		Etudiant e = etudiants.get(0);  // Assuming you take the first result
		Contrat ce = contratRepository.findByIdContrat(idContrat);

		// Rest of the code remains the same
		ce.setEtudiant(e);
		contratRepository.save(ce);
		return ce;
	}*/
	/*public Contrat affectContratToEtudiant(Integer idContrat, String nomE, String prenomE) {
		List<Etudiant> etudiants = etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		if (etudiants.size() > 1) {
			throw new RuntimeException("Multiple students found with the same name. Please refine the search.");
		}
		Etudiant e = etudiants.get(0); // Get the first (and ideally, only) result
		Contrat ce = contratRepository.findByIdContrat(idContrat);
		Set<Contrat> contrats = e.getContrats();
		Integer nbContratsActifs = 0;
		for (Contrat contrat : contrats) {
			if (contrat.getArchive() != null && !contrat.getArchive()) {
				nbContratsActifs++;
			}
		}
		if (nbContratsActifs <= 4) {
			ce.setEtudiant(e);
			contratRepository.save(ce);
		}
		return ce;
	}*/




	public Contrat affectContratToEtudiant (Integer idContrat, String nomE, String prenomE){
		Etudiant e=etudiantRepository.findByNomEAndPrenomE(nomE, prenomE);
		Contrat ce=contratRepository.findByIdContrat(idContrat);
		Set<Contrat> contrats= e.getContrats();
		Integer nbContratssActifs=0;
		if (contrats.size()!=0) {
			for (Contrat contrat : contrats) {
				if (((contrat.getArchive())!=null)&& ((contrat.getArchive())!=false))  {
					nbContratssActifs++;
				}
			}
		}
		if (nbContratssActifs<=4){
			ce.setEtudiant(e);
			contratRepository.save(ce);}
		return ce;
	}
	public 	Integer nbContratsValides(Date startDate, Date endDate){
		return contratRepository.getnbContratsValides(startDate, endDate);
	}
	public void retrieveAndUpdateStatusContrat() {
		// Retrieve all contracts
		List<Contrat> contrats = contratRepository.findAll();

		// Initialize the lists as empty lists
		List<Contrat> contrats15j = new ArrayList<>();
		List<Contrat> contratsAarchiver = new ArrayList<>();

		// Loop through all contracts and check their status
		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();
			if (!contrat.getArchive()) {  // If the contract is not archived
				long difference_In_Time = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
				long difference_In_Days = difference_In_Time / (1000 * 60 * 60 * 24);  // Convert milliseconds to days

				// Check if it's 15 days before the end date
				if (difference_In_Days == 15) {
					contrats15j.add(contrat);
					log.info("Contrat to notify (15 days left): " + contrat);
				}

				// Check if the contract has expired (0 days remaining)
				if (difference_In_Days == 0) {
					contratsAarchiver.add(contrat);
					contrat.setArchive(true);  // Archive the contract
					contratRepository.save(contrat);  // Save the updated contract
				}
			}
		}

		// You can now do something with contrats15j and contratsAarchiver if needed, e.g., send notifications
	}
	/*public void retrieveAndUpdateStatusContrat(){
		List<Contrat>contrats=contratRepository.findAll();
		List<Contrat>contrats15j=null;
		List<Contrat>contratsAarchiver=null;
		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();
			if (contrat.getArchive()==false) {
				long difference_In_Time = dateSysteme.getTime() - contrat.getDateFinContrat().getTime();
				long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
				if (difference_In_Days==15){
					contrats15j.add(contrat);
					log.info(" Contrat : " + contrat);
				}
				if (difference_In_Days==0) {
					contratsAarchiver.add(contrat);
					contrat.setArchive(true);
					contratRepository.save(contrat);
				}
			}
		}
	}*/
	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate){
		float difference_In_Time = endDate.getTime() - startDate.getTime();
		float difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
		float difference_In_months =difference_In_Days/30;
		List<Contrat> contrats=contratRepository.findAll();
		float chiffreAffaireEntreDeuxDates=0;
		for (Contrat contrat : contrats) {
			if (contrat.getSpecialite()== Specialite.IA){
				chiffreAffaireEntreDeuxDates+=(difference_In_months*300);
			} else if (contrat.getSpecialite()== Specialite.CLOUD) {
				chiffreAffaireEntreDeuxDates+=(difference_In_months*400);
			}
			else if (contrat.getSpecialite()== Specialite.RESEAUX) {
				chiffreAffaireEntreDeuxDates+=(difference_In_months*350);
			}
			else //if (contrat.getSpecialite()== Specialite.SECURITE)
			{
				chiffreAffaireEntreDeuxDates+=(difference_In_months*450);
			}
		}
		return chiffreAffaireEntreDeuxDates;


	}


}
