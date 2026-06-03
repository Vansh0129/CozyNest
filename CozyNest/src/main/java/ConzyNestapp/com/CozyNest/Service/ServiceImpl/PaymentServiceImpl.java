package ConzyNestapp.com.CozyNest.Service.ServiceImpl;

import ConzyNestapp.com.CozyNest.Entity.BookingEntity;
import ConzyNestapp.com.CozyNest.Entity.Enums.BookingStatus;
import ConzyNestapp.com.CozyNest.Entity.UserEntity;
import ConzyNestapp.com.CozyNest.Repository.BookingRepository;
import ConzyNestapp.com.CozyNest.Repository.InventoryRepository;
import ConzyNestapp.com.CozyNest.Service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceItemUpdateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    public String getCheckOutSession(BookingEntity booking, String successUrl, String failureUrl) {
        // All Imports Must From Stripe Dependency
        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        SessionCreateParams.LineItem.PriceData priceMemo=SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount(booking.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Hotel " + booking.getHotel().getName() + " : " + booking.getRoom_Id().getType())
                                .setDescription("Booking Id" + booking.getBooking_id())
                                .build()
                ).build();
        // 2. Build PriceData for the Platform Fee (50 INR = 5000 Paisa)
        SessionCreateParams.LineItem.PriceData platFormFee=SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmount(BigDecimal.valueOf(50).multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("PlatForm Fee" )
                                .setDescription("Including All Taxes")
                                .build()
                ).build();


        try {
            CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                    .setName(user.getName())
                    .setEmail(user.getEmail())
                    .build();

            Customer customer = Customer.create(customerCreateParams);

            SessionCreateParams sessionConstruct = SessionCreateParams.builder()        //Creating session with this Pricing Schema
                    .setMode(SessionCreateParams.Mode.PAYMENT) // available at official page under dev documentation
                    .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED)
                    .setCustomer(customer.getId())
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(failureUrl)
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(priceMemo)
                                    .build()
                    )
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(platFormFee)
                                    .build())
            .build();

            Session session = Session.create(sessionConstruct);

            // Session created now need to save Session id into DB
            booking.setTransactionId(session.getId());
            bookingRepository.save(booking);

            return session.getUrl();

        } catch (StripeException e) {
            log.error("Stripe session creation failed for booking ID: {}", booking.getBooking_id(), e);
            throw new RuntimeException("Stripe Payment Error !");
        }
    }

    @Override
    public void capturePayment(Event event) {
        if("checkout.session.completed".equals(event.getType())){
            log.info("booking came under the complete state");

            Session session= (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if(session==null ) return;
            BookingEntity booking=bookingRepository.findBytransactionId(session.getId()).orElseThrow(()->  new BadCredentialsException("No transactional id !"));
            booking.setBookingStatus(BookingStatus.CONFIRMED);
            bookingRepository.save(booking);

            //deduct reserved count and increase the bookedCount. and before Lock the DB
            inventoryRepository.findAndLockReservedInventory(booking.getRoom_Id(),booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoom_Count());      //lock
            inventoryRepository.ConfirmBooking(booking.getRoom_Id(),booking.getCheckInDate(),booking.getCheckOutDate(),booking.getRoom_Count());        //save counts.

        }else {
            log.warn("Unhandled event {}",event.getType());
        }
    }
}
