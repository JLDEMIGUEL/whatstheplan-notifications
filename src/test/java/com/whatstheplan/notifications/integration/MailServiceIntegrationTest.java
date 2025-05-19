package com.whatstheplan.notifications.integration;

import com.whatstheplan.notifications.model.CancellationRequest;
import com.whatstheplan.notifications.model.Event;
import com.whatstheplan.notifications.model.RegistrationRequest;
import com.whatstheplan.notifications.model.WelcomeRequest;
import com.whatstheplan.notifications.service.MailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestChannelBinderConfiguration.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class MailServiceIntegrationTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private InputDestination input;

    @MockitoBean
    private MailService mailService;

    @Test
    void givenWelcomeRequest_whenWelcomeMessageConsumed_thenSendWelcomeEmail() {
        // given
        WelcomeRequest request = WelcomeRequest.builder()
                .username("user123")
                .email("user@example.com")
                .build();

        // when
        input.send(MessageBuilder.withPayload(request).build(), "mail.welcomeMessage");

        // then
        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);

        verify(mailService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), any());

        assertEquals(request.getEmail(), toCaptor.getValue());
        assertEquals("Welcome user123 to WhatsThePlan!", subjectCaptor.getValue());
    }

    @Test
    void givenRegistrationRequest_whenRegistrationEmailConsumed_thenSendRegistrationEmail() {
        // given
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .title("Sample Event")
                .description("Event Desc")
                .dateTime(LocalDateTime.now().plusDays(1))
                .duration(Duration.ofHours(2))
                .location("Location")
                .organizerUsername("organizerUser")
                .build();

        RegistrationRequest request = RegistrationRequest.builder()
                .username("regUser")
                .email("reg@example.com")
                .event(event)
                .build();

        // when
        input.send(MessageBuilder.withPayload(request).build(), "mail.successfulRegistrationEmail");

        // then
        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);

        verify(mailService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), any());

        assertEquals(request.getEmail(), toCaptor.getValue());
        assertEquals("Your Registration for Sample Event is Confirmed!", subjectCaptor.getValue());
    }

    @Test
    void givenCancellationRequest_whenCancellationEmailConsumed_thenSendCancellationEmail() {
        // given
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .title("Cancelled Event")
                .description("Cancelled Desc")
                .dateTime(LocalDateTime.now().plusDays(2))
                .duration(Duration.ofHours(3))
                .location("Cancel Location")
                .organizerUsername("cancelOrganizer")
                .build();

        CancellationRequest request = CancellationRequest.builder()
                .username("cancelUser")
                .email("cancel@example.com")
                .event(event)
                .build();

        // when
        input.send(MessageBuilder.withPayload(request).build(), "mail.cancelledEvent");

        // then
        ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);

        verify(mailService).sendEmail(toCaptor.capture(), subjectCaptor.capture(), any());

        assertEquals(request.getEmail(), toCaptor.getValue());
        assertEquals("Event Cancelled Event has been cancelled!", subjectCaptor.getValue());
    }
}
