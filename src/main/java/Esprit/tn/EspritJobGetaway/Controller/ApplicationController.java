package Esprit.tn.EspritJobGetaway.Controller;

import Esprit.tn.EspritJobGetaway.Entity.Application;
import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Service.ApplicationService;
import Esprit.tn.EspritJobGetaway.Service.UserService;
import Esprit.tn.EspritJobGetaway.dto.request.ApplicationDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/applications")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final UserService userService;
    private final Path fileStorageLocation;


    @Autowired
    public ApplicationController(ApplicationService applicationService, UserService userService) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.fileStorageLocation = Paths.get("src/main/resources/uploads").toAbsolutePath().normalize();

    }

    @PostMapping("/addApplication")
    public ResponseEntity<Map<String, String>> addApplication(@RequestParam("coverLetter") MultipartFile coverLetter, @RequestParam("cv") MultipartFile cv, @RequestParam Long idOffer, @RequestParam Long userId) {
    User user = userService.findUserById(userId);
    Map<String, String> response = new HashMap<>();
    try {
        applicationService.addApplication(coverLetter, cv,idOffer, user);
        response.put("message", "Application added successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
        response.put("error", "An error occurred while adding the application" +e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    }

    @PostMapping("/updateApplication/{id}")
    public ResponseEntity<Object> updateApplication(@RequestParam MultipartFile coverLetter, @RequestParam MultipartFile cv, @PathVariable Long id) {
        try {
            applicationService.updateApplication(coverLetter, cv, id);
            return ResponseEntity.ok().body("{\"message\": \"Updated Successfully\"}");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/GestionStatus/{id}") 
    public ResponseEntity<?> GestionStatus(@RequestBody ApplicationDto applicationDto, @PathVariable Long id) {
        try {
            applicationService.GestionStatus(id, applicationDto);
            return new ResponseEntity<>("Status updated Successfully", HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getApplication/{id}")
    public ResponseEntity<?> retrieveApplication(@PathVariable Long id) {
        try {
            Application application = applicationService.retrieveApplication(id);
            return ResponseEntity.ok(application);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getApplicatiosByOfferId/{id}")
    public ResponseEntity<?>retrieveApplicationsByOfferId(@PathVariable Long id) {
        try {
            List<Application> applications =   applicationService.retrieveApplicationsByOfferId(id);
        return ResponseEntity.ok(applications);
    } catch (NotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
}
    @GetMapping("/getApplications")
    public List<Application> retrieveApplications() {
        return applicationService.retrieveApplications();
    }

    @DeleteMapping("/deleteApplication")
    public ResponseEntity<Map<String, String>> removePostulation(@RequestParam Long id, Principal principal) {
        User currentUser = userService.findUserByEmail(principal.getName());
        Map<String, String> response = new HashMap<>();
        try {
            applicationService.removeApplication(id, currentUser);
            response.put("message", "Dropped Successfully");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String message;
        try {
            applicationService.uploadFile(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body("Add successfully: " + message);
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @PostMapping("/setStatusAccepted/{id}")
    public ResponseEntity<Map<String, String>> setStatusAccepted(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            applicationService.setStatusAccepted(id);
            response.put("message", "Status set to ACCEPTED or PENDING");
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            response.put("error", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @PostMapping("/setStatusRefused/{id}")
    public ResponseEntity<Map<String, String>> setStatusRefused(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            applicationService.setStatusRefused(id);
            response.put("message", "Status set to REFUSED or PENDING");
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            response.put("error", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

   @GetMapping("/openFile/{filename}")
    public ResponseEntity<Resource> openFile(@PathVariable String filename) {
    try {
        Path filePath = this.fileStorageLocation.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            throw new Exception("File not found " + filename);
        }
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ByteArrayResource(("Error occurred: " + ex.getMessage()).getBytes()));
    }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Application>> getApplicationsByUserId(@PathVariable Long userId) {
    try {
        List<Application> applications = applicationService.getApplicationsByUserId(userId);
        return ResponseEntity.ok(applications);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
}