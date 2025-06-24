package Esprit.tn.EspritJobGetaway.Service;

import Esprit.tn.EspritJobGetaway.Entity.User;
import Esprit.tn.EspritJobGetaway.Enum.RoleType;
import Esprit.tn.EspritJobGetaway.Repository.UserRepository;
import Esprit.tn.EspritJobGetaway.dto.request.CompanyProfileRequest;
import Esprit.tn.EspritJobGetaway.dto.request.ProfileRequest;
import Esprit.tn.EspritJobGetaway.exception.NotFoundExecption;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email " + email));
    }

  /*  public Page<User>findUserswithPaginationAndSorting(int offset, int pageSize, String field, String roleType){
        Page<User> users = userRepository.findByRolesRoleType(RoleType.valueOf(roleType), PageRequest.of(offset, pageSize, Sort.by(field)));
        return users;
    }*/

    public User findUserById(Long idUser){
       Optional<User>  user= userRepository.findById(idUser);
       if (!user.isPresent()){
           throw new NotFoundExecption("no user found");
       }
       return user.get();
    }

    public User manageProfile(Long idUser, ProfileRequest profile){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no user found");
        }
        User user= findUser.get();
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setPhone(profile.getPhone());
        user.setProfession(profile.getProfession());
        return userRepository.save(user);
    }

    public User updateCompanyProfile(Long idUser, CompanyProfileRequest profileRequest) {
        Optional<User> findUser = userRepository.findById(idUser);
        if (!findUser.isPresent()) {
            throw new NotFoundExecption("No user found");
        }
        User user = findUser.get();
        user.setCompanyName(profileRequest.getCompanyName());
        user.setCompanyAddress(profileRequest.getCompanyAddress());
        user.setPhone(profileRequest.getPhone());
        user.setProfession(profileRequest.getProfession());
        return userRepository.save(user);
    }

    public User enabledUser(Long idUser, Boolean enable){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no user found");
        }
        User user= findUser.get();
        user.setEnabled(enable);
        return userRepository.save(user);
    }

    public User blockedUser(Long idUser, Boolean blocked){
        Optional<User>  findUser= userRepository.findById(idUser);
        if (!findUser.isPresent()){
            throw new NotFoundExecption("no user found");
        }

        User user= findUser.get();
        user.setNonLocked(blocked);
        return userRepository.save(user);
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public List<User> getAllUsers() {
        List<RoleType> roleTypes = Arrays.asList(RoleType.USER, RoleType.COMPANY, RoleType.STAFF);
        return userRepository.findByRolesRoleTypeIn(roleTypes);
    }

    public String uploadFile(MultipartFile file) {
    if (file.isEmpty()) {
        throw new IllegalArgumentException("Photo profile is required, should select a photo");
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
        if (Files.exists(filePath)) {
            // If file already exists, return the filename to save it in user's profile
            return filename;
        }

        Files.copy(file.getInputStream(), filePath);
        return filename;

    } catch (Exception e) {
        throw new RuntimeException("Failed to store file " + filename, e);
    }
}

    public User findUserByEmail(String  username){
        Optional<User>  user= userRepository.findByEmail(username);
        if (!user.isPresent()){
            throw new NotFoundException("no user found");
        }

        return user.get();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found with id " + id);
        }
        return user.get();
    }

}
