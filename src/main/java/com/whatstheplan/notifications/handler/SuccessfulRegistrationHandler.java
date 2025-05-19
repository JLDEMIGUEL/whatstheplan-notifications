package com.whatstheplan.notifications.handler;

import com.whatstheplan.notifications.model.RegistrationRequest;
import com.whatstheplan.notifications.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SuccessfulRegistrationHandler {

    private static final String EMAIl_SUBJECT = "Your Registration for %s is Confirmed!";
    private static final String EMAIL_TEMPLATE = "registrationEvent";

    private final MailService mailService;
    private final TemplateEngine templateEngine;

    @Value("${whatstheplan.ui-url}")
    private String uiUrl;

    public void sendEmail(RegistrationRequest request) {
        log.info("Sending event registration email {}", request);
        Context context = new Context();
        context.setVariables(Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "event", request.getEvent(),
                "eventUrl", uiUrl + "events/%s".formatted(request.getEvent().getId())
        ));
        String emailText = templateEngine.process(EMAIL_TEMPLATE, context);
        mailService.sendEmail(request.getEmail(), EMAIl_SUBJECT.formatted(request.getEvent().getTitle()), emailText);
    }
}
