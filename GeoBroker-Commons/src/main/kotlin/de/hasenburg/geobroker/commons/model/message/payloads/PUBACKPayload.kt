package de.hasenburg.geobroker.commons.model.message.payloads

import de.hasenburg.geobroker.commons.model.message.ReasonCode

data class PUBACKPayload(val reasonCode: ReasonCode, val numberOfLocalSubscribers: Int,
                         val numberOfAffectedBrokers: Int) : AbstractPayload()
