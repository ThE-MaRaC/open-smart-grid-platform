/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.steps.ws.admin.devicemanagement;

import static com.alliander.osgp.platform.cucumber.core.Helpers.getString;

import java.util.Map;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.soap.client.SoapFaultClientException;

import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.Device;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.FindDevicesWhichHaveNoOwnerRequest;
import com.alliander.osgp.adapter.ws.schema.admin.devicemanagement.FindDevicesWhichHaveNoOwnerResponse;
import com.alliander.osgp.domain.core.repositories.DeviceAuthorizationRepository;
import com.alliander.osgp.domain.core.repositories.DeviceRepository;
import com.alliander.osgp.domain.core.repositories.OrganisationRepository;
import com.alliander.osgp.platform.cucumber.core.ScenarioContext;
import com.alliander.osgp.platform.cucumber.steps.Keys;
import com.alliander.osgp.platform.cucumber.support.ws.admin.AdminDeviceManagementClient;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Class with all the remove organization requests steps
 */
public class FindDevicesWithoutOwner {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private DeviceAuthorizationRepository deviceAuthorizationRepository;

    @Autowired
    private AdminDeviceManagementClient client;

    /**
     * Send an find devices without organization request to the Platform.
     *
     * @param requestParameter
     *            An list with request parameters for the request.
     * @throws Throwable
     */
    @When("^receiving a find devices without organization request$")
    public void receivingAFindDevicesWithoutOrganizationRequest() throws Throwable {

        final FindDevicesWhichHaveNoOwnerRequest request = new FindDevicesWhichHaveNoOwnerRequest();

        try {
            ScenarioContext.Current().put(Keys.RESPONSE, this.client.findDevicesWithoutOwner(request));
        } catch (final SoapFaultClientException e) {
            ScenarioContext.Current().put(Keys.RESPONSE, e);
        }
    }

    /**
     * Verify that the find devices without organization response has items.
     *
     * @throws Throwable
     */
    @Then("^the find devices without organization response contains \"([^\"]*)\" devices?$")
    public void theFindDevicesWithoutOrganizationResponseContains(final Integer expectedCount) throws Throwable {

        final FindDevicesWhichHaveNoOwnerResponse findDevicesWhichHaveNoOwnerResponse = (FindDevicesWhichHaveNoOwnerResponse) ScenarioContext
                .Current().get(Keys.RESPONSE);

        Assert.assertEquals((int) expectedCount, findDevicesWhichHaveNoOwnerResponse.getDevices().size());
    }

    /**
     * Verify that the find devices without organization response is failed.
     *
     * @throws Throwable
     */
    @Then("^the find devices without organization response contains at index \"([^\"]*)\"$")
    public void theFindDevicesWithoutOrganizationResponseContainsAtIndex(final Integer expectedIndex,
            final Map<String, String> expectedResult) throws Throwable {

        final FindDevicesWhichHaveNoOwnerResponse response = (FindDevicesWhichHaveNoOwnerResponse) ScenarioContext
                .Current().get(Keys.RESPONSE);

        for (final Device device : response.getDevices()) {
            Assert.assertEquals(getString(expectedResult, Keys.KEY_DEVICE_IDENTIFICATION),
                    device.getDeviceIdentification());
            Assert.assertTrue(device.getOrganisations().isEmpty());
            Assert.assertNull(device.getOwner());
        }
    }
}