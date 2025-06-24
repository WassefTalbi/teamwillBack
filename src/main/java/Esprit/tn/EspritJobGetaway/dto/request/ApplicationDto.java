package Esprit.tn.EspritJobGetaway.dto.request;

import Esprit.tn.EspritJobGetaway.Enum.StatusApplication;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class ApplicationDto {

        private MultipartFile coverLetterFile;
        private MultipartFile cvFile;
        private StatusApplication statusApplication  ;
}
