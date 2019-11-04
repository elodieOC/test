package com.mUI.microserviceUI.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;


public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response reponse) {

        ObjectMapper mapper = new ObjectMapper();
        try{
            ApiError error = mapper.readValue(reponse.body().asInputStream(), ApiError.class);
            switch (error.getMessage()){
                case "Merchant01":
                    return new BadLoginPasswordException("L'utilisateur n'existe pas");
                case "Merchant02":
                    return new BadLoginPasswordException("L'association login/mot de passe est invalide");
                case "Merchant03":
                    return new CannotAddException("Cet email est déjà lié à un compte");
                case "Merchant04":
                    return new CannotAddException("Ce nom est déjà lié à un compte");
                case "Merchant05":
                    return new CannotAddException("Echec dans l'ajout");
                default: return defaultErrorDecoder.decode(invoqueur, reponse);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
       return defaultErrorDecoder.decode(invoqueur, reponse);
    }
}
