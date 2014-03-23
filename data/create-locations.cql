CREATE KEYSPACE IF NOT EXISTS traffic WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE 
	traffic.locations (
	k_location_id text, 
	k_publication_ts timestamp,
	name text, 
	direction text, 
	location_type text,
	to_latitude text, 
	to_longitude text,
	to_first_loc text,
	to_second_loc text,
	from_latitude text, 
	from_longitude text, 
	from_first_loc text,
	from_second_loc text,
	travel_time decimal,
	freeflow_time decimal,
	normal_time decimal,
	primary key (k_location_id, k_publication_ts)
);