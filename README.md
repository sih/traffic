Traffic
=======

Hobby project to parse and process public data from the UK Highways Agency on the road traffic network.

This pulls data from two feeds, the first enumerates a set of pre-defined segments of the road network and gives them ids; the second is a measure of the expected and actual travel times across these segments.

The data is stored in a Cassandra table and a daily feed populates each of the time-series for the segments.

The project uses the Datastax CQL 2.0 Java driver, the StAX 1.0.1 API, and JUnit 4.8.1, Mockito 1.9.5 in test.

There are (real-life) sample data files in the ./data directory as well as CQL to create the table.

Run using mvn clean install
