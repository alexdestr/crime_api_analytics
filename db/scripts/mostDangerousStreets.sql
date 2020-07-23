SELECT c.street_id, c.street_name, 'from <start> till <end>' AS period, c.cnt
FROM (SELECT b.street_id, b.street_name, COUNT(*) AS cnt FROM
(SELECT a.type AS type,
       a.involved_person AS involved_person,
       a.operation AS operation,
       a.operation_name AS operation_name,
       a.latitude AS latitude,
       a.longitude AS longitude,
       a.street_id AS street_id,
       a.street_name AS street_name,
       a.gender AS gender,
       a.age_range AS age_range,
       a.self_defined_ethnicity AS self_defined_ethnicity,
       a.officer_defined_ethnicity AS officer_defined_ethnicity,
       a.legislation AS legislation,
       a.object_of_search AS object_of_search,
       a.outcome AS outcome,
       a.outcome_linked_to_object_of_search AS outcome_linked_to_object_of_search,
       a.removal_of_more_than_outer_clothing AS removal_of_more_than_outer_clothing
FROM
  (SELECT * FROM stopandsearchesbyforce
    WHERE datetime >= TO_DATE('2018-01', 'YYYY-MM')
    AND datetime <= TO_DATE('2018-02', 'YYYY-MM')
  )AS a
) AS b
GROUP BY b.street_id, b.street_name) AS c
ORDER BY c.cnt DESC
LIMIT 10
