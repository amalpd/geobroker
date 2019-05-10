package de.hasenburg.geobroker.commons.model.message.payloads;

import java.util.Objects;

@SuppressWarnings("WeakerAccess") // as fields are checked by AbstractPayload.nullField()
public class BrokerForwardSubscribePayload extends AbstractPayload {

	PINGREQPayload pingreqPayload;

	public BrokerForwardSubscribePayload() {

	}

	public BrokerForwardSubscribePayload(PINGREQPayload pingreqPayload) {
		super();
		this.pingreqPayload = pingreqPayload;
	}

	/*****************************************************************
	 * Getter & Setter
	 ****************************************************************/

	public PINGREQPayload getPingreqPayload() {
		return pingreqPayload;
	}

	/*****************************************************************
	 * Generated Code
	 ****************************************************************/

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BrokerForwardSubscribePayload that = (BrokerForwardSubscribePayload) o;
		return Objects.equals(getPingreqPayload(), that.getPingreqPayload());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPingreqPayload());
	}
}