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
                case "NotFound":
                    return new BadLoginPasswordException("L'item demandé n'existe pas");
                case "LoginPassword":
                    return new BadLoginPasswordException("L'association login/mot de passe est invalide");
                case "UniqueFail":
                    return new CannotAddException("Existe déjà en base de donnée");
                case "AddFail":
                    return new CannotAddException("Echec dans l'ajout");
                default: return defaultErrorDecoder.decode(invoqueur, reponse);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
       return defaultErrorDecoder.decode(invoqueur, reponse);
    }
}
