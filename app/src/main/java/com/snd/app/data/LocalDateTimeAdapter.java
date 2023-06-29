package com.snd.app.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public JsonElement serialize(LocalDateTime dateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        String formattedDateTime = dateTime.format(formatter);
        return new JsonPrimitive(formattedDateTime);
    }


    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String[] dateTimeArray = jsonElement.getAsJsonArray().toString().replace("[", "").replace("]", "").split(",");
        int[] dateTimeValues = new int[dateTimeArray.length];
        for (int i = 0; i < dateTimeArray.length; i++) {
            dateTimeValues[i] = Integer.parseInt(dateTimeArray[i].trim());
        }
        return LocalDateTime.of(dateTimeValues[0], dateTimeValues[1], dateTimeValues[2], dateTimeValues[3], dateTimeValues[4], dateTimeValues[5]);
    }


    public LocalDateTime test(int[] dateTimeArray) throws JsonParseException {
        int[] dateTimeValues = new int[dateTimeArray.length];
        for (int i = 0; i < dateTimeArray.length; i++) {
            dateTimeValues[i] = dateTimeArray[i];
        }
        return LocalDateTime.of(dateTimeValues[0], dateTimeValues[1], dateTimeValues[2], dateTimeValues[3], dateTimeValues[4], dateTimeValues[5]);
    }

}
