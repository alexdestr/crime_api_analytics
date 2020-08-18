WITH count_crime_with_current_category AS (
  SELECT
    street_id,
    street_name,
    outcome_category,
    month,
    COUNT(*) AS count
  FROM streetlevelcrimes
  WHERE outcome_category = (''|| ? ||'')
  GROUP BY
    street_id,
    street_name,
    outcome_category,
    month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

count_crime_with_other_category AS (
  SELECT
    street_id,
    street_name,
    month,
  COUNT(*) AS count
  FROM streetlevelcrimes
  WHERE outcome_category IS NOT NULL
  GROUP BY
    street_id,
    street_name,
    month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
)

SELECT
  current_count.street_id,
  current_count.street_name,
  current_count.outcome_category,
concat(round(cast(current_count.count AS decimal) / cast(other_count.count AS decimal) * 100.0, 1), '%') AS percentage_of_the_total_crimes FROM
count_crime_with_current_category AS current_count
JOIN count_crime_with_other_category AS other_count
ON current_count.street_id = other_count.street_id AND current_count.month = other_count.month
GROUP BY
  current_count.street_id,
  current_count.street_name,
  current_count.outcome_category,
  percentage_of_the_total_crimes
