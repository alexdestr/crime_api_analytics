CREATE TABLE crimeCategories (
url TEXT PRIMARY KEY,
name TEXT
);

CREATE TABLE streetLevelCrimes (
category TEXT,
locationType TEXT,
latitude DECIMAL,
longitude DECIMAL,
streetId BIGINT,
streetName TEXT,
context BIGINT,
outcomeCategory TEXT,
outcomeDate TIMESTAMP,
persistentId TEXT,
id BIGINT PRIMARY KEY UNIQUE,
locationSubtype TEXT,
month TEXT,
CONSTRAINT FK_crime_category FOREIGN KEY(category)
REFERENCES crimeCategories(url)
);