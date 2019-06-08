ALTER TABLE natureza ADD COLUMN carimbo_id INTEGER REFERENCES carimbo(id);
UPDATE natureza set carimbo_id = 1;