WITH crimes_count AS
(SELECT *
 FROM
 (SELECT *
  FROM
  (SELECT category,
          MONTH AS mth,
          COUNT(*) OVER(PARTITION BY category, MONTH
          ORDER BY MONTH)
   FROM streetlevelcrimes) AS a
  GROUP BY a.category,
  a.mth,
  a.count
  ORDER BY a.mth) AS b),
odr AS
(SELECT b.category,
        b.mth,
        a.count AS previous_month_count,
        b.count AS current_month_count
 FROM crimes_count AS a
   JOIN crimes_count AS b ON a.category = b.category
 WHERE a.mth = to_char(to_date(b.mth, 'YYYY-MM') - INTERVAL '1 month', 'YYYY-MM') )
SELECT category,
       mth,
       previous_month_count,
       current_month_count,
       current_month_count - previous_month_count AS delta,
       concat(cast(round((cast(current_month_count AS decimal) - cast(previous_month_count AS decimal)) / cast(previous_month_count AS decimal) * 100, 1) AS text), '%') AS growth_rate
FROM odr
WHERE TO_DATE(mth, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
AND TO_DATE(mth, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')