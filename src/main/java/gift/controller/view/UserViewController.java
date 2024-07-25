package gift.controller.view;

import gift.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    private final OrderService orderService;

    public UserViewController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/kakao")
    public String kakao() {
        return "kakao";
    }

    @GetMapping("/me")
    public String me(Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        model.addAttribute("orders", orderService.findAll(email));
        return "me";
    }
}
