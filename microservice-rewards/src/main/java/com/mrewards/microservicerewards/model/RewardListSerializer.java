package com.mrewards.microservicerewards.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RewardListSerializer extends StdSerializer<List<Reward>> {

    public RewardListSerializer() {
        this(null);
    }

    public RewardListSerializer(Class<List<Reward>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Reward> rewards,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<Integer> ids = new ArrayList<>();
        for (Reward reward : rewards) {
            ids.add(reward.getId());
        }
        generator.writeObject(ids);
    }
}
