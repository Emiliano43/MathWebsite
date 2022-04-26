package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.model.Review;
import com.example.service.ReviewService;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private ReviewService service;

    @RequestMapping(value = "/home")
    public String homePage(Model model) {
        return "home";
    }

    @RequestMapping(value = "/feedback")
    public String reviewsPage(Model model) {
        model.addAttribute("reviews", service.getAll());
        return "reviews";
    }

    @RequestMapping(value = "/feedback/editor")
    public String editorPage(Model model) {
        model.addAttribute("review", new Review());
        return "editor";
    }

    @RequestMapping(value = "/feedback/editor/submit", method = RequestMethod.POST)
    public String submitReview(@ModelAttribute Review review) {
        service.save(review);
        return "redirect:../";
    }

    @RequestMapping(value = "/feedback/delete/{reviewId}")
    public String deteleReview(@PathVariable("reviewId") Integer reviewId) {
        service.deleteById(reviewId);
        return "redirect:../";
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "login";
    }
}
