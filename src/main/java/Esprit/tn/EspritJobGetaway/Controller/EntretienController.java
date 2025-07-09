package Esprit.tn.EspritJobGetaway.Controller;

import Esprit.tn.EspritJobGetaway.Entity.Entretien;
import Esprit.tn.EspritJobGetaway.Entity.Offre;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Enum.TypeEntretien;
import Esprit.tn.EspritJobGetaway.Service.EntretienService;
import Esprit.tn.EspritJobGetaway.Service.OffreService;
import Esprit.tn.EspritJobGetaway.dto.EntretienEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/entretiens")
@RequiredArgsConstructor
public class EntretienController {


    private final EntretienService entretienService;

    @PostMapping("/planifier")
    public Entretien planifier(@RequestParam Long applicationId,
                               @RequestParam TypeEntretien type,
                               @RequestParam Date date,
                               @RequestParam String lieu) {
        return entretienService.planifierEntretien(applicationId, type, date, lieu);
    }

    @PostMapping("/resultat")
    public void traiter(@RequestParam Long entretienId, @RequestParam boolean accepte) {
        entretienService.traiterResultat(entretienId, accepte);
    }

    @GetMapping("/parApplication")
    public List<Entretien> lister(@RequestParam Long applicationId) {
        return entretienService.listerEntretiens(applicationId);
    }

    @GetMapping("/events")
    public List<EntretienEventDTO> getEventsByApplication(@RequestParam Long applicationId) {
        return entretienService.getEntretienEventsByApplication(applicationId);
    }
}