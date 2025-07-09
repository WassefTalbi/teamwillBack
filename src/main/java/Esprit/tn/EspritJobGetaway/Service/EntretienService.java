package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.Application;
import Esprit.tn.EspritJobGetaway.Entity.Entretien;

import Esprit.tn.EspritJobGetaway.Enum.StatusApplication;
import Esprit.tn.EspritJobGetaway.Enum.StatusEntretien;
import Esprit.tn.EspritJobGetaway.Enum.TypeEntretien;
import Esprit.tn.EspritJobGetaway.Repository.ApplicationRepository;
import Esprit.tn.EspritJobGetaway.Repository.EntretienRepository;


import Esprit.tn.EspritJobGetaway.dto.EntretienEventDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class EntretienService {

    private final EntretienRepository entretienRepository;
    private final ApplicationRepository applicationRepository;
    private final EmailService emailService;

    public Entretien planifierEntretien(Long applicationId, TypeEntretien type, Date date, String lieu) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application non trouvée"));

        if (app.getStatusApplication() == StatusApplication.REFUSED) {
            throw new IllegalStateException("Candidature déjà refusée");
        }

        if (entretienRepository.existsByApplicationAndTypeEntretien(app, type)) {
            throw new IllegalStateException("Entretien déjà planifié de ce type");
        }

        Entretien e = new Entretien(null, type, date, lieu, StatusEntretien.PLANIFIE, app);
        return entretienRepository.save(e);
    }

    public void traiterResultat(Long entretienId, boolean accepte) {
        Entretien entretien = entretienRepository.findById(entretienId)
                .orElseThrow(() -> new RuntimeException("Entretien non trouvé"));

        Application app = entretien.getApplication();

        if (accepte) {
            entretien.setStatusEntretien(StatusEntretien.ACCEPTE);
            entretienRepository.save(entretien);

            emailService.envoyerMail(app.getUser().getEmail(), "Succès entretien", "Vous avez réussi l'entretien : " + entretien.getTypeEntretien());
        } else {
            entretien.setStatusEntretien(StatusEntretien.REFUSE);
            entretienRepository.save(entretien);

            app.setStatusApplication(StatusApplication.REFUSED);
            applicationRepository.save(app);

            emailService.envoyerMail(app.getUser().getEmail(), "Candidature refusée", "Votre candidature est arrêtée.");
        }
    }

    public List<Entretien> listerEntretiens(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application non trouvée"));
        return entretienRepository.findByApplication(app);
    }

    public List<EntretienEventDTO> getEntretienEventsByApplication(Long applicationId) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        List<Entretien> entretiens = entretienRepository.findByApplication(app);
        return entretiens.stream()
                .map(e -> new EntretienEventDTO(
                        e.getId(),
                        "Entretien - " + e.getTypeEntretien().name(),
                        e.getDateEntretien()
                ))
                .toList();
    }
}