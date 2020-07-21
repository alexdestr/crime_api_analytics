CREATE TABLE crimeCategories (
  url TEXT PRIMARY KEY,
  name TEXT
);

CREATE TABLE forcesList (
  id TEXT PRIMARY KEY,
  name TEXT
);

CREATE TABLE streetLevelCrimes (
  category TEXT,
  location_type TEXT,
  latitude DECIMAL,
  longitude DECIMAL,
  street_id BIGINT,
  street_name TEXT,
  context TEXT,
  outcome_category TEXT,
  outcome_date TEXT,
  persistent_id TEXT,
  id BIGINT NOT NULL PRIMARY KEY UNIQUE,
  location_subtype TEXT,
  month TEXT,
  CONSTRAINT FK_crime_category FOREIGN KEY(category)
  REFERENCES crimeCategories(url)
);

CREAtE TABLE stopAndSearchesByForce (
  type TEXT,
  involved_person TEXT,
  datetime TIMESTAMP,
  operation BOOLEAN,
  operation_name TEXT,
  latitude DECIMAL,
  longitude DECIMAL,
  street_id BIGINT,
  street_name TEXT,
  gender TEXT,
  age_range TEXT,
  self_defined_ethnicity TEXT,
  officer_defined_ethnicity TEXT,
  legislation TEXT,
  object_of_search TEXT,
  outcome TEXT,
  outcome_linked_to_object_of_search BOOLEAN,
  removal_of_more_than_outer_clothing BOOLEAN,
  CONSTRAINT UC_stopandSearchesByForce UNIQUE (type, datetime, age_range, gender, officer_defined_ethnicity, legislation, self_defined_ethnicity)
);