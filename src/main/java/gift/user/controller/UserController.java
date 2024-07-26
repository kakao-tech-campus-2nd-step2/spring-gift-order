package gift.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import gift.user.domain.Role;
import gift.dto.request.UserDetails;

@Controller
public class UserController {

	// 로그인 되어있으면 LoginMain으로, 아니면 Main으로 이동
	@GetMapping("/main")
	public String viewProducts(Model model, @RequestAttribute("userDetails") UserDetails userDetails) {
		if(userDetails.roles().contains(Role.GUEST)) {
			return "user/login";
		}
		return "user/main";
	}
}
