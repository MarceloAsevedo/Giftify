
package Giftify.Giftify.Controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorControllerA implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        
        return "static/error.html"; 
    }

    public String getErrorPath() {
        return "static/error.html";
    }
}