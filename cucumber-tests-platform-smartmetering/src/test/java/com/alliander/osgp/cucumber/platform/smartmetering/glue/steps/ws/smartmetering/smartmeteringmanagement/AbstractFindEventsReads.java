/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.smartmeteringmanagement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alliander.osgp.adapter.ws.schema.smartmetering.management.Event;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.EventType;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindEventsAsyncRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindEventsAsyncResponse;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindEventsRequest;
import com.alliander.osgp.adapter.ws.schema.smartmetering.management.FindEventsResponse;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.PlatformKeys;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import com.alliander.osgp.cucumber.platform.smartmetering.PlatformSmartmeteringKeys;
import com.alliander.osgp.cucumber.platform.smartmetering.glue.steps.ws.smartmetering.SmartMeteringStepsBase;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.FindEventsRequestFactory;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.SmartMeteringManagementRequestClient;
import com.alliander.osgp.cucumber.platform.smartmetering.support.ws.smartmetering.management.SmartMeteringManagementResponseClient;

public abstract class AbstractFindEventsReads extends SmartMeteringStepsBase {

    @Autowired
    private SmartMeteringManagementRequestClient<FindEventsAsyncResponse, FindEventsRequest> requestClient;

    @Autowired
    private SmartMeteringManagementResponseClient<FindEventsResponse, FindEventsAsyncRequest> responseClient;

    private static final String EXPECTED_NUMBER_OF_EVENTS = "ExpectedNumberOfEvents";

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractFindEventsReads.class);

    protected abstract String getEventLogCategory();

    public void receivingAFindEventsRequest(final Map<String, String> requestData) throws Throwable {
        final Map<String, String> settings = new HashMap<String, String>();
        settings.put(PlatformSmartmeteringKeys.EVENT_TYPE, this.getEventLogCategory());
        settings.put(PlatformSmartmeteringKeys.KEY_BEGIN_DATE,
                requestData.get(PlatformSmartmeteringKeys.KEY_BEGIN_DATE));
        settings.put(PlatformSmartmeteringKeys.KEY_END_DATE, requestData.get(PlatformSmartmeteringKeys.KEY_END_DATE));
        settings.put(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION,
                requestData.get(PlatformSmartmeteringKeys.KEY_DEVICE_IDENTIFICATION));
        final FindEventsRequest request = FindEventsRequestFactory.fromParameterMap(settings);
        final FindEventsAsyncResponse asyncResponse = this.requestClient.doRequest(request);

        assertNotNull("asyncResponse should not be null", asyncResponse);
        ScenarioContext.current().put(PlatformSmartmeteringKeys.KEY_CORRELATION_UID, asyncResponse.getCorrelationUid());
    }

    public void eventsShouldBeReturned(final Map<String, String> settings) throws Throwable {
        final FindEventsAsyncRequest asyncRequest = FindEventsRequestFactory.fromScenarioContext();
        final FindEventsResponse response = this.responseClient.getResponse(asyncRequest);

        assertNotNull("FindEventsRequestResponse should not be null", response);
        assertNotNull("Expected events", response.getEvents());
        assertEquals("Number of events should match", Integer.parseInt(settings.get(EXPECTED_NUMBER_OF_EVENTS)),
                response.getEvents().size());

        /*
         * For every event in the response, check if it matches with the
         * AllowedEventTypes list. If so, the EventType is from the correct type
         * as expected.
         */
        for (final Event event : response.getEvents()) {
            Event eventMatch = null;
            for (final EventType item : this.getAllowedEventTypes()) {
                if (item.toString().equals(event.getEventType().toString())) {
                    eventMatch = event;
                    break;
                }
            }
            assertNotNull("No match found for EventType", eventMatch);
        }
    }

    public void eventsForAllTypesShouldBeReturned(final Map<String, String> settings) throws Throwable {
        this.eventsShouldBeReturned(
                SettingsHelper.addDefault(settings, PlatformKeys.KEY_EVENTS_NODELIST_EXPECTED, "true"));
    }

    public void eventsShouldBeReturned(final int numberOfEvents, final Map<String, String> settings) throws Throwable {
        this.eventsShouldBeReturned(
                SettingsHelper.addDefault(settings, EXPECTED_NUMBER_OF_EVENTS, String.valueOf(numberOfEvents)));
    }

    /**
     * Return the types of events allowed in a response, an assert will be done.
     *
     * @return
     */
    protected abstract List<EventType> getAllowedEventTypes();

    /**
     * To be called from subclasses, checks whether event types returned belong
     * to the log category requested.
     *
     * @param allowed
     * @throws XPathExpressionException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
}
