package ConzyNestapp.com.CozyNest;

import ConzyNestapp.com.CozyNest.Dto.Request.SignUpDto;
import ConzyNestapp.com.CozyNest.Security.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class CozyNestApplicationTests {
	@Autowired
			AuthServiceImpl authService;

//"email":"vanshlute@gmail.com",
//    "password": "vansh@123"
//	@Test
	void contextLoads() {
		authService.signUpForHotelManager(new SignUpDto("vansh","vanshlute@gmail.com","vansh@123"));

	}

}
