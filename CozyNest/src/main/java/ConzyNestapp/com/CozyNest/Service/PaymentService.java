package ConzyNestapp.com.CozyNest.Service;

import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import com.stripe.model.Event;
import org.springframework.stereotype.Service;

@Service
public interface PaymentService {
    String getCheckOutSession(BookingEntity booking,String successUrl,String failureUrl);

    void capturePayment(Event event);
}
