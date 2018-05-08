/*** Inserts data into the event / seat tables ***/
INSERT INTO `event`
  SELECT *
  FROM GENERATE_SERIES (1, 4);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    1,
    0,
    1,
    'ADULT'
  FROM GENERATE_SERIES (1, 60);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    1,
    1,
    0,
    'CHILD'
  FROM GENERATE_SERIES (61, 90);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    2,
    0,
    0,
    'ADULT'
  FROM GENERATE_SERIES (91, 160);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    2,
    1,
    0,
    'CHILD'
  FROM GENERATE_SERIES (161, 220);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    3,
    1,
    1,
    'CHILD'
  FROM GENERATE_SERIES (221, 250);

INSERT INTO `seat` (id, event_id, available, aisle, type)
  SELECT
    *,
    3,
    1,
    0,
    'ADULT'
  FROM GENERATE_SERIES (251, 300);