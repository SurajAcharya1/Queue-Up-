package com.queueup.qup.controller;

import com.queueup.qup.dto.LoginDto;
import com.queueup.qup.dto.UserDto;
import com.queueup.qup.repository.UserRepo;
import com.queueup.qup.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserServiceImpl userService;
    @Autowired
    UserRepo userRepo;
    public IndexController(UserServiceImpl userService) {

        this.userService = userService;
    }

    @GetMapping()
    public String openMainPage(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "index";

    }

    @PostMapping("create")
    public String createUser(@ModelAttribute UserDto userDto, RedirectAttributes redirectAttributes){
       try {
           userDto = userService.save(userDto);
           redirectAttributes.addFlashAttribute("message","User Registered successfully!!!");
       }catch (Exception e) {
           redirectAttributes.addFlashAttribute("message","User Registration Failed");
           e.fillInStackTrace();
           return "redirect:/#form-modal";
       }
    return "redirect:/#form-modal";
    }
    @PostMapping("login/create")
    public String login(@ModelAttribute LoginDto loginDto, Model model){
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();
        try {
            if (userRepo.getUserByEmail(email).equals(email) && userRepo.getUserByPassword(password).equals(password)) {
                return "redirect:/admin/adminPanel";
            } else {
                model.addAttribute("loginMessage", "Invalid username or Password");
                return "redirect:/";
            }
        }catch (Exception e){
            model.addAttribute("loginMessage", "Something went wrong");
            return "redirect:/";
        }

    }
}
