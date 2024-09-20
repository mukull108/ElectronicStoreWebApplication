package com.electronic.store.ElectronicStore_webapp.dtos;

import com.electronic.store.ElectronicStore_webapp.validate.ImgNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userId;

    @Size(min = 3,max = 20, message = "This is invalid name !!")
    private String name;

    @Email(message = "invalid user email !!")
    @NotBlank(message = "email is required")
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",message = "Write a correct email")
    private String email;

    @NotBlank(message = "password is required !!")
    private String password;

    @Size(min = 4,message = "Enter valid gender !!")
    private String gender;

    @NotBlank(message = "Write something about yourself !!")
    private String about;

    @ImgNameValid
    private String imageName;

    private Set<RoleDto> roles;
}
