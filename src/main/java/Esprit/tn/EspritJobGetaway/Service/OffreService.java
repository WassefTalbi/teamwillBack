package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.Offre;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Repository.OffreRepository;
import Esprit.tn.EspritJobGetaway.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OffreService {
    private final OffreRepository offreRepository;
    private final UserRepository userRepository;


    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("email of current user "+authentication.getName());
        return authentication.getName();

    }

    private Long getCurrentUserId() {
    String currentUserEmail = getCurrentUserEmail();
    User user = userRepository.findByEmail(currentUserEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return user.getId();
}

    // Create
    public Offre createOffre(Offre offre) {

        String currentUserEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("User not found"));

        Offre newOffre = new Offre();
        newOffre.setTitre(offre.getTitre());


        newOffre.setDescription(offre.getDescription());
        newOffre.setStartDate(offre.getStartDate());
        newOffre.setEndDate(offre.getEndDate());
        newOffre.setDuration((int) ChronoUnit.DAYS.between(offre.getStartDate().toInstant(), offre.getEndDate().toInstant()));
        newOffre.setOfferType(offre.getOfferType());
        newOffre.setActive(true);
        newOffre.setUser(user);

        return offreRepository.save(newOffre);
    }


    // Read all
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    // Read by ID
    public Optional<Offre> getOffreById(Long id) {
        return offreRepository.findById(id);
    }

    // Update
    public Offre updateOffre(Long id, Offre updatedOffre) {

        String currentUserEmail = getCurrentUserEmail();
        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("User not found"));

        return offreRepository.findById(id)
                .map(offre -> {
                    offre.setTitre(updatedOffre.getTitre());
                    offre.setDescription(updatedOffre.getDescription());
                    offre.setStartDate(updatedOffre.getStartDate());
                    offre.setEndDate(updatedOffre.getEndDate());
                    offre.setDuration((int) ChronoUnit.DAYS.between(updatedOffre.getStartDate().toInstant(), updatedOffre.getEndDate().toInstant()));
                    offre.setOfferType(updatedOffre.getOfferType());
                    offre.setActive(true);
                    offre.setUser(user);
                    return offreRepository.save(offre);
                })
                .orElseThrow(() -> new IllegalArgumentException("Offre not found with id: " + id));
    }

    // Delete
    public void deleteOffre(Long id) {
        if (offreRepository.existsById(id)) {
            offreRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Offre not found with id: " + id);
        }
    }

    public List<Offre> getOffresByCurrentUser() {
    Long currentUserId = getCurrentUserId();
    return offreRepository.findByUserId(currentUserId);
    }

    public List<Offre> getOffresNotByCurrentUser() {
    Long currentUserId = getCurrentUserId();
    return offreRepository.findByUserIdNot(currentUserId);
    }

    public User getUserByOffreId(Long offreId) {
        Offre offre = offreRepository.findById(offreId).orElseThrow(() -> new IllegalArgumentException("Invalid offre ID"));
        return userRepository.findById(offre.getUser().getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}