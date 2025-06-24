package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.Application;
import Esprit.tn.EspritJobGetaway.Entity.Offre;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Enum.StatusApplication;
import Esprit.tn.EspritJobGetaway.Repository.ApplicationRepository;
import Esprit.tn.EspritJobGetaway.Repository.OffreRepository;
import Esprit.tn.EspritJobGetaway.Repository.UserRepository;

import Esprit.tn.EspritJobGetaway.dto.request.ApplicationDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class ApplicationService implements IApplicationService {
    private OffreRepository offerRepository;
    private ApplicationRepository applicationRepository;
    private UserRepository userRepository;

    public Application retrieveApplication(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " id must not be null");
        }
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id :" + id));
        return applicationRepository.findById(id).get();
    }

    public List<Application> retrieveApplications() {
        return applicationRepository.findAll();
    }

    public List<Application> retrieveApplicationsByOfferId(Long id) {
        return applicationRepository.findAllByOfferId(id);
    }

    public void removeApplication(Long id, User user) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " id must not be null");
        }
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id :" + id));
        Long idCurrentUser = user.getId();
        if (!application.getUser().getId().equals(idCurrentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "you can not delete an application you didn't create");
        }
        applicationRepository.deleteById(id);
    }

    public Application addApplication(MultipartFile coverLetter, MultipartFile cv, Long idOffer, User currentUser) {
        Offre offer = offerRepository.findById(idOffer).orElse(null);
        Boolean exist = offer.getApplications().stream().map(application -> application.getUser().getId()).anyMatch(p -> p.equals(currentUser.getId()));
        if (exist) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "you can't apply : you already applied");
        }
        Application application = new Application();
        application.setOffer(offer);
        application.setStatusApplication(StatusApplication.PENDING);
        application.setUser(currentUser);
        if (coverLetter != null) {
            String nameFile = uploadFile(coverLetter);
            application.setCoverLetter(nameFile);
        }
        if (cv != null) {
            String nameFile = uploadFile(cv);
            application.setCv(nameFile);
        }
        return applicationRepository.save(application);
    }

    public Application updateApplication(MultipartFile coverLetter, MultipartFile cv, Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " id must not be null");
        }
        Application existingApplication = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "offer not found with id :" + id));
        if (coverLetter != null) {
            String fileName = StringUtils.cleanPath(coverLetter.getOriginalFilename());
            existingApplication.setCoverLetter(fileName);
        }
        if (cv != null) {
            uploadFile(cv);
            String fileName = StringUtils.cleanPath(cv.getOriginalFilename());
            existingApplication.setCv(fileName);
        }
        return applicationRepository.save(existingApplication);
    }

    public Application GestionStatus(Long id, ApplicationDto applicationDto) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, " id must not be null");
        }
        Application existingApplication = applicationRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id :" + id));
        existingApplication.setStatusApplication(applicationDto.getStatusApplication());
        return applicationRepository.save(existingApplication);
    }

    public String uploadFile(MultipartFile file) {
    if (file.isEmpty()) {
        throw new IllegalArgumentException("File is empty");
    }

    String filename = StringUtils.cleanPath(file.getOriginalFilename());
    try {
        if (filename.contains("..")) {
            throw new IllegalArgumentException("Cannot upload file with relative path outside current directory");
        }

        Path uploadDir = Paths.get("src/main/resources/uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(filename);

        // If the file already exists, delete it
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        Files.copy(file.getInputStream(), filePath);

        System.out.println("File uploaded successfully: " + filePath);
        return filename;
    } catch (Exception e) {
        throw new RuntimeException("Failed to store file " + filename, e);
    }
    }

    public void setStatusAccepted(Long id) {
    Application application = applicationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + id));
    if (application.getStatusApplication() == StatusApplication.ACCEPTED) {
        application.setStatusApplication(StatusApplication.PENDING);
    } else {
        application.setStatusApplication(StatusApplication.ACCEPTED);
    }
    applicationRepository.save(application);
}

    public void setStatusRefused(Long id) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found with id: " + id));
        if (application.getStatusApplication() == StatusApplication.REFUSED) {
            application.setStatusApplication(StatusApplication.PENDING);
        } else {
            application.setStatusApplication(StatusApplication.REFUSED);
        }
        applicationRepository.save(application);
    }

    public Resource openFile(String filename) {
        try {
            Path filePath = Paths.get("src/main/resources/uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found or not readable: " + filename);
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file: " + filename, e);
        }
    }

    public List<Application> getApplicationsByUserId(Long userId) {
        return applicationRepository.findByUserId(userId);
    }



}