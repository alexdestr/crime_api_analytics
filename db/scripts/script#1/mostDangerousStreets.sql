SELECT c.street_id, c.street_name, 'from ('|| ? ||') till (' || ? || ')' AS period, c.cnt AS cnt
FROM
(SELECT b.street_id, b.street_name, COUNT(*) AS cnt FROM
(SELECT *
FROM
  (SELECT * FROM streetlevelcrimes
    WHERE TO_DATE(month, 'YYYY-MM') >= TO_DATE(''|| ? ||'', 'YYYY-MM')
    AND TO_DATE(month, 'YYYY-MM') <= TO_DATE(''|| ? ||'', 'YYYY-MM')
  )AS a
) AS b
GROUP BY b.street_id, b.street_name) AS c
ORDER BY c.cnt DESC
LIMIT 10
