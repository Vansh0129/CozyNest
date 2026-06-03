package ConzyNestapp.com.CozyNest.Controller;

import ConzyNestapp.com.CozyNest.Service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks")
public class WebHooksController {

    private final PaymentService paymentService;

    @Value("${stripe.webhooks.key}")
    private final String webhookKey;

    @PostMapping(value = "/payment")
    public ResponseEntity<Void> capturePayment(@RequestBody String payload,@RequestHeader("Stripe-Signature") String header) throws SignatureVerificationException {
        try{
            Event event= Webhook.constructEvent(payload,header,webhookKey);
             paymentService.capturePayment(event);

        }catch (Exception e){
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }
}
