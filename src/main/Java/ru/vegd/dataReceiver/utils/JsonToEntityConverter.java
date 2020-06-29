package ru.vegd.dataReceiver.utils;

import com.google.gson.JsonObject;
import ru.vegd.entity.CrimeCategories;
import ru.vegd.entity.StreetLevelCrimes;

import java.time.YearMonth;

public class JsonToEntityConverter {

    CrimeCategories crimeCategories = new CrimeCategories();
    StreetLevelCrimes crimes = new StreetLevelCrimes();

    public CrimeCategories convertToCrimeCategories(JsonObject json) {
        if (json.get("url").isJsonNull() == false) {
            crimeCategories.setUrl(json.get("url").getAsString().replaceAll("\"", ""));
        } else {
            crimeCategories.setUrl(null);
        }
        if (json.get("name").isJsonNull() == false) {
            crimeCategories.setName(json.get("name").getAsString().replaceAll("\"", ""));
        } else {
            crimeCategories.setName(null);
        }
        return crimeCategories;
    }

    public StreetLevelCrimes convertToStreetLevelCrimes(JsonObject json) {
        if (json.get("category").isJsonNull() == false) {
            crimes.setCategory(json.get("category").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setCategory(null);
        }
        if (json.get("location_type").isJsonNull() == false) {
            crimes.setLocatonType(json.get("location_type").getAsString().replaceAll("\"", ""));
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
                crimes.setStreetName(json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").getAsString().replaceAll("\"",""));
            }

        } else {
            crimes.setLatitude(null);
            crimes.setLongitude(null);
            crimes.setStreetId(null);
            crimes.setStreetName(null);
        }
        if (json.get("context").isJsonNull() == false && !json.get("context").getAsString().isEmpty()) {
            crimes.setContext(json.get("context").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setContext(null);
        }
        if (json.get("outcome_status").isJsonNull() == false) {
            if (json.get("outcome_status").getAsJsonObject().get("category").isJsonNull() == false && json.get("outcome_status").getAsJsonObject().get("date").isJsonNull() == false) {
                crimes.setOutcomeCategory(json.get("outcome_status").getAsJsonObject().get("category").getAsString().replaceAll("\"",""));
                crimes.setOutcomeDate(YearMonth.parse(json.get("outcome_status").getAsJsonObject().get("date").getAsString().replaceAll("\"", "")));
            }
        } else {
            crimes.setOutcomeCategory(null);
            crimes.setOutcomeDate(null);
        }
        if (json.get("persistent_id").isJsonNull() == false && !json.get("persistent_id").getAsString().isEmpty()) {
            crimes.setPersistentId(json.get("persistent_id").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setPersistentId(null);
        }
        if (json.get("id").isJsonNull() == false) {
            crimes.setId(json.get("id").getAsLong());
        } else {
            crimes.setId(null);
        }
        if (json.get("location_subtype").isJsonNull() == false && !json.get("location_subtype").getAsString().isEmpty()) {
            crimes.setLocationSubtype(json.get("location_subtype").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setLocationSubtype(null);
        }
        if (json.get("month").isJsonNull() == false) {
            crimes.setMonth(YearMonth.parse(json.get("month").getAsString().replaceAll("\"", "")));
        } else {
            crimes.setMonth(null);
        }
        return  crimes;
    }
}
