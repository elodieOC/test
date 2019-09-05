package com.musers.microserviceusers.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserListSerializer extends StdSerializer<List<User>> {

    public UserListSerializer() {
        this(null);
    }

    public UserListSerializer(Class<List<User>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<User> users,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Integer> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        generator.writeObject(ids);
    }
}
