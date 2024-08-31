package com.electronic.store.ElectronicStore_webapp.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<ImgNameValid,String> {
    Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //logic to validate
        logger.info("Message from isValid : {}",value);
        if(value.isBlank()){
            return false;
        }

        return true;
    }
}
