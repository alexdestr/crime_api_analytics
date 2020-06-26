package ru.vegd.dataReceiver.utils;

import com.google.gson.JsonObject;
import ru.vegd.entity.StreetLevelCrimes;

import java.time.YearMonth;

public class JsonToEntityConverter {

    StreetLevelCrimes crimes = new StreetLevelCrimes();

    public StreetLevelCrimes convert(JsonObject json) {
        if (json.get("categpry").isJsonNull() == false) {
            crimes.setCategory(json.get("category").toString().replaceAll("\"", ""));
        } else {
            crimes.setCategory(null);
        }
        if (json.get("location_type").isJsonNull() == false) {
            crimes.setLocatonType(json.get("location_type").toString().replaceAll("\"", ""));
        } else {
            crimes.setLocatonType(null);
        }
        if (json.get("location").isJsonNull() == false) {
            if (json.get("location").getAsJsonObject().get("latitude").isJsonNull() == false && json.get("location").getAsJsonObject().get("longitude").isJsonNull() == false) {
                crimes.setLatitude(json.get("location").getAsJsonObject().get("latitude").getAsDouble());
                crimes.setLongitude(json.get("location").getAsJsonObject().get("longitude").getAsDouble());
            }
            if (json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").isJsonNull() == false && json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").isJsonNull() == false) {
                crimes.setStreetId(json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").getAsLong());
                crimes.setStreetName(json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").toString().replaceAll("\"",""));
            }

        } else {
            crimes.setLatitude(null);
            crimes.setLongitude(null);
            crimes.setStreetId(null);
            crimes.setStreetName(null);
        }
        if (json.get("context").isJsonNull() == false) {
            crimes.setContext(json.get("context").toString().replaceAll("\"", ""));
        } else {
            crimes.setContext(null);
        }
        if (json.get("outcome_status").isJsonNull() == false) {
            if (json.get("outcome_status").getAsJsonObject().get("category").isJsonNull() == false && json.get("outcome_status").getAsJsonObject().get("date").isJsonNull() == false) {
                crimes.setOutcomeCategory(json.get("outcome_status").getAsJsonObject().get("category").toString().replaceAll("\"",""));
                crimes.setOutcomeDate(YearMonth.parse(json.get("outcome_status").getAsJsonObject().get("date").toString().replaceAll("\"", "")));
            }
        } else {
            crimes.setOutcomeCategory(null);
            crimes.setOutcomeDate(null);
        }
        if (json.get("persistent_id").isJsonNull() == false) {
            crimes.setPersistentId(json.get("persistent_id").toString().replaceAll("\"", ""));
        } else {
            crimes.setPersistentId(null);
        }
        if (json.get("id").isJsonNull() == false) {
            crimes.setId(json.get("id").getAsLong());
        } else {
            crimes.setId(null);
        }
        if (json.get("location_subtype").isJsonNull() == false) {
            crimes.setLocationSubtype(json.get("location_subtype").toString().replaceAll("\"", ""));
        } else {
            crimes.setLocationSubtype(null);
        }
        if (json.get("month").isJsonNull() == false) {
            crimes.setMonth(YearMonth.parse(json.get("month").toString().replaceAll("\"", "")));
        } else {
            crimes.setMonth(null);
        }
        return  crimes;
    }
}
