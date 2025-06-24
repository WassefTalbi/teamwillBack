package Esprit.tn.EspritJobGetaway.Controller;

import Esprit.tn.EspritJobGetaway.Entity.Offre;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Service.OffreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/offres")
public class OffreController {

    @Autowired
    private OffreService offreService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createOffre(@RequestBody Offre offre) {
        Offre createdOffre = offreService.createOffre(offre);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Offre created successfully");
        response.put("OffreId", String.valueOf(createdOffre.getId()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Offre>> getAllOffres() {
        List<Offre> offres = offreService.getAllOffres();
        return ResponseEntity.ok(offres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Offre> getOffreById(@PathVariable Long id) {
        Optional<Offre> offre = offreService.getOffreById(id);
        return offre.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateOffre(@PathVariable Long id, @RequestBody Offre updatedOffre) {
        try {
            Offre updated = offreService.updateOffre(id, updatedOffre);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Offre updated successfully");
            response.put("OffreId", String.valueOf(updated.getId()));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteOffre(@PathVariable Long id) {
        try {
            offreService.deleteOffre(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Offre deleted successfully");
            response.put("OffreId", String.valueOf(id));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/image/{offreId}")
    public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable Long offreId) {
        Optional<Offre> offreOptional = offreService.getOffreById(offreId);
        if (offreOptional.isPresent()) {
            Offre offre = offreOptional.get();
            String imageName = offre.getUser().getPhotoProfile(); // Assuming getPhotoProfile() returns the image file name
            Path imagePath = Paths.get("src/main/resources/uploads").resolve(imageName);
            try {
                org.springframework.core.io.Resource resource = new UrlResource(imagePath.toUri());
                if (resource.exists() || resource.isReadable()) {
                    return ResponseEntity.ok().body(resource);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/offre")
    public ResponseEntity<List<Offre>> getOffresByCurrentUser() {
        List<Offre> offres = offreService.getOffresByCurrentUser();
        if (offres.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(offres);
        }
    }

    @GetMapping("/noffre")
    public ResponseEntity<List<Offre>> getOffresNotByCurrentUser() {
    List<Offre> offres = offreService.getOffresNotByCurrentUser();
    if (offres.isEmpty()) {
        return ResponseEntity.notFound().build();
    } else {
        return ResponseEntity.ok(offres);
    }
    }


    @GetMapping("/{offreId}/user")
    public ResponseEntity<User> getUserByOffreId(@PathVariable Long offreId) {
        User user = offreService.getUserByOffreId(offreId);
        return ResponseEntity.ok(user);
    }
}