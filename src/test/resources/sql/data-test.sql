/*Inserts test data into the event / seat tables for the integration tests*/
INSERT INTO `event`
  SELECT
    *
  FROM GENERATE_SERIES (1, 5);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    1,
    0,
    1,
    'adult'
  FROM GENERATE_SERIES (1, 50);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    1,
    1,
    0,
    'child'
  FROM GENERATE_SERIES (51, 100);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    2,
    0,
    0,
    'adult'
  FROM GENERATE_SERIES (101, 150);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    2,
    1,
    0,
    'child'
  FROM GENERATE_SERIES (151, 200);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    3,
    1,
    1,
    'child'
  FROM GENERATE_SERIES (201, 250);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    3,
    1,
    0,
    'adult'
  FROM GENERATE_SERIES (251, 300);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    4,
    1,
    1,
    'child'
  FROM GENERATE_SERIES (301, 350);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    4,
    0,
    0,
    'child'
  FROM GENERATE_SERIES (351, 400);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    5,
    0,
    0,
    'child'
  FROM GENERATE_SERIES (401, 450);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    5,
    1,
    0,
    'adult'
  FROM GENERATE_SERIES (451, 500);