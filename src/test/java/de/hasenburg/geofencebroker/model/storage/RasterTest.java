package de.hasenburg.geofencebroker.model.storage;

import de.hasenburg.geofencebroker.model.spatial.Geofence;
import de.hasenburg.geofencebroker.model.spatial.Location;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.exception.InvalidShapeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class RasterTest {

	private static final Logger logger = LogManager.getLogger();

	Method privateMethod_calculateIndexLocation;
	Method privateMethod_calculateIndexLocations;
	Raster raster;

	@Before
	public void setUpTest() throws NoSuchMethodException {
		privateMethod_calculateIndexLocation = Raster.class.getDeclaredMethod("calculateIndexLocation", Location.class);
		privateMethod_calculateIndexLocations = Raster.class.getDeclaredMethod("calculateIndexLocations", Geofence.class);

		privateMethod_calculateIndexLocation.setAccessible(true);
		privateMethod_calculateIndexLocations.setAccessible(true);
	}

	@After
	public void tearDownTest() {
		privateMethod_calculateIndexLocation = null;
		privateMethod_calculateIndexLocations = null;
		raster = null;
	}

	public Location invokeCalculateIndexLocation(Location location) {
		try {
			return (Location) privateMethod_calculateIndexLocation.invoke(raster, location);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			fail("Could not invoke private method");
		}
		// Stupid, I never get here, why do I need to return something?
		return null;
	}

	public List<RasterEntry> invokeCalculateIndexLocations(Geofence geofence) {
		try {
			return (List<RasterEntry>) privateMethod_calculateIndexLocations.invoke(raster, geofence);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
			fail("Could not invoke private method");
		}
		// Stupid, I never get here, why do I need to return something?
		return null;
	}

	@Test(expected = InvalidShapeException.class)
	public void testCalculateIndexGranularity1() {
		raster = new Raster(1);
		Location calculatedIndex;

		// even
		calculatedIndex = invokeCalculateIndexLocation(new Location(10, -10));
		assertEquals(new Location(10, -10), calculatedIndex);

		// many fractions
		calculatedIndex = invokeCalculateIndexLocation(new Location(10.198, -11.198));
		assertEquals(new Location(10, -12), calculatedIndex);

		// exact boundary
		calculatedIndex = invokeCalculateIndexLocation(new Location(90, -180));
		assertEquals(new Location(90, -180), calculatedIndex);

		// out of bounds, expect throw
		invokeCalculateIndexLocation(new Location(91, -181));
	}

	@Test(expected = InvalidShapeException.class)
	public void testCalculateIndexGranularity10() {

		raster = new Raster(10);
		Location calculatedIndex;

		// even
		calculatedIndex = invokeCalculateIndexLocation(new Location(10, -10));
		assertEquals(new Location(10, -10), calculatedIndex);

		// many fractions
		calculatedIndex = invokeCalculateIndexLocation(new Location(10.198, -11.198));
		assertEquals(new Location(10.1, -11.2), calculatedIndex);

		// exact boundary
		calculatedIndex = invokeCalculateIndexLocation(new Location(90, -180));
		assertEquals(new Location(90, -180), calculatedIndex);

		// out of bounds, expect throw
		invokeCalculateIndexLocation(new Location(91, -181));
	}

	@Test(expected = InvalidShapeException.class)
	public void testCalculateIndexGranularity100() {

		raster = new Raster(100);
		Location calculatedIndex;

		// even
		calculatedIndex = invokeCalculateIndexLocation(new Location(10, -10));
		assertEquals(new Location(10, -10), calculatedIndex);

		// many fractions
		calculatedIndex = invokeCalculateIndexLocation(new Location(10.198, -11.198));
		assertEquals(new Location(10.19, -11.2), calculatedIndex);

		// exact boundary
		calculatedIndex = invokeCalculateIndexLocation(new Location(90, -180));
		assertEquals(new Location(90, -180), calculatedIndex);

		// out of bounds, expect throw
		invokeCalculateIndexLocation(new Location(91, -181));
	}

	@Test
	public void testCalculateIndexLocationsForGeofenceRectangle() {
		raster = new Raster(1);
		Geofence fence = Geofence.polygon(Arrays.asList(
				new Location(-0.5, -0.5),
				new Location(-0.5, 1.5),
				new Location(1.5, 1.5),
				new Location(1.5, -0.5)
		));

		List<RasterEntry> result = invokeCalculateIndexLocations(fence);
		assertEquals(9, result.size());
		assertTrue(containsLocation(result, new Location(-1, -1)));
		assertTrue(containsLocation(result, new Location(-1, 0)));
		assertTrue(containsLocation(result, new Location(-1, 1)));
		assertTrue(containsLocation(result, new Location(0, -1)));
		assertTrue(containsLocation(result, new Location(0, 0)));
		assertTrue(containsLocation(result, new Location(0, 1)));
		assertTrue(containsLocation(result, new Location(1, -1)));
		assertTrue(containsLocation(result, new Location(1, 0)));
		assertTrue(containsLocation(result, new Location(1, 1)));
	}

	@Test
	public void testCalculateIndexLocationsForGeofenceTriangle() {
		raster = new Raster(1);
		Geofence fence = Geofence.polygon(Arrays.asList(
				new Location(-0.5, -1.5),
				new Location(-0.5, 0.7),
				new Location(1.7, -1.5)
		));

		List<RasterEntry> result = invokeCalculateIndexLocations(fence);
		assertEquals(8, result.size());
		assertTrue(containsLocation(result, new Location(-1, -2)));
		assertTrue(containsLocation(result, new Location(-1, -1)));
		assertTrue(containsLocation(result, new Location(-1, 0)));
		assertTrue(containsLocation(result, new Location(0, -2)));
		assertTrue(containsLocation(result, new Location(0, -1)));
		assertTrue(containsLocation(result, new Location(0, 0)));
		assertTrue(containsLocation(result, new Location(1, -2)));
		assertTrue(containsLocation(result, new Location(1, -1)));
	}

	@Test
	public void testCalculateIndexLocationsForGeofenceCircle() {
		raster = new Raster(1);
		Geofence fence = Geofence.circle(new Location(0.5, 0), 1.1);

		List<RasterEntry> result = invokeCalculateIndexLocations(fence);
		assertEquals(8, result.size());
		assertTrue(containsLocation(result, new Location(-1, -1)));
		assertTrue(containsLocation(result, new Location(-1, 0)));
		assertTrue(containsLocation(result, new Location(0, -2)));
		assertTrue(containsLocation(result, new Location(0, -1)));
		assertTrue(containsLocation(result, new Location(0, 0)));
		assertTrue(containsLocation(result, new Location(0, 1)));
		assertTrue(containsLocation(result, new Location(1, -1)));
		assertTrue(containsLocation(result, new Location(1, 0)));
	}

	private boolean containsLocation(List<RasterEntry> result, Location l) {
		for (RasterEntry rasterEntry : result) {
			if (rasterEntry.getIndex().equals(l)) {
				return true;
			}
		}

		return  false;
	}

}