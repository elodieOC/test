package com.mmerchants.microservicemerchants.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MerchantListSerializer extends StdSerializer<List<Merchant>> {

    public MerchantListSerializer() {
        this(null);
    }

    public MerchantListSerializer(Class<List<Merchant>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Merchant> merchants,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Integer> ids = new ArrayList<>();
        for (Merchant merchant : merchants) {
            ids.add(merchant.getId());
        }
        generator.writeObject(ids);
    }
}
