package com.example.controllers;

import com.example.dao.ReviewRepository;
import com.example.models.Review;
import com.example.services.ReviewService;
import mathLibrary.func.ArrayFunc;
import mathLibrary.ode.solvers.DerivnFunction;
import mathLibrary.ode.solvers.OdeSystemSolver;
import mathLibrary.plot.MathChart;
import mathLibrary.polynom.util.MathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(path = "/admin")
public class AdminController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @RequestMapping(value = "/home")
    public String adminHome(Model model) {
        return "admin-home";
    }

    @RequestMapping(value = "/feedback")
    public String reviewsPage(Model model) {
        model.addAttribute("reviews", reviewService.getAll());
        return "admin-reviews";
    }


    @RequestMapping(value = "/feedback/editor")
    public String editorPage(Model model) {
        model.addAttribute("review", new Review());
        return "admin-editor";
    }

    @RequestMapping(value = "/feedback/editor/submit", method = RequestMethod.POST)
    public String submitReview(@ModelAttribute Review review) {
        reviewService.save(review);
        return "redirect:../";
    }

    @RequestMapping(value = "/feedback/delete/{reviewId}")
    public String deteleReview(@PathVariable("reviewId") Integer reviewId) {
        reviewService.deleteById(reviewId);
        return "redirect:../";
    }

    @RequestMapping(value = "/solution")
    public String solve(Model model, HttpServletResponse response) throws IOException {
        DerivnFunction function = (x, y) -> new double[]{y[1], -y[0]};
        double x0 = 0.0;
        double[] y0 = {0.0, 1.0};

        OdeSystemSolver odeSystemSolver = new OdeSystemSolver(function, x0, y0);
        double[] x = MathUtils.linspace(0.0, 20.0, 1000);
        double[] y1Exact = ArrayFunc.apply(t -> Math.sin(t), x);
        double[] y2Exact = ArrayFunc.apply(t -> Math.cos(t), x);
        double[] xEuler = MathUtils.linspace(0.0, 20.0, 100);
        double[][] yEuler = odeSystemSolver.euler(xEuler);

        MathChart figure = new MathChart();
        //figure.plot(x, y2Exact, "b");
        figure.plot(xEuler, yEuler[0], "red", 1f, "1st");
        figure.plot(xEuler, yEuler[1], "blue", 1f, "2nd");
        figure.renderPlot();
        figure.legendON();
        figure.setFigLineWidth(1, 1);
        figure.xlabel("x values");
        figure.ylabel("y(x) solution");
        figure.show(true);
        return "solution";
    }


    @RequestMapping(value = "/logout")
    public String logout(Model model) {
        return "home";
    }
}
