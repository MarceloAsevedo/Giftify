/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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