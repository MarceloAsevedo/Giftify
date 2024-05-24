
package Giftify.Giftify.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/") // Renamed to "index"
    public String index() {
        return "static/index.html"; // Updated to return "index"
    }

}