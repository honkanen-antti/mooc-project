package sec.project.controller;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.EventRepository;
import sec.project.repository.SignupRepository;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
    @Autowired EventRepository eventRepository;
    
    @Autowired
    private HttpSession session;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String event) {
        signupRepository.save(new Signup(name, address, event, session.getId()));
        return "done";
    }
    
    @RequestMapping(value = "/signups", method = RequestMethod.GET)
    public String signups(Model model)
    {
        // Security flaw #3: Sensitive data exposure due to listing of all the signups not permitted to only logged in users.
        model.addAttribute("items", signupRepository.findAll());
        return "signups";
    }

    @RequestMapping(value = "/mysignups")
    public String mySignups(Model model) {
        model.addAttribute("items", signupRepository.findBySession(session.getId()));
        return "mysignups";
    }
    
    @RequestMapping(value = "/mysignups/{id}", method = RequestMethod.GET)
    public String signupDetails(Model model, @PathVariable Long id) {
        // Security flaw #4: Broken access control due to not validating session id with the signup.
        model.addAttribute("signup", signupRepository.findOne(id));
        return "details";
    }
    
    @RequestMapping(value = "/mysignups/{id}", method = RequestMethod.DELETE)
    public String signupDetails(@PathVariable Long id) {
        // Security flaw #4: Broken access control due to not validating session id with the signup.
        signupRepository.delete(id);
        return "redirect:/mysignups";
    }
}
