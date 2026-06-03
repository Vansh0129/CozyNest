package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks")
public class WebHooksController {

    private final PaymentService paymentService;

    @Value("${stripe.webhooks.key}")
    private  String webhookKey;     //Basics:-final key String cannot be reassigned.

    @PostMapping(value = "/payment")
    public ResponseEntity<Void> capturePayment(@RequestBody String payload,@RequestHeader("Stripe-Signature") String header) throws SignatureVerificationException {
        log.info("Booking Payment event accured");
        try{
            Event event= Webhook.constructEvent(payload,header,webhookKey);
             paymentService.capturePayment(event);
             return ResponseEntity.noContent().build();

        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
