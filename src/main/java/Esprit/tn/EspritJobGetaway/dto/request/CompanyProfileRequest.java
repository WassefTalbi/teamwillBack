package Esprit.tn.EspritJobGetaway.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileRequest {
    @NotBlank(message = "Company name is required and cannot be blank.")
    @Size(min=3, max=50, message = "Company name length must be between 3 and 50 characters.")
    private String companyName;

    @NotBlank(message = "Company address is required and cannot be blank.")
    @Size(min=10, max=100, message = "Company address length must be between 10 and 100 characters.")
    private String companyAddress;

    @NotBlank(message = "Company phone number is required and cannot be blank.")
    @Size(min=10, max=15, message = "Company phone number length must be between 10 and 15 characters.")
    private String phone;

    @NotBlank(message = "profession  is required and cannot be blank.")
    @Size(min=3, message = "first name length min is 3 ")
    private String profession;
}