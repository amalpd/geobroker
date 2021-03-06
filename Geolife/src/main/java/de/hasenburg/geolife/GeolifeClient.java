package de.hasenburg.geolife;

import de.hasenburg.geobroker.commons.model.message.*;
import de.hasenburg.geobroker.commons.model.message.payloads.*;
import de.hasenburg.geobroker.commons.communication.ZMQProcessManager;
import de.hasenburg.geobroker.client.main.BenchmarkClient;
import de.hasenburg.geobroker.commons.Utility;
import de.hasenburg.geobroker.client.communication.InternalClientMessage;
import de.hasenburg.geobroker.commons.model.spatial.Geofence;
import de.hasenburg.geobroker.commons.model.spatial.Location;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeolifeClient extends BenchmarkClient {

	private static final Logger logger = LogManager.getLogger();
	private ClientConfiguration c;
	private Route route;
	private Thread t;

	public GeolifeClient(ClientConfiguration c, int index, Route r, ZMQProcessManager processManager) {
		super(c.getManagerName() + "-" + index, c.getAddress(), c.getPort(), processManager);
		this.route = r;
		this.c = c;
	}

	public void start() {
		logger.debug("Started geolife client {}", getIdentity());
		InternalClientMessage clientMessage = new InternalClientMessage(ControlPacketType.CONNECT,
																		new CONNECTPayload(route
																								   .getVisitedLocations()
																								   .get(0).left));
		sendInternalClientMessage(clientMessage);
		t = new Thread(() -> {
			Topic topic = new Topic("data");
			String data = Utility.generatePayloadWithSize(c.getPayloadSize(), getIdentity());

			while (!t.isInterrupted()) {
				logger.info("(Re-)starting route");
				for (ImmutablePair<Location, Integer> visitedLocation : route.getVisitedLocations()) {
					logger.debug("Driving to location {} in {} seconds", visitedLocation.left, visitedLocation.right);
					try {
						Thread.sleep(visitedLocation.right * 1000);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
					logger.trace("Arrived at location, transmitting data");
					Geofence fence = Geofence.circle(visitedLocation.left, c.getGeofenceSize());
					InternalClientMessage pingreq = new InternalClientMessage(ControlPacketType.PINGREQ,
																			  new PINGREQPayload(visitedLocation.left));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// finished
						Thread.currentThread().interrupt();
						break;
					}
					sendInternalClientMessage(pingreq);
					InternalClientMessage subscribe = new InternalClientMessage(ControlPacketType.SUBSCRIBE,
																				new SUBSCRIBEPayload(topic, fence));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// finished
						Thread.currentThread().interrupt();
						break;
					}
					sendInternalClientMessage(subscribe);
					InternalClientMessage publish = new InternalClientMessage(ControlPacketType.PUBLISH,
																			  new PUBLISHPayload(topic, fence, data));
					sendInternalClientMessage(publish);
				}
			}
		}); t.setName(getIdentity());
		t.start();
	}

	public void stop() {
		logger.info("Stopping GeolifeClient {}", getIdentity());
		t.interrupt();
		InternalClientMessage clientMessage = new InternalClientMessage(ControlPacketType.DISCONNECT,
																		new DISCONNECTPayload(ReasonCode.NormalDisconnection));
		sendInternalClientMessage(clientMessage);
		tearDownClient();
	}

	public static void main (String[] args) {
		ClientConfiguration con = new ClientConfiguration();
		ZMQProcessManager processManager = new ZMQProcessManager();
	    for (int i = 0; i < 500; i++) {
	    	GeolifeClient c = new GeolifeClient(con, i, null, processManager);
		}
		Utility.sleepNoLog(2000, 0);
	    logger.info("Could initiate clients");
	}

}
