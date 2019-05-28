package de.hasenburg.geobroker.commons.model.spatial;

import de.hasenburg.geobroker.commons.model.JSONable;
import de.hasenburg.geobroker.commons.model.KryoSerializer;
import de.hasenburg.geobroker.commons.model.spatial.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LocationTest {

	private static final Logger logger = LogManager.getLogger();

	private Location location;
	private final int N = 10000;

	@Before
	public void setUp() {
		location = Location.random();
		assertNotNull(location);
	}

	@Test
	public void equalsAndRandom() {
		assertEquals(location, location);
		assertNotEquals(location, Location.random());
	}

	@Test
	public void toAndFromJson() {
		String json = JSONable.toJSON(location);
		logger.info("Location JSON: {}", json);
		assertEquals(location, JSONable.fromJSON(json, Location.class).orElse(null));
	}

	@Test
	public void toAndFromJsonN() {
		long time = System.currentTimeMillis();
		for (int i = 0; i < N; i++) {
			assertEquals(location, JSONable.fromJSON(JSONable.toJSON(location), Location.class).orElse(null));
		}
		logger.info("Created and read {} JSONs in {}ms", N, System.currentTimeMillis() - time);
	}

	@Test
	public void toAndFromByte() {
		KryoSerializer kryo = new KryoSerializer();
		// point set
		byte[] bytes = kryo.write(location);
		assertEquals(location, kryo.read(bytes, Location.class));

		// undefined
		bytes = kryo.write(Location.undefined());
		assertEquals(Location.undefined(), kryo.read(bytes, Location.class));
	}

	@Test
	public void undefinedLocation() {
		Location l = Location.random();
		Location ul1 = Location.undefined();

		assertEquals(-1.0, l.distanceKmTo(ul1), 0.1);

		String json = JSONable.toJSON(ul1);
		logger.info(json);
		Location ul2 = JSONable.fromJSON(json, Location.class).get();
		assertEquals(ul1, ul2);
	}

	@Test
	public void testDistance() {
		Location location = new Location(40.0, 40.0);
		Location location2 = new Location(35.0, 35.0);
		logger.info(location.distanceKmTo(location2));
		assertTrue(location.distanceKmTo(location2) < 710.000);
		assertTrue(location.distanceKmTo(location2) > 700.000);
	}

	@Test
	public void testDistanceBerlinHamburg() {
		Location berlin = new Location(52.5200, 13.4050);
		Location hamburg = new Location(53.511, 9.9937);
		logger.info(berlin.distanceKmTo(hamburg));
		assertEquals(253.375, berlin.distanceKmTo(hamburg), 0.5);
	}

	@Test
	public void testRandomInGeofence() {
		Geofence geofence = Geofence.circle(new Location(30, 30), 3.0);
		Location l = Location.randomInGeofence(geofence);
		assertTrue(geofence.contains(l));
	}
}