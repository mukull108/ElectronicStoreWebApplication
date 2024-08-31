package com.electronic.store.ElectronicStore_webapp.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImgNameValid {
    String message() default "Invalid image format"; //error message

    Class<?>[] groups() default { }; //represent group of constraints

    Class<? extends Payload>[] payload() default { }; //addition info about annotation

}
