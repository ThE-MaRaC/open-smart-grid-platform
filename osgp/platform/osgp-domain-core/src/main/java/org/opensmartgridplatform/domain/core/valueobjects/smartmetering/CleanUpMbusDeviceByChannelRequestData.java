/*
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.domain.core.valueobjects.smartmetering;

import org.opensmartgridplatform.domain.core.valueobjects.DeviceFunction;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;

public class CleanUpMbusDeviceByChannelRequestData implements ActionRequest {

    private static final long serialVersionUID = 1522902244442651253L;
    private short channel;

    public CleanUpMbusDeviceByChannelRequestData(final short channel) {
        this.channel = channel;
    }

    public short getChannel() {
        return this.channel;
    }

    @Override
    public void validate() throws FunctionalException {
        // nothing to validate
    }

    @Override
    public DeviceFunction getDeviceFunction() {
        return DeviceFunction.CLEAN_UP_MBUS_DEVICE_BY_CHANNEL;
    }

}
