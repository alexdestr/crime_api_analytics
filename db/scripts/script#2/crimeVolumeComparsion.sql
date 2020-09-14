WITH crimes_count_for_each_category AS (
  SELECT
    category,
    MONTH AS mth,
    COUNT(*)
  OVER(PARTITION BY category, MONTH ORDER BY MONTH)
  FROM streetlevelcrimes
),

crimes_count AS (
  SELECT *
  FROM crimes_count_for_each_category
  GROUP BY
    crimes_count_for_each_category.category,
    crimes_count_for_each_category.mth,
    crimes_count_for_each_category.count
  ORDER BY crimes_count_for_each_category.mth
),

month_to_month_comparsion AS
(SELECT
  current_month_data.category,
  current_month_data.mth,
  last_month_data.count AS previous_month_count,
  current_month_data.count AS current_month_count
 FROM crimes_count AS last_month_data
   JOIN crimes_count AS current_month_data ON last_month_data.category = current_month_data.category
 WHERE last_month_data.mth = to_char(to_date(current_month_data.mth, 'YYYY-MM') - INTERVAL '1 month', 'YYYY-MM') )

SELECT
  category,
  mth,
  previous_month_count,
  current_month_count,
  current_month_count - previous_month_count AS delta,
  concat(cast(round((cast(current_month_count AS decimal) - cast(previous_month_count AS decimal)) / cast(previous_month_count AS decimal) * 100, 1) AS text), '%') AS growth_rate
FROM month_to_month_comparsion
WHERE TO_DATE(mth, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
AND TO_DATE(mth, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')