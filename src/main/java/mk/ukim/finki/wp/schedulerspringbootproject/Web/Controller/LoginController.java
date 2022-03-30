package mk.ukim.finki.wp.schedulerspringbootproject.Web.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {


    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String error,
                               HttpServletRequest request,
                               Model model) {

        if(error != null){
            model.addAttribute("hasError", true);
        }
        if(request.getRemoteUser() != null){
            return "redirect:/home";
        }

        model.addAttribute("bodyContent", "login");
        return "master-template";
    }


}
