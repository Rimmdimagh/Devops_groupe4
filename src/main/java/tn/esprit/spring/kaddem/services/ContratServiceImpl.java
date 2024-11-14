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


	@Override
	public void retrieveAndUpdateStatusContrat() {
		List<Contrat> contrats = contratRepository.findAll();
		List<Contrat> contrats15j = new ArrayList<>(); // Initialize list for logging
		List<Contrat> contratsAarchiver = new ArrayList<>(); // Initialize list for archiving

		for (Contrat contrat : contrats) {
			Date dateSysteme = new Date();

			if (!contrat.getArchive()) { // Only process non-archived contracts
				long differenceInTime = contrat.getDateFinContrat().getTime() - dateSysteme.getTime();
				long differenceInDays = differenceInTime / (1000 * 60 * 60 * 24); // Convert milliseconds to days

				if (differenceInDays == 15) {
					contrats15j.add(contrat);
					log.info("Contrat approaching expiry in 15 days: " + contrat);
				}

				if (differenceInDays <= 0) { // Contract expired, archive it
					contratsAarchiver.add(contrat);
					contrat.setArchive(true);
					contratRepository.save(contrat); // Save updated contract
					log.info("Archived contrat: " + contrat);
				}
			}
		}
	}

	@Override
	public float getChiffreAffaireEntreDeuxDates(Date startDate, Date endDate) {
		// Validate input dates
		if (startDate == null || endDate == null || startDate.after(endDate)) {
			throw new IllegalArgumentException("Invalid date range");
		}

		// Calculate the number of months between startDate and endDate
		long differenceInMillis = endDate.getTime() - startDate.getTime();
		float differenceInDays = (float) differenceInMillis / (1000 * 60 * 60 * 24);
		float differenceInMonths = differenceInDays / 30;

		List<Contrat> contrats = contratRepository.findAll();
		float totalRevenue = 0;

		for (Contrat contrat : contrats) {
			if (!contrat.getArchive()) { // Only consider active contracts
				float monthlyRate = 0;

				// Determine the monthly rate based on the contract's specialite
				switch (contrat.getSpecialite()) {
					case IA:
						monthlyRate = 300;
						break;
					case CLOUD:
						monthlyRate = 400;
						break;
					case RESEAUX:
						monthlyRate = 350;
						break;
					case SECURITE:
						monthlyRate = 450;
						break;
					default:
						monthlyRate = 0;
				}

				totalRevenue += monthlyRate * differenceInMonths;
			}
		}

		return totalRevenue;
	}



}