/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.domain.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.adapter.domain.smartmetering.application.mapping.ConfigurationMapper;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.core.OsgpCoreRequestMessageSender;
import com.alliander.osgp.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.ActivityCalendar;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.AlarmNotifications;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.GetAdministration;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SetAdministration;
import com.alliander.osgp.dto.valueobjects.smartmetering.SetConfigurationObjectRequest;
import com.alliander.osgp.dto.valueobjects.smartmetering.SpecialDaysRequest;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;
import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.infra.jms.RequestMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessageResultType;

@Service(value = "domainSmartMeteringConfigurationService")
@Transactional(value = "transactionManager")
public class ConfigurationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationService.class);

    @Autowired
    @Qualifier(value = "domainSmartMeteringOutgoingOsgpCoreRequestMessageSender")
    private OsgpCoreRequestMessageSender osgpCoreRequestMessageSender;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Autowired
    private WebServiceResponseMessageSender webServiceResponseMessageSender;

    @Autowired
    private DomainHelperService domainHelperService;

    public ConfigurationService() {
        // Parameterless constructor required for transactions...
    }

    public void requestSpecialDays(
            @Identification final String organisationIdentification,
            @Identification final String deviceIdentification,
            final String correlationUid,
            final com.alliander.osgp.domain.core.valueobjects.smartmetering.SpecialDaysRequest specialDaysRequestValueObject,
            final String messageType) throws FunctionalException {

        LOGGER.info("requestSpecialDays for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        LOGGER.info("Sending request message to core.");

        final SpecialDaysRequest specialDaysRequestDto = this.configurationMapper.map(specialDaysRequestValueObject,
                SpecialDaysRequest.class);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, specialDaysRequestDto), messageType);
    }

    public void setConfigurationObject(
            @Identification final String organisationIdentification,
            @Identification final String deviceIdentification,
            final String correlationUid,
            final com.alliander.osgp.domain.core.valueobjects.smartmetering.SetConfigurationObjectRequest setConfigurationObjectRequestValueObject,
            final String messageType) throws FunctionalException {

        LOGGER.info("setConfigurationObject for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        LOGGER.info("Sending request message to core.");

        final SetConfigurationObjectRequest setConfigurationObjectRequestDto = this.configurationMapper.map(
                setConfigurationObjectRequestValueObject, SetConfigurationObjectRequest.class);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, setConfigurationObjectRequestDto), messageType);
    }

    public void handleSpecialDaysResponse(final String deviceIdentification, final String organisationIdentification,
            final String correlationUid, final String messageType, final ResponseMessageResultType deviceResult,
            final OsgpException exception) {

        LOGGER.info("handleSpecialDaysresponse for MessageType: {}", messageType);

        ResponseMessageResultType result = deviceResult;
        if (exception != null) {
            LOGGER.error("Device Response not ok. Unexpected Exception", exception);
            result = ResponseMessageResultType.NOT_OK;
        }

        this.webServiceResponseMessageSender.send(new ResponseMessage(correlationUid, organisationIdentification,
                deviceIdentification, result, exception, null), messageType);
    }

    public void setAlarmNotifications(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, final String correlationUid,
            final AlarmNotifications alarmNotifications, final String messageType) throws FunctionalException {

        LOGGER.info("setAlarmNotifications for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        // TODO: bypassing authorization, this should be fixed.
        // Organisation organisation =
        // this.findOrganisation(organisationIdentification);
        // final Device device = this.findActiveDevice(deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        final com.alliander.osgp.dto.valueobjects.smartmetering.AlarmNotifications alarmNotificationsDto = this.configurationMapper
                .map(alarmNotifications, com.alliander.osgp.dto.valueobjects.smartmetering.AlarmNotifications.class);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, alarmNotificationsDto), messageType);
    }

    public void setAdministration(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, final String correlationUid,
            final SetAdministration setAdministration, final String messageType) throws FunctionalException {

        LOGGER.info("SetAdministration for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, setAdministration), messageType);
    }

    public void getAdministration(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, final String correlationUid,
            final GetAdministration getAdministration, final String messageType) throws FunctionalException {

        LOGGER.info("SetAdministration for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, getAdministration), messageType);
    }
    
        public void setActivityCalendar(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification, final String correlationUid,
            final ActivityCalendar activityCalendar, final String messageType) throws FunctionalException {

        LOGGER.info("set Activity Calendar for organisationIdentification: {} for deviceIdentification: {}",
                organisationIdentification, deviceIdentification);

        // TODO: bypassing authorization, this should be fixed.
        // Organisation organisation =
        // this.findOrganisation(organisationIdentification);
        // final Device device = this.findActiveDevice(deviceIdentification);

        this.domainHelperService.ensureFunctionalExceptionForUnknownDevice(deviceIdentification);

        final com.alliander.osgp.dto.valueobjects.smartmetering.ActivityCalendar activityCalendarDto = this.configurationMapper
                .map(activityCalendar, com.alliander.osgp.dto.valueobjects.smartmetering.ActivityCalendar.class);

        this.osgpCoreRequestMessageSender.send(new RequestMessage(correlationUid, organisationIdentification,
                deviceIdentification, activityCalendarDto), messageType);
    }

    public void handleSetAlarmNotificationsResponse(final String deviceIdentification,
            final String organisationIdentification, final String correlationUid, final String messageType,
            final ResponseMessageResultType deviceResult, final OsgpException exception) {

        LOGGER.info("handleSetAlarmNotificationsResponse for MessageType: {}", messageType);

        ResponseMessageResultType result = deviceResult;
        if (exception != null) {
            LOGGER.error("Set Alarm Notifications Response not ok. Unexpected Exception", exception);
            result = ResponseMessageResultType.NOT_OK;
        }

        this.webServiceResponseMessageSender.send(new ResponseMessage(correlationUid, organisationIdentification,
                deviceIdentification, result, exception, null), messageType);
    }

    public void handleSetConfigurationObjectResponse(final String deviceIdentification,
            final String organisationIdentification, final String correlationUid, final String messageType,
            final ResponseMessageResultType deviceResult, final OsgpException exception) {

        LOGGER.info("handle SetConfigurationObject response for MessageType: {}", messageType);

        ResponseMessageResultType result = deviceResult;
        if (exception != null) {
            LOGGER.error("Device Response not ok. Unexpected Exception", exception);
            result = ResponseMessageResultType.NOT_OK;
        }

        this.webServiceResponseMessageSender.send(new ResponseMessage(correlationUid, organisationIdentification,
                deviceIdentification, result, exception, null), messageType);
    }
    
        public void handleSetActivityCalendarResponse(final String deviceIdentification,
            final String organisationIdentification, final String correlationUid, final String messageType,
            final ResponseMessageResultType responseMessageResultType, final OsgpException exception,
            final String resultString) {
        LOGGER.info("handleSetActivityCalendarResponse for MessageType: {}", messageType);

        ResponseMessageResultType result = responseMessageResultType;
        if (exception != null) {
            LOGGER.error("Set Activity Calendar Response not ok. Unexpected Exception", exception);
            result = ResponseMessageResultType.NOT_OK;
        }

        this.webServiceResponseMessageSender.send(new ResponseMessage(correlationUid, organisationIdentification,
                deviceIdentification, result, exception, resultString), messageType);

    }
}
