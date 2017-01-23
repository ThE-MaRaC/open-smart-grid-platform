/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.platform.cucumber.config.ws.publiclighting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;

import com.alliander.osgp.platform.cucumber.config.ws.BaseWebServiceConfig;
import com.alliander.osgp.platform.cucumber.support.ws.WebServiceTemplateFactory;

@Configuration
public class DeviceMonitoringWebServiceConfig extends BaseWebServiceConfig {

    @Value("${web.service.template.default.uri.publiclighting.devicemonitoring}")
    private String webserviceTemplateDefaultUriPublicLightingDeviceMonitoring;

    @Value("${jaxb2.marshaller.context.path.publiclighting.devicemonitoring}")
    private String contextPathPublicLightingDeviceMonitoring;

    @Bean
    public WebServiceTemplateFactory publicLightingDeviceMonitoringWstf() {
        return new WebServiceTemplateFactory.Builder().setMarshaller(this.publiclightingDeviceMonitoringMarshaller())
                .setMessageFactory(this.messageFactory())
                .setDefaultUri(this.baseUri.concat(this.webserviceTemplateDefaultUriPublicLightingDeviceMonitoring))
                .setKeyStoreType(this.webserviceKeystoreType).setKeyStoreLocation(this.webserviceKeystoreLocation)
                .setKeyStorePassword(this.webserviceKeystorePassword)
                .setTrustStoreFactory(this.webServiceTrustStoreFactory()).setApplicationName(this.applicationName)
                .build();
    }

    /**
     * Method for creating the Marshaller for PublicLighting DeviceMonitoring.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller publiclightingDeviceMonitoringMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.contextPathPublicLightingDeviceMonitoring);

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for
     * PublicLighting DeviceMonitoring.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor publicLightingDeviceMonitoringMarshallingPayloadMethodProcessor() {
        return new MarshallingPayloadMethodProcessor(this.publiclightingDeviceMonitoringMarshaller(),
                this.publiclightingDeviceMonitoringMarshaller());
    }

}
