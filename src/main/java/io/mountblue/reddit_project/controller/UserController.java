package io.mountblue.reddit_project.controller;

import io.mountblue.reddit_project.model.SubReddit;
import io.mountblue.reddit_project.model.User;
import io.mountblue.reddit_project.service.SubRedditService;
import io.mountblue.reddit_project.service.SubscriptionService;
import io.mountblue.reddit_project.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final SubRedditService subRedditService;
    private final SubscriptionService subscriptionService;


    public UserController(UserService userService, SubRedditService subRedditService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subRedditService = subRedditService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/new")
    public String userPage(Model model) {
        model.addAttribute("userRegistrationInfo", new User());
        return "register";
    }

    @PostMapping("/submit")
    public String newUser(User user, Model model) {
        System.out.println("Entering");
        Set<String> emailSet = userService.getEmailSet();
        Set<String> userNameSet = userService.getUserNameSet();

        if (emailSet.contains(user.getEmail()) || userNameSet.contains(user.getUsername())) {
            return "redirect:/user/new?error=emailOrUsernameExists";
        }
        userService.saveNewUser(user);
        return "login";
    }

    @GetMapping("/profile")
    public String viewUserProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = ((User) authentication.getPrincipal()).getUsername();

        User user = userService.getUserByUsername(username);

        model.addAttribute("user", user);

        return "user-view";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/profile/update")
    public String editProfile(@ModelAttribute("user") User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        user.setUserInfo(updatedUser.getUserInfo());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());
        user.setUsername(updatedUser.getUsername());
        userService.updateUser(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/profile/delete")
    public String deleteProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        userService.deleteUser(user.getId());
        SecurityContextHolder.clearContext();
        return "redirect:/logout";
    }
    @PostMapping("/join")
    public String joinSubReddit(@RequestParam String name) {
        System.out.println(name);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        SubReddit subReddit = subRedditService.getSubReddit(name);
        subscriptionService.saveSubScription(user,subReddit);
        return "redirect:/?subRedditName=" + name;
    }

    @PostMapping("/leave")
    public String leaveSubReddit(@RequestParam String name){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        SubReddit subReddit = subRedditService.getSubReddit(name);
        subscriptionService.deleteSubScription(user,subReddit);
        return "redirect:/";
    }
}
