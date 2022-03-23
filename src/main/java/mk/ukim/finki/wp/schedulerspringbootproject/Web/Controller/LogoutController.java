package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public String logout(HttpServletRequest request, Model model) {
        request.getSession().invalidate();
        model.addAttribute("bodyContent", "login");
        return "master-template";
    }

}
