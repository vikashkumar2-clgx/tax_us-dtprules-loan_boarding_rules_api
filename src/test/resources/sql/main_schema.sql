DROP TABLE  IF EXISTS DROOLS;

CREATE TABLE DROOLS
(
    id uuid NOT NULL,
    client_id VARCHAR(10) NOT NULL,
    rule_id uuid NOT NULL,
    file_name VARCHAR(200) NOT NULL,
    file_content bytea NOT NULL,
    last_updated_ts timestamp without time zone NOT NULL,
    is_enabled boolean DEFAULT true,
    CONSTRAINT drools_pkey PRIMARY KEY (id),
    CONSTRAINT unique_rule_id_constraint UNIQUE (rule_id),
    CONSTRAINT unique_file_name_constraint UNIQUE (file_name)
)
