package de.hasenburg.geobroker.commons.model;

import com.esotericsoftware.kryo.io.Input;
import de.hasenburg.geobroker.commons.model.message.KryoSerializer;
import de.hasenburg.geobroker.commons.model.message.payloads.*;

public interface KryoInterface {

    static CONNACKPayload readCONNACK(KryoSerializer kryo, Input input) {
        return (CONNACKPayload) kryo.read(input);
    }

    static CONNECTPayload readCONNECT(KryoSerializer kryo, Input input) {
        return (CONNECTPayload) kryo.read(input);
    }

    static DISCONNECTPayload readDISCONNECT(KryoSerializer kryo, Input input) {
        return (DISCONNECTPayload) kryo.read(input);
    }

    static PINGREQPayload readPINGREQ(KryoSerializer kryo, Input input) {
        return (PINGREQPayload) kryo.read(input);
    }

    static PINGRESPPayload readPINGRESP(KryoSerializer kryo, Input input) {
        return (PINGRESPPayload) kryo.read(input);
    }

    static PUBACKPayload readPUBACK(KryoSerializer kryo, Input input) {
        return (PUBACKPayload) kryo.read(input);
    }

    static PUBLISHPayload readPUBLISH(KryoSerializer kryo, Input input) {
        return (PUBLISHPayload) kryo.read(input);
    }

    static SUBACKPayload readSUBACK(KryoSerializer kryo, Input input) {
        return (SUBACKPayload) kryo.read(input);
    }

    static SUBSCRIBEPayload readSUBSCRIBE(KryoSerializer kryo, Input input) {
        return (SUBSCRIBEPayload) kryo.read(input);
    }

    static UNSUBACKPayload readUNSUBACK(KryoSerializer kryo, Input input) {
        return (UNSUBACKPayload) kryo.read(input);
    }

    static UNSUBSCRIBEPayload readUNSUBSCRIBE(KryoSerializer kryo, Input input) {
        return (UNSUBSCRIBEPayload) kryo.read(input);
    }

    static BrokerForwardDisconnectPayload readBrokerForwardDisconnect(KryoSerializer kryo, Input input) {
        return (BrokerForwardDisconnectPayload) kryo.read(input);
    }

    static BrokerForwardPingreqPayload readBrokerForwardPingreq(KryoSerializer kryo, Input input) {
        return (BrokerForwardPingreqPayload) kryo.read(input);
    }

    static BrokerForwardPublishPayload readBrokerForwardPublish(KryoSerializer kryo, Input input) {
        return (BrokerForwardPublishPayload) kryo.read(input);
    }

    static BrokerForwardSubscribePayload readBrokerForwardSubscribe(KryoSerializer kryo, Input input) {
        return (BrokerForwardSubscribePayload) kryo.read(input);
    }

    static BrokerForwardUnsubscribePayload readBrokerForwardUnsubscribe(KryoSerializer kryo, Input input) {
        return (BrokerForwardUnsubscribePayload) kryo.read(input);
    }

}
