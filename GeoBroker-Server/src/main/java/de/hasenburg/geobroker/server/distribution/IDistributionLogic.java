package de.hasenburg.geobroker.server.distribution;

import de.hasenburg.geobroker.commons.model.KryoSerializer;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMsg;

/**
 * Different Distribution logic, for example, could check or discard acknowledgements.
 */
public interface IDistributionLogic {

	void sendMessageToOtherBrokers(ZMsg msg, Socket broker, String targetBrokerId, KryoSerializer kryo);

	void processOtherBrokerAcknowledgement(ZMsg msg, String otherBrokerId, KryoSerializer kryo);

}
