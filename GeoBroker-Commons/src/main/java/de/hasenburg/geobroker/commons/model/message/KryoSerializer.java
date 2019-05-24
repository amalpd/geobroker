package de.hasenburg.geobroker.commons.model.message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import de.hasenburg.geobroker.commons.model.BrokerInfo;
import de.hasenburg.geobroker.commons.model.message.payloads.*;
import de.hasenburg.geobroker.commons.model.spatial.Geofence;
import de.hasenburg.geobroker.commons.model.spatial.Location;

public class KryoSerializer {
	private Kryo kryo = new Kryo();

	public KryoSerializer() {
		kryo.register(BrokerInfo.class, new Serializer<BrokerInfo>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerInfo o) {
				kryo.writeClassAndObject(output, o.getBrokerId());
				kryo.writeClassAndObject(output, o.getIp());
				kryo.writeClassAndObject(output, o.getPort());
			}

			@Override
			public BrokerInfo read(Kryo kryo, Input input, Class<BrokerInfo> aClass) {
				String brokerId = (String) kryo.readClassAndObject(input);
				String ip = (String) kryo.readClassAndObject(input);
				int port = (int) kryo.readClassAndObject(input);
				return new BrokerInfo(brokerId, ip, port);
			}
		});
		kryo.register(Location.class, new Serializer<Location>() {
			@Override
			public void write(Kryo kryo, Output output, Location o) {
				kryo.writeClassAndObject(output, o.getLat());
				kryo.writeClassAndObject(output, o.getLon());
			}

			@Override
			public Location read(Kryo kryo, Input input, Class<Location> aClass) {
				double lat = (double) kryo.readClassAndObject(input);
				double lon = (double) kryo.readClassAndObject(input);
				return new Location(lat, lon);
			}
		});
		kryo.register(Geofence.class, new Serializer<Geofence>() {
			@Override
			public void write(Kryo kryo, Output output, Geofence o) {
				kryo.writeClassAndObject(output, o.getWKTString());
			}

			@Override
			public Geofence read(Kryo kryo, Input input, Class<Geofence> aClass) {
				try {
					return new Geofence((String) kryo.readClassAndObject(input));
				} catch (Exception ex) {
					return null;
				}
			}
		});
		kryo.register(Topic.class, new Serializer<Topic>() {
			@Override
			public void write(Kryo kryo, Output output, Topic o) {
				kryo.writeClassAndObject(output, o.getTopic());
			}

			@Override
			public Topic read(Kryo kryo, Input input, Class<Topic> aClass) {
				return new Topic((String) kryo.readClassAndObject(input));
			}
		});
		kryo.register(BrokerForwardDisconnectPayload.class, new Serializer<BrokerForwardDisconnectPayload>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerForwardDisconnectPayload o) {
				kryo.writeClassAndObject(output, o.getClientIdentifier());
				kryo.writeClassAndObject(output, o.getDisconnectPayload());
			}

			@Override
			public BrokerForwardDisconnectPayload read(Kryo kryo, Input input,
													   Class<BrokerForwardDisconnectPayload> aClass) {
				String clientidentifier = (String) kryo.readClassAndObject(input);
				DISCONNECTPayload disconnectPayload = (DISCONNECTPayload) kryo.readClassAndObject(input);
				return new BrokerForwardDisconnectPayload(clientidentifier, disconnectPayload);
			}
		});
		kryo.register(BrokerForwardPingreqPayload.class, new Serializer<BrokerForwardPingreqPayload>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerForwardPingreqPayload o) {
				kryo.writeClassAndObject(output, o.getClientIdentifier());
				kryo.writeClassAndObject(output, o.getPingreqPayload());
			}

			@Override
			public BrokerForwardPingreqPayload read(Kryo kryo, Input input, Class<BrokerForwardPingreqPayload> aClass) {
				String clientidentifier = (String) kryo.readClassAndObject(input);
				PINGREQPayload pingreqPayload = (PINGREQPayload) kryo.readClassAndObject(input);
				return new BrokerForwardPingreqPayload(clientidentifier, pingreqPayload);
			}
		});
		kryo.register(BrokerForwardPublishPayload.class, new Serializer<BrokerForwardPublishPayload>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerForwardPublishPayload o) {
				kryo.writeClassAndObject(output, o.getPublishPayload());
				kryo.writeClassAndObject(output, o.getPublisherLocation());
				kryo.writeClassAndObject(output, o.getSubscriberClientIdentifier());
			}

			@Override
			public BrokerForwardPublishPayload read(Kryo kryo, Input input, Class<BrokerForwardPublishPayload> aClass) {
				PUBLISHPayload publishPayload = (PUBLISHPayload) kryo.readClassAndObject(input);
				Location location = (Location) kryo.readClassAndObject(input);
				String subscriberClientIdentifier = (String) kryo.readClassAndObject(input);
				return new BrokerForwardPublishPayload(publishPayload, subscriberClientIdentifier, location);
			}
		});
		kryo.register(BrokerForwardSubscribePayload.class, new Serializer<BrokerForwardSubscribePayload>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerForwardSubscribePayload o) {
				kryo.writeClassAndObject(output, o.getClientIdentifier());
				kryo.writeClassAndObject(output, o.getSubscribePayload());
			}

			@Override
			public BrokerForwardSubscribePayload read(Kryo kryo, Input input,
													  Class<BrokerForwardSubscribePayload> aClass) {
				String clientidentifier = (String) kryo.readClassAndObject(input);
				SUBSCRIBEPayload subscribePayload = (SUBSCRIBEPayload) kryo.readClassAndObject(input);
				return new BrokerForwardSubscribePayload(clientidentifier, subscribePayload);
			}
		});
		kryo.register(BrokerForwardUnsubscribePayload.class, new Serializer<BrokerForwardUnsubscribePayload>() {
			@Override
			public void write(Kryo kryo, Output output, BrokerForwardUnsubscribePayload o) {
				kryo.writeClassAndObject(output, o.getClientIdentifier());
				kryo.writeClassAndObject(output, o.getUnsubscribePayload());
			}

			@Override
			public BrokerForwardUnsubscribePayload read(Kryo kryo, Input input,
														Class<BrokerForwardUnsubscribePayload> aClass) {
				String clientidentifier = (String) kryo.readClassAndObject(input);
				UNSUBSCRIBEPayload unsubscribePayload = (UNSUBSCRIBEPayload) kryo.readClassAndObject(input);
				return new BrokerForwardUnsubscribePayload(clientidentifier, unsubscribePayload);
			}
		});
		kryo.register(CONNACKPayload.class, new Serializer<CONNACKPayload>() {
			@Override
			public void write(Kryo kryo, Output output, CONNACKPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
			}

			@Override
			public CONNACKPayload read(Kryo kryo, Input input, Class<CONNACKPayload> aClass) {
				return new CONNACKPayload((ReasonCode) kryo.readClassAndObject(input));
			}
		});
		kryo.register(CONNECTPayload.class, new Serializer<CONNECTPayload>() {
			@Override
			public void write(Kryo kryo, Output output, CONNECTPayload o) {
				kryo.writeClassAndObject(output, o.getLocation());
			}

			@Override
			public CONNECTPayload read(Kryo kryo, Input input, Class<CONNECTPayload> aClass) {
				return new CONNECTPayload((Location) kryo.readClassAndObject(input));
			}
		});
		kryo.register(DISCONNECTPayload.class, new Serializer<DISCONNECTPayload>() {
			@Override
			public void write(Kryo kryo, Output output, DISCONNECTPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
				kryo.writeClassAndObject(output, o.getBrokerInfo());
			}

			@Override
			public DISCONNECTPayload read(Kryo kryo, Input input, Class<DISCONNECTPayload> aClass) {
				ReasonCode reasonCode = (ReasonCode) kryo.readClassAndObject(input);
				BrokerInfo brokerInfo = (BrokerInfo) kryo.readClassAndObject(input);
				return new DISCONNECTPayload(reasonCode, brokerInfo);
			}
		});
		kryo.register(PINGREQPayload.class, new Serializer<PINGREQPayload>() {
			@Override
			public void write(Kryo kryo, Output output, PINGREQPayload o) {
				kryo.writeClassAndObject(output, o.getLocation());
			}

			@Override
			public PINGREQPayload read(Kryo kryo, Input input, Class<PINGREQPayload> aClass) {
				Location location = (Location) kryo.readClassAndObject(input);
				return new PINGREQPayload(location);
			}
		});
		kryo.register(PINGRESPPayload.class, new Serializer<PINGRESPPayload>() {
			@Override
			public void write(Kryo kryo, Output output, PINGRESPPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
			}

			@Override
			public PINGRESPPayload read(Kryo kryo, Input input, Class<PINGRESPPayload> aClass) {
				ReasonCode reasonCode = (ReasonCode) kryo.readClassAndObject(input);
				return new PINGRESPPayload(reasonCode);
			}
		});
		kryo.register(PUBACKPayload.class, new Serializer<PUBACKPayload>() {
			@Override
			public void write(Kryo kryo, Output output, PUBACKPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
			}

			@Override
			public PUBACKPayload read(Kryo kryo, Input input, Class<PUBACKPayload> aClass) {
				return new PUBACKPayload((ReasonCode) kryo.readClassAndObject(input));
			}
		});
		kryo.register(PUBLISHPayload.class, new Serializer<PUBLISHPayload>() {
			@Override
			public void write(Kryo kryo, Output output, PUBLISHPayload o) {
				kryo.writeClassAndObject(output, o.getContent());
				kryo.writeClassAndObject(output, o.getGeofence());
				kryo.writeClassAndObject(output, o.getTopic());
			}

			@Override
			public PUBLISHPayload read(Kryo kryo, Input input, Class<PUBLISHPayload> aClass) {
				String content = (String) kryo.readClassAndObject(input);
				Geofence g = (Geofence) kryo.readClassAndObject(input);
				Topic topic = (Topic) kryo.readClassAndObject(input);
				return new PUBLISHPayload(topic, g, content);
			}
		});
		kryo.register(SUBACKPayload.class, new Serializer<SUBACKPayload>() {
			@Override
			public void write(Kryo kryo, Output output, SUBACKPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
			}

			@Override
			public SUBACKPayload read(Kryo kryo, Input input, Class<SUBACKPayload> aClass) {
				ReasonCode reasonCode = (ReasonCode) kryo.readClassAndObject(input);
				return new SUBACKPayload(reasonCode);
			}
		});
		kryo.register(SUBSCRIBEPayload.class, new Serializer<SUBSCRIBEPayload>() {
			@Override
			public void write(Kryo kryo, Output output, SUBSCRIBEPayload o) {
				kryo.writeClassAndObject(output, o.getGeofence());
				kryo.writeClassAndObject(output, o.getTopic());
			}

			@Override
			public SUBSCRIBEPayload read(Kryo kryo, Input input, Class<SUBSCRIBEPayload> aClass) {
				Geofence geofence = (Geofence) kryo.readClassAndObject(input);
				Topic topic = (Topic) kryo.readClassAndObject(input);
				return new SUBSCRIBEPayload(topic, geofence);
			}
		});
		kryo.register(UNSUBACKPayload.class, new Serializer<UNSUBACKPayload>() {
			@Override
			public void write(Kryo kryo, Output output, UNSUBACKPayload o) {
				kryo.writeClassAndObject(output, o.getReasonCode());
			}

			@Override
			public UNSUBACKPayload read(Kryo kryo, Input input, Class<UNSUBACKPayload> aClass) {
				ReasonCode reasonCode = (ReasonCode) kryo.readClassAndObject(input);
				return new UNSUBACKPayload(reasonCode);
			}
		});
		kryo.register(UNSUBSCRIBEPayload.class, new Serializer<UNSUBSCRIBEPayload>() {
			@Override
			public void write(Kryo kryo, Output output, UNSUBSCRIBEPayload o) {
				kryo.writeClassAndObject(output, o.getTopic());
			}

			@Override
			public UNSUBSCRIBEPayload read(Kryo kryo, Input input, Class<UNSUBSCRIBEPayload> aClass) {
				Topic topic = (Topic) kryo.readClassAndObject(input);
				return new UNSUBSCRIBEPayload(topic);
			}
		});

	}

	public void write(Output output, Object o) {
		kryo.writeClassAndObject(output, o);
	}

	public Object read(Input input) {
		return ((Object) kryo.readClassAndObject(input));
	}
}
