CREATE KEYSPACE IF NOT EXISTS traffic WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 1};

CREATE TABLE 
	traffic.locations (
	location_id text, 
	name text, 
	direction text, 
	location_type text,
	to_latitude text, 
	to_longitude text,
	to_first_loc text,
	to_second_loc text,
	to_description text,
	from_latitude text, 
	from_longitude text, 
	from_first_loc text,
	from_second_loc text,
	from_description text,
	primary key (location_id)
);