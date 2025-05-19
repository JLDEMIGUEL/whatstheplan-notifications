package com.whatstheplan.notifications.handler;

import com.whatstheplan.notifications.model.CancellationRequest;
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
public class CancelledEventHandler {

    private static final String EMAIl_SUBJECT = "Event %s has been cancelled!";
    private static final String EMAIL_TEMPLATE = "cancelledEvent";

    private final MailService mailService;
    private final TemplateEngine templateEngine;

    @Value("${whatstheplan.ui-url}")
    private String uiUrl;

    public void sendEmail(CancellationRequest request) {
        log.info("Sending cancelled event email {}", request);
        Context context = new Context();
        context.setVariables(Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "event", request.getEvent(),
                "homeUrl", uiUrl
        ));
        String emailText = templateEngine.process(EMAIL_TEMPLATE, context);
        mailService.sendEmail(request.getEmail(), EMAIl_SUBJECT.formatted(request.getEvent().getTitle()), emailText);
    }
}
