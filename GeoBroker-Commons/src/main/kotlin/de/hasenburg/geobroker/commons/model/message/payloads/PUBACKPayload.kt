package de.hasenburg.geobroker.commons.model.message.payloads

import de.hasenburg.geobroker.commons.model.message.ReasonCode

data class PUBACKPayload(val reasonCode: ReasonCode, var numberOfLocalSubscribers: Int,
                         var numberOfAffectedBrokers: Int) : AbstractPayload(){
    constructor(reason: ReasonCode) : this(reason, -1, -1)
}
