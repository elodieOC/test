package com.mmerchants.microservicemerchants.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RoleSerializer extends StdSerializer<Role> {
    public RoleSerializer() {
        this(null);
    }

    public RoleSerializer(Class<Role> t) {
        super(t);
    }

    @Override
    public void serialize(
            Role value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("roleName", value.getRoleName());
        jgen.writeEndObject();
    }


}
