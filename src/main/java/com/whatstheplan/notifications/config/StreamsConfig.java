package com.whatstheplan.notifications.config;

import com.whatstheplan.notifications.handler.CancelledEventHandler;
import com.whatstheplan.notifications.handler.SuccessfulRegistrationHandler;
import com.whatstheplan.notifications.handler.WelcomeMessageMailHandler;
import com.whatstheplan.notifications.model.CancellationRequest;
import com.whatstheplan.notifications.model.RegistrationRequest;
import com.whatstheplan.notifications.model.WelcomeRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class StreamsConfig {

    @Bean
    public Consumer<WelcomeRequest> welcomeMessage(WelcomeMessageMailHandler handler) {
        return handler::sendEmail;
    }

    @Bean
    public Consumer<RegistrationRequest> successfulRegistrationEmail(SuccessfulRegistrationHandler handler) {
        return handler::sendEmail;
    }

    @Bean
    public Consumer<CancellationRequest> cancelledEvent(CancelledEventHandler handler) {
        return handler::sendEmail;
    }
}
