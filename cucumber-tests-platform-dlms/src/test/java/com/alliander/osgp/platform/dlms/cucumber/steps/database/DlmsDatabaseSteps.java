/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.dlms.cucumber.steps.database;

import org.osgp.adapter.protocol.dlms.domain.repositories.DlmsDeviceRepository;
import org.osgp.adapter.protocol.dlms.domain.repositories.DlmsSecurityKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.adapter.ws.smartmetering.domain.repositories.MeterResponseDataRepository;
<<<<<<< HEAD
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.ScheduledTaskRepository;
import com.alliander.osgp.domain.core.repositories.SmartMeterRepository;
import com.alliander.osgp.logging.domain.repositories.DeviceLogItemRepository;

=======
>>>>>>> 9e04c9442481152415dc74dccba037259b711b6e

/**
 * DLMS related database steps.
 */
@Component
public class DlmsDatabaseSteps {

    @Autowired
    private DlmsDeviceRepository dlmsDeviceRepo;

    @Autowired
    private DlmsSecurityKeyRepository dlmsDSecurityKeyRepo;

    @Autowired
    private MeterResponseDataRepository meterResponseDataRepo;

<<<<<<< HEAD
    @Autowired
    private DeviceLogItemRepository deviceLogItemRepository;

    @Autowired
    private ScheduledTaskRepository scheduledTaskRepository;
    
=======
>>>>>>> 9e04c9442481152415dc74dccba037259b711b6e
    /**
     * Before each scenario dlms related stuff needs to be removed.
     */
    @Transactional(transactionManager = "txMgrCore")
    public void prepareDatabaseForScenario() {
<<<<<<< HEAD
        this.deviceAuthorization.deleteAllInBatch();
        
=======
>>>>>>> 9e04c9442481152415dc74dccba037259b711b6e
        this.dlmsDSecurityKeyRepo.deleteAllInBatch();
        this.dlmsDeviceRepo.deleteAllInBatch();
        this.meterResponseDataRepo.deleteAllInBatch();

        this.insertDefaultData();
    }

    /**
     * This method is used to create default data not directly related to the
     * specific tests. For example: A default dlms gateway device.
     */
    private void insertDefaultData() {
        // TODO insert here default devices.
    }

}
