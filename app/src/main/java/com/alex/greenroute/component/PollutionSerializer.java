package com.alex.greenroute.component;

import com.alex.greenroute.data.remote.response.PollutionResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import java.util.HashMap;

/**
 * Created by alex on 26/11/2017.
 */

public class PollutionSerializer implements JsonDeserializer<PollutionResponse> {
    @Override
    public PollutionResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PollutionResponse response = new PollutionResponse();
        JsonObject object = json.getAsJsonObject();

        double NO2 = object.get("NO2").getAsDouble();
        double CO2 = object.get("CO2").getAsDouble();

        response.polluants = new HashMap<>();

        response.polluants.put("NO2", NO2);
        response.polluants.put("CO2", CO2);

        return response;
    }
}
