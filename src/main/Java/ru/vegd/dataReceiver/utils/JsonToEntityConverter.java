package ru.vegd.dataReceiver.utils;

import com.google.gson.JsonObject;
import ru.vegd.entity.CrimeCategory;
import ru.vegd.entity.ForcesList;
import ru.vegd.entity.StreetLevelCrime;

import java.time.YearMonth;

public class JsonToEntityConverter {

    private final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(JsonToEntityConverter.class.getName());

    // TODO: move to methods
    ForcesList forcesList = new ForcesList();
    CrimeCategory crimeCategory = new CrimeCategory();
    StreetLevelCrime crimes = new StreetLevelCrime();

    public ForcesList convertToForcesList(JsonObject json) {
        if (!json.get("id").isJsonNull()) {
            // TODO: init var and set(var)
            forcesList.setId(json.get("id").getAsString().replaceAll("\"", ""));
        }
        if (!json.get("name").isJsonNull()) {
            forcesList.setName(json.get("name").getAsString().replaceAll("\"", ""));
        }
        return forcesList;
    }

    public CrimeCategory convertToCrimeCategories(JsonObject json) {
        if (!json.get("url").isJsonNull()) {
            crimeCategory.setUrl(json.get("url").getAsString().replaceAll("\"", ""));
        } else {
            crimeCategory.setUrl(null);
        }
        if (!json.get("name").isJsonNull()) {
            crimeCategory.setName(json.get("name").getAsString().replaceAll("\"", ""));
        } else {
            crimeCategory.setName(null);
        }
        return crimeCategory;
    }

    public StreetLevelCrime convertToStreetLevelCrimes(JsonObject json) {
        if (!json.get("category").isJsonNull()) {
            crimes.setCategory(json.get("category").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setCategory(null);
        }
        if (!json.get("location_type").isJsonNull()) {
            crimes.setLocatonType(json.get("location_type").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setLocatonType(null);
        }
        if (!json.get("location").isJsonNull()) {
            if (!json.get("location").getAsJsonObject().get("latitude").isJsonNull() && !json.get("location").getAsJsonObject().get("longitude").isJsonNull()) {
                crimes.setLatitude(json.get("location").getAsJsonObject().get("latitude").getAsDouble());
                crimes.setLongitude(json.get("location").getAsJsonObject().get("longitude").getAsDouble());
            }
            if (!json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").isJsonNull() && !json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").isJsonNull()) {
                crimes.setStreetId(json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").getAsLong());
                crimes.setStreetName(json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").getAsString().replaceAll("\"",""));
            }

        } else {
            crimes.setLatitude(null);
            crimes.setLongitude(null);
            crimes.setStreetId(null);
            crimes.setStreetName(null);
        }
        if (!json.get("context").isJsonNull() && !json.get("context").getAsString().isEmpty()) {
            crimes.setContext(json.get("context").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setContext(null);
        }
        if (!json.get("outcome_status").isJsonNull()) {
            if (!json.get("outcome_status").getAsJsonObject().get("category").isJsonNull() && !json.get("outcome_status").getAsJsonObject().get("date").isJsonNull()) {
                crimes.setOutcomeCategory(json.get("outcome_status").getAsJsonObject().get("category").getAsString().replaceAll("\"",""));
                crimes.setOutcomeDate(YearMonth.parse(json.get("outcome_status").getAsJsonObject().get("date").getAsString().replaceAll("\"", "")));
            }
        } else {
            crimes.setOutcomeCategory(null);
            crimes.setOutcomeDate(null);
        }
        if (!json.get("persistent_id").isJsonNull() && !json.get("persistent_id").getAsString().isEmpty()) {
            crimes.setPersistentId(json.get("persistent_id").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setPersistentId(null);
        }
        if (!json.get("id").isJsonNull()) {
            crimes.setId(json.get("id").getAsLong());
        } else {
            crimes.setId(null);
        }
        if (!json.get("location_subtype").isJsonNull() && !json.get("location_subtype").getAsString().isEmpty()) {
            crimes.setLocationSubtype(json.get("location_subtype").getAsString().replaceAll("\"", ""));
        } else {
            crimes.setLocationSubtype(null);
        }
        if (!json.get("month").isJsonNull()) {
            crimes.setMonth(YearMonth.parse(json.get("month").getAsString().replaceAll("\"", "")));
        } else {
            crimes.setMonth(null);
        }
        return  crimes;
    }
}
