package com.electronic.store.ElectronicStore_webapp.exceptions;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(){
        super("Resource Not found !!");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
