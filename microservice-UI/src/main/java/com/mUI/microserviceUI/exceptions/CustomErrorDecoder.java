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
                case "User03":
                    return new CannotAddException("");
                case "User01":
                    return new BadLoginPasswordException("");
                case "User02":
                    return new BadLoginPasswordException("");
                default: return defaultErrorDecoder.decode(invoqueur, reponse);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
       return defaultErrorDecoder.decode(invoqueur, reponse);
    }
}
