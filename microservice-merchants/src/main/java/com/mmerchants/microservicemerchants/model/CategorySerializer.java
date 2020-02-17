package com.mmerchants.microservicemerchants.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;

public class CategorySerializer extends StdSerializer<Category> {
    public CategorySerializer() {
        this(null);
    }

    public CategorySerializer(Class<Category> t) {
        super(t);
    }

    @Override
    public void serialize(
            Category value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("categoryName", value.getCategoryName());
        //Iterate List
        jgen.writeArrayFieldStart("merchants");
        for(Merchant merchant: value.getMerchants()) {
            jgen.writeStartObject();
            jgen.writeNumberField("id", merchant.getId());
            jgen.writeStringField("merchantName", merchant.getMerchantName());
            jgen.writeStringField("longitude", merchant.getLongitude());
            jgen.writeStringField("latitude", merchant.getLatitude());
            jgen.writeEndObject();
        }
        jgen.writeEndArray();
        jgen.writeEndObject();
    }

}
