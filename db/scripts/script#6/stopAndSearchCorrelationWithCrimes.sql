WITH street_level_crimes_by_date AS (
  SELECT
    street_id,
    street_name,
    category,
    month
  FROM streetlevelcrimes
  GROUP BY
    street_id,
    street_name,
    category,
    month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

stop_and_searches_by_force_by_date AS (
  SELECT
    street_id,
    street_name,
    object_of_search,
    datetime
  FROM stopandsearchesbyforce
  GROUP BY
    street_id,
    street_name,
    object_of_search,
    datetime
  HAVING TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

drugs_crime_count AS (
  SELECT
    street_id,
    street_name,
    month,
    COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY
    month,
    category,
    street_id,
    street_name
  HAVING category = 'drugs'
),

drugs_stop_and_search_count AS (
  SELECT
    street_id,
    street_name,
    to_char(datetime, 'YYYY-MM') AS datetime,
    COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY
    to_char(datetime, 'YYYY-MM'),
    object_of_search,
    street_id,
    street_name
  HAVING object_of_search = 'Controlled drugs'
),

weapons_crimes_count AS (
  SELECT
    street_id,
    street_name,
    month,
    COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY
    month,
    category,
    street_id,
    street_name
  HAVING category = 'possession-of-weapons'
),

weapons_stop_and_search_count AS (
  SELECT
    to_char(datetime, 'YYYY-MM') AS datetime,
    street_id,
    COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY
    to_char(datetime, 'YYYY-MM'),
    object_of_search,
    street_id
  HAVING object_of_search = 'Offensive weapons' OR object_of_search = 'Firearms'
),

theft_crimes_count AS (
  SELECT
    street_id,
    street_name,
    month,
    COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY
    month,
    category,
    street_id,
    street_name
  HAVING category = 'theft-from-the-person' OR category = 'shoplifting'
),

theft_stop_and_search_count AS (
  SELECT
    to_char(datetime, 'YYYY-MM') AS datetime,
    street_id,
    COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY
    to_char(datetime, 'YYYY-MM'),
    object_of_search,
    street_id
  HAVING object_of_search = 'Stolen goods'
)

SELECT
  drugs_count.street_id,
  drugs_count.street_name,
  drugs_count.month,
  drugs_count.count AS drugs_crime_count,
  stop_drugs_count.count AS drugs_stop_and_search_count,
  weapon_crimes.count AS weapons_crimes_count,
  stop_weapon_count.count AS weapons_stop_and_search_count,
  theft_crimes.count AS theft_crimes_count,
  stop_theft_count.count AS theft_stop_and_search_count
FROM drugs_crime_count AS drugs_count
LEFT JOIN drugs_stop_and_search_count AS stop_drugs_count ON drugs_count.month = stop_drugs_count.datetime AND drugs_count.street_id = stop_drugs_count.street_id
LEFT JOIN weapons_crimes_count AS weapon_crimes ON stop_drugs_count.datetime = weapon_crimes.month AND stop_drugs_count.street_id = weapon_crimes.street_id
LEFT JOIN weapons_stop_and_search_count AS stop_weapon_count ON weapon_crimes.month = stop_weapon_count.datetime AND weapon_crimes.street_id = stop_weapon_count.street_id
LEFT JOIN theft_crimes_count AS theft_crimes ON stop_weapon_count.datetime = theft_crimes.month AND stop_weapon_count.street_id = theft_crimes.street_id
LEFT JOIN theft_stop_and_search_count AS stop_theft_count ON stop_theft_count.datetime = theft_crimes.month AND stop_theft_count.street_id = theft_crimes.street_id
GROUP BY
  drugs_count.street_id,
  drugs_count.street_name,
  drugs_count.month,
  drugs_crime_count,
  drugs_stop_and_search_count,
  weapons_crimes_count,
  weapons_stop_and_search_count,
  theft_crimes_count,
  theft_stop_and_search_count
ORDER BY drugs_count.month