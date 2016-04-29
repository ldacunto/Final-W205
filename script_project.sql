CREATE TABLE users
(
  id serial NOT NULL,
  userid character varying NOT NULL,
  ipaddress character varying NOT NULL,
  lon character varying NOT NULL,
  lat character varying NOT NULL,
  time_first timestamp without time zone NOT NULL,
  calls integer NOT NULL,
  type character varying NOT NULL,
  mostwatchedcontent character varying NOT NULL,
  totaltime integer NOT NULL,
  mostwatchedmateches integer NOT NULL,
  CONSTRAINT userspkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE users
  OWNER TO postgres;


CREATE INDEX users_indx1
  ON users
  USING btree
  (type);

CREATE INDEX users_indx2
  ON users
  USING btree
  (ipaddress);
  
GRANT ALL PRIVILEGES ON TABLE users TO w205;