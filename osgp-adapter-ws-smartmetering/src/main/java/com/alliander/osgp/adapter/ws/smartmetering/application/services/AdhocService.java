package com.alliander.osgp.adapter.ws.smartmetering.application.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.alliander.osgp.adapter.ws.smartmetering.infra.jms.SmartMeteringRequestMessage;
import com.alliander.osgp.adapter.ws.smartmetering.infra.jms.SmartMeteringRequestMessageSender;
import com.alliander.osgp.adapter.ws.smartmetering.infra.jms.SmartMeteringRequestMessageType;
import com.alliander.osgp.domain.core.services.CorrelationIdProviderService;
import com.alliander.osgp.domain.core.validation.Identification;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.PeriodicMeterReadsRequest;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SmartMeteringDevice;
import com.alliander.osgp.domain.core.valueobjects.smartmetering.SynchronizeTimeReadsRequest;
//import com.alliander.osgp.domain.core.valueobjects.SmartMeteringDevice;
import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

/**
 * @author OSGP
 *
 */
@Service(value = "wsSmartMeteringAdhocService")
@Validated
// @Transactional(value = "coreTransactionManager")
public class AdhocService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdhocService.class);

    @Autowired
    private CorrelationIdProviderService correlationIdProviderService;

    @Autowired
    private SmartMeteringRequestMessageSender smartMeteringRequestMessageSender;
	
	
    public String enqueueSynchronizeTimeRequest(@Identification final String organisationIdentification,
            @Identification final String deviceIdentification,
            @Identification final SynchronizeTimeReadsRequest requestData) throws FunctionalException {

        // TODO: bypassing authorization logic for now, needs to be fixed.

        // final Organisation organisation =
        // this.domainHelperService.findOrganisation(organisationIdentification);
        // final Device device =
        // this.domainHelperService.findActiveDevice(deviceIdentification);
        //
        // this.domainHelperService.isAllowed(organisation, device,
        // DeviceFunction.GET_STATUS);

    	
        LOGGER.debug("enqueueSynchronizeTimeReadsRequest called with organisation {} and device {}",
                organisationIdentification, deviceIdentification);

        final String correlationUid = this.correlationIdProviderService.getCorrelationId(organisationIdentification,
                deviceIdentification);

        final SmartMeteringRequestMessage message = new SmartMeteringRequestMessage(
                SmartMeteringRequestMessageType.REQUEST_PERIODIC_METER_DATA, correlationUid, organisationIdentification,
                deviceIdentification, requestData);

        this.smartMeteringRequestMessageSender.send(message);

        return correlationUid;
    }
    
    
    /**
     * @param organisationIdentification
     * @param device
     * @throws FunctionalException
     */
    public String requestSynchronizeTimeData(final String organisationIdentification,
            final SynchronizeTimeReadsRequest requestData) throws FunctionalException {
        return this.enqueueSynchronizeTimeRequest(organisationIdentification,
                requestData.getDeviceIdentification(), requestData);
    }
    
}
