SELECT
  final_rows.street_id,
  final_rows.street_name,
  'from ('|| ? ||') till (' || ? || ')' AS period,
  final_rows.count
FROM (
  SELECT
    rows_by_date.street_id,
    rows_by_date.street_name,
    COUNT(*) AS count
  FROM (
    SELECT *
    FROM streetlevelcrimes
    WHERE TO_DATE(month, 'YYYY-MM') >= TO_DATE('' || ? || '', 'YYYY-MM')
    AND TO_DATE(month, 'YYYY-MM') <= TO_DATE('' || ? || '', 'YYYY-MM')
  ) AS rows_by_date
  GROUP BY rows_by_date.street_id, rows_by_date.street_name) AS final_rows
ORDER BY final_rows.count
DESC LIMIT 10
