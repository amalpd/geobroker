package de.hasenburg.geolife;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeolifeDatasetHelper {

	private static final Logger logger = LogManager.getLogger();

	private AmazonS3 s3;
	private Map<Integer, String> indices = new HashMap<>();

	public GeolifeDatasetHelper() {
		s3 = AmazonS3ClientBuilder.defaultClient();
	}

	public void prepare() {
		try {
			// read in indices file
			S3Object o = s3.getObject(ClientConfiguration.S3_BUCKET_NAME, keyIndices());
			BufferedReader reader = new BufferedReader(new InputStreamReader(o.getObjectContent()));
			String line = reader.readLine();

			while (line != null && !line.equals("")) {
				String[] elements = line.split(";");
				indices.put(Integer.parseInt(elements[0]), elements[1]);
				line = reader.readLine();
			}

			if (indices.size() != 18670) {
				logger.fatal("Indices file did not contain the correct amount of elements, was " + indices.size());
			} else {
				logger.debug("Successfully read the 18670 geolife dataset index entries");
			}

		} catch (IOException | NullPointerException e) {
			logger.fatal("Could not read indices file", e);
			System.exit(1);
		}
	}

	public Map<Integer, Route> downloadRequiredFiles(int index, int count) {
		Map<Integer, Route> routes = new HashMap<>();
		for (int i = index; i <= count; i++) {
			logger.trace("Creating route for file at index {}", i);
			List<String> lines = getLinesOfRouteFileAtIndex(i);
			routes.put(i, Route.createRoute(lines));
		}
		if (routes.size() == 0) {
			logger.fatal("No routes were created");
			System.exit(1);
		}

		logger.debug("Successfully created {} routes", routes.size());
		return routes;
	}

	private List<String> getLinesOfRouteFileAtIndex(int index) {
		List<String> lines = new ArrayList<>();

		try {
			// read in route file
			S3Object o = s3.getObject(ClientConfiguration.S3_BUCKET_NAME, keyRoute(indices.get(index)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(o.getObjectContent()));
			String line = reader.readLine();

			while (line != null && !line.equals("")) {
				lines.add(line);
				line = reader.readLine();
			}
		} catch (IOException | NullPointerException e) {
			logger.fatal("Could not read route file at index {}", index, e);
			System.exit(1);
		}

		return lines;
	}

	/*****************************************************************
	 * S3 Keymanager
	 ****************************************************************/

	private String keyIndices() {
		return ClientConfiguration.S3_GEOLIFE_FOLDER + "indices.csv";
	}

	private String keyRoute(String relativePath) {
		return ClientConfiguration.S3_GEOLIFE_FOLDER + relativePath;
	}

	public static void main(String[] args) {
		GeolifeDatasetHelper gdh = new GeolifeDatasetHelper();
		gdh.prepare();
		logger.info("File at index 10 is {}", gdh.indices.get(10));
		logger.info(gdh.getLinesOfRouteFileAtIndex(10));
	}

}
