WITH street_level_crimes_by_date AS (
  SELECT street_id, street_name, category, month FROM streetlevelcrimes
  GROUP BY street_id, street_name, category, month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

stop_and_searches_by_force_by_date AS (
  SELECT street_id, street_name, object_of_search FROM stopandsearchesbyforce
  GROUP BY street_id, street_name, object_of_search, datetime
  HAVING TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

drugs_crime_count AS (
  SELECT street_id, street_name, month, COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY month, category, street_id, street_name
  HAVING category = 'drugs'
),

drugs_stop_and_search_count AS (
  SELECT street_id, street_name, to_char(datetime, 'YYYY-MM') AS datetime, COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY to_char(datetime, 'YYYY-MM'), object_of_search, street_id, street_name
  HAVING object_of_search = 'Controlled drugs'
),

weapons_crimes_count AS (
  SELECT street_id, street_name, month, COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY month, category, street_id, street_name
  HAVING category = 'possession-of-weapons'
),

weapons_stop_and_search_count AS (
  SELECT to_char(datetime, 'YYYY-MM') AS datetime, street_id, COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY to_char(datetime, 'YYYY-MM'), object_of_search, street_id
  HAVING object_of_search = 'Offensive weapons' OR object_of_search = 'Firearms'
),

theft_crimes_count AS (
  SELECT street_id, street_name, month, COUNT(*)
  FROM street_level_crimes_by_date
  GROUP BY month, category, street_id, street_name
  HAVING category = 'theft-from-the-person' OR category = 'shoplifting'
),

theft_stop_and_search_count AS (
  SELECT to_char(datetime, 'YYYY-MM') AS datetime, street_id, COUNT(*)
  FROM stopandsearchesbyforce
  GROUP BY to_char(datetime, 'YYYY-MM'), object_of_search, street_id
  HAVING object_of_search = 'Stolen goods'
)

SELECT a.street_id, a.street_name, a.month, a.count AS drugs_crime_count, b.count AS drugs_stop_and_search_count, c.count AS weapons_crimes_count, d.count AS weapons_stop_and_search_count, f.count AS theft_crimes_count, g.count AS theft_stop_and_search_count
FROM drugs_crime_count AS a
LEFT JOIN drugs_stop_and_search_count AS b ON a.month = b.datetime AND a.street_id = b.street_id
LEFT JOIN weapons_crimes_count AS c ON b.datetime = c.month AND b.street_id = c.street_id
LEFT JOIN weapons_stop_and_search_count AS d ON c.month = d.datetime AND c.street_id = d.street_id
LEFT JOIN theft_crimes_count AS f ON d.datetime = f.month AND d.street_id = f.street_id
LEFT JOIN theft_stop_and_search_count AS g ON g.datetime = f.month AND g.street_id = f.street_id
GROUP BY a.street_id, a.street_name, a.month, drugs_crime_count, drugs_stop_and_search_count, weapons_crimes_count, weapons_stop_and_search_count, theft_crimes_count, theft_stop_and_search_count
ORDER BY a.street_id