/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects.smartmetering;

public class DeCoupleMbusDeviceResponseDto extends ActionResponseDto {

    private static final long serialVersionUID = -4454979905929290745L;

    private String mbusDeviceIdentification;
    private final ChannelElementValuesDto channelElementValues;

    public DeCoupleMbusDeviceResponseDto(final ChannelElementValuesDto channelElementValues) {
        super("Decouple Mbus Device was successful");
        this.channelElementValues = channelElementValues;
    }

    @Override
    public String toString() {
        return "DeCoupleMbusDeviceResponseDto [channel=" + this.channelElementValues.getChannel()
                + ", mbusDeviceIdentification=" + this.mbusDeviceIdentification + "]";
    }

    public ChannelElementValuesDto getChannelElementValues() {
        return this.channelElementValues;
    }

    public String getMbusDeviceIdentification() {
        return this.mbusDeviceIdentification;
    }

    public void setMbusDeviceIdentification(final String mbusDeviceIdentification) {
        this.mbusDeviceIdentification = mbusDeviceIdentification;
    }
}
