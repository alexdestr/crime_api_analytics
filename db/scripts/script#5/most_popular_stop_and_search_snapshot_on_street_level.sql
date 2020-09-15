WITH count_most_popular_age_range AS (
  SELECT
    street_id,
    street_name,
    age_range,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  WHERE age_range IS NOT NULL
  GROUP BY
    street_id,
    street_name,
    age_range
),

most_popular_age_range AS (
  SELECT
    street_id,
    street_name,
    age_range,
    count,
    ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_age_range
),

count_most_popular_gender AS (
  SELECT
    street_id,
    street_name,
    gender,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  WHERE gender IS NOT NULL AND gender != 'null'
  GROUP BY
    street_id,
    street_name,
    gender
),

most_popular_gender AS (
  SELECT
    street_id,
    street_name,
    gender,
    count,
    ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_gender
),

count_most_popular_ethnicity AS (
  SELECT
    street_id,
    street_name,
    officer_defined_ethnicity,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  WHERE officer_defined_ethnicity IS NOT NULL
  GROUP BY
    street_id,
    street_name,
    officer_defined_ethnicity
),

most_popular_ethnicity AS (
  SELECT
    street_id,
    street_name,
    officer_defined_ethnicity,
    count,
    ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_ethnicity
),

count_most_popular_object_of_search AS (
  SELECT
    street_id,
    street_name,
    object_of_search,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  WHERE object_of_search IS NOT NULL
  GROUP BY
    street_id,
    street_name,
    object_of_search
),

most_popular_object_of_search AS (
  SELECT
    street_id,
    street_name,
    object_of_search,
    count,
    ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_object_of_search
),

count_most_popular_outcome AS (
  SELECT
    street_id,
    street_name,
    outcome,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  WHERE outcome IS NOT NULL
  GROUP BY
    street_id,
    street_name,
    outcome
),

most_popular_outcome AS (
  SELECT
    street_id,
    street_name,
    outcome,
    count,
    ROW_NUMBER() OVER (PARTITION BY street_id, street_name ORDER BY count DESC) AS rn
  FROM count_most_popular_outcome
)

SELECT
  all_rows.street_id,
  all_rows.street_name,
  most_popular_age.age_range,
  most_popular_gender.gender,
  most_popular_ethnicity.officer_defined_ethnicity,
  most_popular_object_of_search.object_of_search,
  most_popular_outcome.outcome
FROM stopandsearchesbyforce AS all_rows
  JOIN most_popular_age_range AS most_popular_age ON all_rows.street_id = most_popular_age.street_id AND most_popular_age.rn = 1
  JOIN most_popular_gender AS most_popular_gender ON most_popular_age.street_id = most_popular_gender.street_id AND most_popular_gender.rn = 1
  JOIN most_popular_ethnicity AS most_popular_ethnicity ON most_popular_gender.street_id = most_popular_ethnicity.street_id AND most_popular_ethnicity.rn = 1
  JOIN most_popular_object_of_search AS most_popular_object_of_search ON most_popular_ethnicity.street_id = most_popular_object_of_search.street_id AND most_popular_object_of_search.rn = 1
  JOIN most_popular_outcome AS most_popular_outcome ON most_popular_object_of_search.street_id = most_popular_outcome.street_id AND most_popular_outcome.rn = 1
GROUP BY
  all_rows.street_id,
  all_rows.street_name,
  most_popular_age.age_range,
  most_popular_gender.gender,
  most_popular_ethnicity.officer_defined_ethnicity,
  most_popular_object_of_search.object_of_search,
  most_popular_outcome.outcome
