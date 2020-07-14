package ru.vegd.dataReceiver.utils;

import com.google.gson.JsonObject;
import ru.vegd.entity.CrimeCategory;
import ru.vegd.entity.Force;
import ru.vegd.entity.StreetLevelCrime;

import java.time.YearMonth;

public class JsonToEntityConverter {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JsonToEntityConverter.class.getName());

    public Force convertToForcesList(JsonObject json) {
        Force force = new Force();
        if (!json.get("id").isJsonNull()) {
            String id = json.get("id").getAsString().replaceAll("\"", "");
            force.setId(id);
        }
        if (!json.get("name").isJsonNull()) {
            String name = json.get("name").getAsString().replaceAll("\"", "");
            force.setName(name);
        }
        return force;
    }

    public CrimeCategory convertToCrimeCategories(JsonObject json) {
        CrimeCategory crimeCategory = new CrimeCategory();
        if (!json.get("url").isJsonNull()) {
            String url = json.get("url").getAsString().replaceAll("\"", "");
            crimeCategory.setUrl(url);
        } else {
            crimeCategory.setUrl(null);
        }
        if (!json.get("name").isJsonNull()) {
            String name = json.get("name").getAsString().replaceAll("\"", "");
            crimeCategory.setName(name);
        } else {
            crimeCategory.setName(null);
        }
        return crimeCategory;
    }

    public StreetLevelCrime convertToStreetLevelCrimes(JsonObject json) {
        StreetLevelCrime crimes = new StreetLevelCrime();
        if (!json.get("category").isJsonNull()) {
            String category = json.get("category").getAsString().replaceAll("\"", "");
            crimes.setCategory(category);
        } else {
            crimes.setCategory(null);
        }
        if (!json.get("location_type").isJsonNull()) {
            String locationType = json.get("location_type").getAsString().replaceAll("\"", "");
            crimes.setLocatonType(locationType);
        } else {
            crimes.setLocatonType(null);
        }
        if (!json.get("location").isJsonNull()) {
            if (!json.get("location").getAsJsonObject().get("latitude").isJsonNull() && !json.get("location").getAsJsonObject().get("longitude").isJsonNull()) {
                Double latitude = json.get("location").getAsJsonObject().get("latitude").getAsDouble();
                crimes.setLatitude(latitude);
                Double longitude = json.get("location").getAsJsonObject().get("longitude").getAsDouble();
                crimes.setLongitude(longitude);
            }
            if (!json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").isJsonNull() && !json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").isJsonNull()) {
                Long id = json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").getAsLong();
                crimes.setStreetId(id);
                String name = json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").getAsString().replaceAll("\"","");
                crimes.setStreetName(name);
            }

        } else {
            crimes.setLatitude(null);
            crimes.setLongitude(null);
            crimes.setStreetId(null);
            crimes.setStreetName(null);
        }
        if (!json.get("context").isJsonNull() && !json.get("context").getAsString().isEmpty()) {
            String context = json.get("context").getAsString().replaceAll("\"", "");
            crimes.setContext(context);
        } else {
            crimes.setContext(null);
        }
        if (!json.get("outcome_status").isJsonNull()) {
            if (!json.get("outcome_status").getAsJsonObject().get("category").isJsonNull() && !json.get("outcome_status").getAsJsonObject().get("date").isJsonNull()) {
                String outcomeCategory = json.get("outcome_status").getAsJsonObject().get("category").getAsString().replaceAll("\"","");
                crimes.setOutcomeCategory(outcomeCategory);
                YearMonth outcameDate = YearMonth.parse(json.get("outcome_status").getAsJsonObject().get("date").getAsString().replaceAll("\"", ""));
                crimes.setOutcomeDate(outcameDate);
            }
        } else {
            crimes.setOutcomeCategory(null);
            crimes.setOutcomeDate(null);
        }
        if (!json.get("persistent_id").isJsonNull() && !json.get("persistent_id").getAsString().isEmpty()) {
            String persistentId = json.get("persistent_id").getAsString().replaceAll("\"", "");
            crimes.setPersistentId(persistentId);
        } else {
            crimes.setPersistentId(null);
        }
        if (!json.get("id").isJsonNull()) {
            Long id = json.get("id").getAsLong();
            crimes.setId(id);
        } else {
            crimes.setId(null);
        }
        if (!json.get("location_subtype").isJsonNull() && !json.get("location_subtype").getAsString().isEmpty()) {
            String locationSubtype = json.get("location_subtype").getAsString().replaceAll("\"", "");
            crimes.setLocationSubtype(locationSubtype);
        } else {
            crimes.setLocationSubtype(null);
        }
        if (!json.get("month").isJsonNull()) {
            YearMonth month = YearMonth.parse(json.get("month").getAsString().replaceAll("\"", ""));
            crimes.setMonth(month);
        } else {
            crimes.setMonth(null);
        }
        return crimes;
    }
}
