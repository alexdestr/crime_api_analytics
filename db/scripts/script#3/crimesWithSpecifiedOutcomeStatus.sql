WITH count_crime_with_current_category AS (
  SELECT street_id, street_name, outcome_category, month, COUNT(*) AS count FROM streetlevelcrimes
  WHERE outcome_category = (''|| ? ||'')
  GROUP BY street_id, street_name, outcome_category, month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

count_crime_with_other_category AS (
  SELECT street_id, street_name, month, COUNT(*) AS count FROM streetlevelcrimes
  WHERE outcome_category IS NOT NULL
  GROUP BY street_id, street_name, month
  HAVING TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
)

SELECT a.street_id, a.street_name, a.outcome_category, concat(round(cast(a.count AS decimal) / cast(b.count AS decimal) * 100.0, 1), '%') AS percentage_of_the_total_crimes FROM
count_crime_with_current_category AS a
JOIN count_crime_with_other_category AS b
ON a.street_id = b.street_id AND a.month = b.month
GROUP BY a.street_id, a.street_name, a.outcome_category, percentage_of_the_total_crimes
