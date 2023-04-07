package com.kgds.fi.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {AppConfiguration.class})
@ExtendWith(SpringExtension.class)
class AppConfigurationTest {
    @Autowired
    private AppConfiguration appConfiguration;

    /**
     * Method under test: {@link AppConfiguration#webClient()}
     */
    @Test
    void testWebClient() {
        // TODO: Complete this test.
        //   Reason: R002 Missing observers.
        //   Diffblue Cover was unable to create an assertion.
        //   Add getters for the following fields or make them package-private:
        //     DefaultWebClient.builder
        //     DefaultWebClient.defaultCookies
        //     DefaultWebClient.defaultHeaders
        //     DefaultWebClient.defaultRequest
        //     DefaultWebClient.defaultStatusHandlers
        //     DefaultWebClient.exchangeFunction
        //     DefaultWebClient.observationConvention
        //     DefaultWebClient.observationRegistry
        //     DefaultWebClient.uriBuilderFactory

        appConfiguration.webClient();
    }
}

