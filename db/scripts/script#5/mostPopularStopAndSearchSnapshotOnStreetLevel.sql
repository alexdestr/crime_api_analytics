WITH stopandsearchbydate AS (
  SELECT * FROM stopandsearchesbyforce
),

count_most_popular_age_range AS (
  SELECT street_id, street_name, age_range, COUNT(*)
  FROM stopandsearchbydate
  WHERE age_range IS NOT NULL
  GROUP BY street_id, street_name, age_range
),

most_popular_age_range AS (
  SELECT street_id, street_name, age_range, count,
         ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_age_range
),

count_most_popular_gender AS (
  SELECT street_id, street_name, gender, COUNT(*)
  FROM stopandsearchbydate
  WHERE gender IS NOT NULL
  GROUP BY street_id, street_name, gender
),

most_popular_gender AS (
  SELECT street_id, street_name, gender, count,
  ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_gender
),

count_most_popular_ethnicity AS (
  SELECT street_id, street_name, officer_defined_ethnicity, COUNT(*)
  FROM stopandsearchbydate
  WHERE officer_defined_ethnicity IS NOT NULL
  GROUP BY street_id, street_name, officer_defined_ethnicity
),

most_popular_ethnicity AS (
  SELECT street_id, street_name, officer_defined_ethnicity, count,
         ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_ethnicity
),

count_most_popular_object_of_search AS (
  SELECT street_id, street_name, object_of_search, COUNT(*)
  FROM stopandsearchbydate
  WHERE object_of_search IS NOT NULL
  GROUP BY street_id, street_name, object_of_search
),

most_popular_object_of_search AS (
  SELECT street_id, street_name, object_of_search, count,
         ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_object_of_search
),

count_most_popular_outcome AS (
  SELECT street_id, street_name, outcome, COUNT(*)
  FROM stopandsearchbydate
  WHERE outcome IS NOT NULL
  GROUP BY street_id, street_name, outcome
),

most_popular_outcome AS (
  SELECT street_id, street_name, outcome, count,
         ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_outcome
)

SELECT a.street_id, a.street_name, b.age_range, c.gender, d.officer_defined_ethnicity, f.object_of_search, g.outcome
FROM stopandsearchesbyforce AS a
JOIN most_popular_age_range AS b ON a.street_id = b.street_id AND b.rn = 1
JOIN most_popular_gender AS c ON b.street_id = c.street_id AND c.rn = 1
JOIN most_popular_ethnicity AS d ON c.street_id = d.street_id AND d.rn = 1
JOIN most_popular_object_of_search AS f ON d.street_id = f.street_id AND f.rn = 1
JOIN most_popular_outcome AS g ON f.street_id = g.street_id AND g.rn = 1
GROUP BY a.street_id, a.street_name, b.age_range, c.gender, d.officer_defined_ethnicity, f.object_of_search, g.outcome
