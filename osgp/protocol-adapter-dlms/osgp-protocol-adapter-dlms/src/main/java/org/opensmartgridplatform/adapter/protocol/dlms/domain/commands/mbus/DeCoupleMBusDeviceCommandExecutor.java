/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.mbus;

import org.openmuc.jdlms.ObisCode;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.AbstractCommandExecutor;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.utils.CosemObjectAccessor;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.Protocol;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.opensmartgridplatform.dlms.interfaceclass.InterfaceClass;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ChannelElementValuesDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DeCoupleMbusDeviceDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.DeCoupleMbusDeviceResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.MbusChannelElementsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DeCoupleMBusDeviceCommandExecutor
        extends AbstractCommandExecutor<DeCoupleMbusDeviceDto, DeCoupleMbusDeviceResponseDto> {

    @Autowired
    private DeviceChannelsHelper deviceChannelsHelper;

    public DeCoupleMBusDeviceCommandExecutor() {
        super(DeCoupleMbusDeviceDto.class);
    }

    @Override
    public DeCoupleMbusDeviceResponseDto execute(final DlmsConnectionManager conn, final DlmsDevice device,
            final DeCoupleMbusDeviceDto decoupleMbusDto) throws ProtocolAdapterException {

        Short channel = decoupleMbusDto.getChannel();
        String mBusDeviceIdentification = decoupleMbusDto.getmBusDeviceIdentification();
        log.debug("DeCouple mbus device from gateway device");

        final ObisCode obisCode = this.deviceChannelsHelper.getObisCode(channel);
        
        final CosemObjectAccessor mBusSetup = new CosemObjectAccessor(conn, obisCode, InterfaceClass.MBUS_CLIENT.id());

        this.deviceChannelsHelper.deinstallSlave(conn, device, channel, mBusDeviceIdentification, mBusSetup);

        this.writeUpdatedMbus(conn, device, channel);
        
        return new DeCoupleMbusDeviceResponseDto(mBusDeviceIdentification,
                channel);

    }

    private ChannelElementValuesDto writeUpdatedMbus(final DlmsConnectionManager conn,
            final DlmsDevice device, Short channel) throws ProtocolAdapterException {

        MbusChannelElementsDto mbusChannelElementsDto = new MbusChannelElementsDto((short)0, "", "", "", (short)0, (short)0);
        return this.deviceChannelsHelper.writeUpdatedMbus(conn,
                mbusChannelElementsDto, channel, Protocol.forDevice(device), "DeCoupleMbusDevice");

    }

}
