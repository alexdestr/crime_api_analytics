package ru.vegd.dataReceiver.utils;

import com.google.gson.JsonObject;
import ru.vegd.entity.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;

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
                String name = json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
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
                String outcomeCategory = json.get("outcome_status").getAsJsonObject().get("category").getAsString().replaceAll("\"", "");
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

    public StopAndSearchesByForce convertToStopAndSearchByForce(JsonObject json) {
        StopAndSearchesByForce stopAndSearchesByForce = new StopAndSearchesByForce();
        if (!json.get("type").isJsonNull()) {
            String type = json.get("type").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setType(type);
        } else {
            stopAndSearchesByForce.setType(null);
        }

        if (!json.get("involved_person").isJsonNull()) {
            String involvedPerson = json.get("involved_person").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setInvolvedPerson(involvedPerson);
        }

        if (!json.get("datetime").isJsonNull()) {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_DATE_TIME;
            TemporalAccessor accessor = timeFormatter.parse(json.get("datetime").getAsString());
            Instant instant = Instant.from(accessor);
            Timestamp timestamp = Timestamp.from(instant);
            stopAndSearchesByForce.setDateTime(timestamp);
        } else {
            stopAndSearchesByForce.setDateTime(null);
        }

        if (!json.get("operation").isJsonNull()) {
            Boolean operation = json.get("operation").getAsBoolean();
            stopAndSearchesByForce.setOperation(operation);
        } else {
            stopAndSearchesByForce.setOperation(null);
        }

        if (!json.get("operation_name").isJsonNull()) {
            String operationName = json.get("operation_name").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setOperationName("operation_name");
        } else {
            stopAndSearchesByForce.setOperationName(null);
        }

        if (!json.get("location").isJsonNull()) {
            if (!json.get("location").getAsJsonObject().get("latitude").isJsonNull() && !json.get("location").getAsJsonObject().get("longitude").isJsonNull()) {
                Double latitude = json.get("location").getAsJsonObject().get("latitude").getAsDouble();
                stopAndSearchesByForce.setLatitude(latitude);
                Double longitude = json.get("location").getAsJsonObject().get("longitude").getAsDouble();
                stopAndSearchesByForce.setLongitude(longitude);
            } else {
                stopAndSearchesByForce.setLatitude(null);
                stopAndSearchesByForce.setLongitude(null);
            }
            if (!json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").isJsonNull() && !json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").isJsonNull()) {
                Long id = json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("id").getAsLong();
                stopAndSearchesByForce.setStreetId(id);
                String name = json.get("location").getAsJsonObject().get("street").getAsJsonObject().get("name").getAsString().replaceAll("\"", "");
                stopAndSearchesByForce.setStreetName(name);
            } else {
                stopAndSearchesByForce.setStreetId(null);
                stopAndSearchesByForce.setStreetName(null);
            }
        } else {
            stopAndSearchesByForce.setLatitude(null);
            stopAndSearchesByForce.setLongitude(null);
            stopAndSearchesByForce.setStreetId(null);
            stopAndSearchesByForce.setStreetName(null);
        }

        if (!json.get("gender").isJsonNull()) {
            Gender gender = Gender.valueOf(json.get("gender").getAsString().replaceAll("\"", ""));
            stopAndSearchesByForce.setGender(gender);
        } else {
            stopAndSearchesByForce.setGender(null);
        }

        if (!json.get("age_range").isJsonNull()) {
            String ageRange = json.get("age_range").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setAgeRange(ageRange);
        } else {
            stopAndSearchesByForce.setAgeRange(null);
        }

        if (!json.get("self_defined_ethnicity").isJsonNull()) {
            String selfDefinedEthnicity = json.get("self_defined_ethnicity").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setSelfDefinedEthnicity(selfDefinedEthnicity);
        } else {
            stopAndSearchesByForce.setSelfDefinedEthnicity(null);
        }

        if (!json.get("officer_defined_ethnicity").isJsonNull()) {
            String officerDefinedEthnicity = json.get("officer_defined_ethnicity").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setOfficerDefinedEthnicity(officerDefinedEthnicity);
        } else {
            stopAndSearchesByForce.setOfficerDefinedEthnicity(null);
        }

        if (!json.get("legislation").isJsonNull()) {
            String legislation = json.get("legislation").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setLegislation(legislation);
        } else {
            stopAndSearchesByForce.setLegislation(null);
        }

        if (!json.get("object_of_search").isJsonNull()) {
            String objectOfSearch = json.get("object_of_search").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setObjectOfSearch(objectOfSearch);
        } else {
            stopAndSearchesByForce.setObjectOfSearch(null);
        }

        if (!json.get("outcome").isJsonNull()) {
            String outcome = json.get("outcome").getAsString().replaceAll("\"", "");
            stopAndSearchesByForce.setOutcome(outcome);
        } else {
            stopAndSearchesByForce.setOutcome(null);
        }

        if (!json.get("outcome_linked_to_object_of_search").isJsonNull()) {
            Boolean outcomeLinkedToObjectOfSearch = json.get("outcome_linked_to_object_of_search").getAsBoolean();
            stopAndSearchesByForce.setOutcomeLinkedToObjectOfSearch(outcomeLinkedToObjectOfSearch);
        } else {
            stopAndSearchesByForce.setOutcomeLinkedToObjectOfSearch(null);
        }

        if (!json.get("removal_of_more_than_outer_clothing").isJsonNull()) {
            Boolean removalOfMoreThanOuterClothing = json.get("removal_of_more_than_outer_clothing").getAsBoolean();
            stopAndSearchesByForce.setRemovalOfMoreThanOuterClothing(removalOfMoreThanOuterClothing);
        } else {
            stopAndSearchesByForce.setRemovalOfMoreThanOuterClothing(null);
        }

        return stopAndSearchesByForce;
    }
}
