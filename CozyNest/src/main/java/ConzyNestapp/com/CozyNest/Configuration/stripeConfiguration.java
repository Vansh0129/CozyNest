package ConzyNestapp.com.CozyNest.Configuration;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class stripeConfiguration {


    public stripeConfiguration(@Value("${stripe.secret.key}")  String secretKey){
        Stripe.apiKey=secretKey;
    }

}
