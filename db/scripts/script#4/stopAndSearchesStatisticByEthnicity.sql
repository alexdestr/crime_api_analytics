WITH count_by_date AS (
  SELECT
    officer_defined_ethnicity,
    datetime,
    outcome,
    COUNT(*) AS count
  FROM stopandsearchesbyforce
  GROUP BY
    officer_defined_ethnicity,
    datetime,
    outcome
  HAVING TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
  AND TO_DATE(to_char(datetime, 'YYYY-MM'), 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
),

arrest_count AS (
  SELECT
    officer_defined_ethnicity,
    COUNT(*) AS count
  FROM count_by_date
  WHERE outcome = 'Arrest'
  GROUP BY
    officer_defined_ethnicity
),

release_count AS (
  SELECT
    officer_defined_ethnicity,
    COUNT(*) AS count
  FROM count_by_date
  WHERE outcome = 'A no further action disposal'
  GROUP BY
    officer_defined_ethnicity
),

other_count AS (
  SELECT
    officer_defined_ethnicity,
    COUNT(*) AS count
  FROM count_by_date
  WHERE outcome != 'Arrest' AND outcome != 'A no further action disposal' AND outcome IS NOT NULL
  GROUP BY
    officer_defined_ethnicity
),

object_of_search AS (
  SELECT
    officer_defined_ethnicity,
    object_of_search,
    count,
  ROW_NUMBER() OVER (PARTITION BY officer_defined_ethnicity ORDER BY count DESC) AS rn
  FROM (SELECT
        officer_defined_ethnicity,
        object_of_search,
        COUNT(*) AS count
        FROM stopandsearchesbyforce
        GROUP BY
        officer_defined_ethnicity,
        object_of_search
  ) AS grouped_count
)

SELECT
  common_count.officer_defined_ethnicity,
  common_count.count,
  concat(round(cast(arrest_outcome.count AS decimal) / (cast(common_count.count AS decimal)) * 100, 1), '%') AS arrest_rate,
  concat(round(cast(release_outcome.count AS decimal) / (cast(common_count.count AS decimal)) * 100, 1), '%') AS release_count,
  concat(round(cast(other_outcome.count AS decimal) / (cast(common_count.count AS decimal)) * 100, 1), '%') AS other_outcomes_count,
  object.object_of_search
FROM
(SELECT
  grouped_count_by_date.officer_defined_ethnicity,
  COUNT(*) AS count
 FROM count_by_date AS grouped_count_by_date
 GROUP BY
  grouped_count_by_date.officer_defined_ethnicity)
AS common_count
  JOIN arrest_count AS arrest_outcome ON common_count.officer_defined_ethnicity = arrest_outcome.officer_defined_ethnicity
  JOIN release_count AS release_outcome ON arrest_outcome.officer_defined_ethnicity = release_outcome.officer_defined_ethnicity
  JOIN other_count AS other_outcome ON release_outcome.officer_defined_ethnicity = other_outcome.officer_defined_ethnicity
  JOIN object_of_search AS object ON other_outcome.officer_defined_ethnicity = object.officer_defined_ethnicity
GROUP BY
  common_count.officer_defined_ethnicity,
  common_count.count,
  arrest_rate,
  release_count,
  other_outcomes_count,
  object.object_of_search,
  object.rn
HAVING object.rn = 1