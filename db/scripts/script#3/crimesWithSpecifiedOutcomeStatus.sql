WITH crime_by_current_category AS (
  SELECT * FROM streetlevelcrimes
  WHERE outcome_category = 'Investigation complete; no suspect identified'
),

count_crime_with_current_category AS (
  SELECT street_id, street_name, outcome_category, COUNT(*) AS count FROM streetlevelcrimes
  WHERE outcome_category = 'Investigation complete; no suspect identified'
  GROUP BY street_id, street_name, outcome_category
),

count_crime_with_other_category AS (
  SELECT street_id, COUNT(*) AS count FROM streetlevelcrimes
  WHERE outcome_category IS NOT NULL
  GROUP BY street_id
)

SELECT a.street_id, a.street_name, a.outcome_category, concat(round(cast(a.count AS decimal) / cast(b.count AS decimal) * 100.0, 1), '%') AS percentage_of_the_total_crimes FROM
count_crime_with_current_category AS a
JOIN count_crime_with_other_category AS b
ON a.street_id = b.street_id
GROUP BY a.street_id, a.street_name, a.outcome_category, percentage_of_the_total_crimes