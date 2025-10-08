package fr.hb.mlang.electricitybusiness.security.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import fr.hb.mlang.electricitybusiness.security.auth.validator.MinAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import org.hibernate.validator.constraints.URL;

//@JsonInclude(JsonInclude.Include.NON_NULL) // If issue with null values (phoneNumber & avatar), add this annotation to ignore null
public record RegisterRequest(
    @NotBlank
    @Email
    @Size(max = 255)
    String email,

    @Size(max = 15)
    @Pattern(regexp = "^\\+?[0-9 .()-]{7,15}$")
    String phoneNumber,

    @NotBlank
    @Size(min = 6, max = 64)
    @JsonProperty(access = Access.WRITE_ONLY) // Set as write-only so it doesn't get serialized back in the response
    String password,

    @NotBlank
    @Size(min = 2, max = 50)
    String firstName,

    @NotBlank
    @Size(min = 2, max = 50)
    String lastName,

    @NotNull
    @Past
    @MinAge(18)
    LocalDate dateOfBirth,

    @NotBlank
    @Size(max = 512)
    String homeAddress,

    @URL
    String avatar
) {

}
