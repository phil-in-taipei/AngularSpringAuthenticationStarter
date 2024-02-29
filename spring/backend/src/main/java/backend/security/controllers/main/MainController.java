package backend.security.controllers.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping(value = {"/"})
    public String index() {
        return "index";
    }

   // @GetMapping(value = {"/{regex:[\\w-]+}", "/**/{regex:[\\w-]+}"})
    //public String forward404() {
    //    return "forward:/index";
    //}
}
