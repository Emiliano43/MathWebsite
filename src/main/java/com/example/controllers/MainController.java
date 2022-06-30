package com.example.controllers;

import flanagan.integration.RungeKutta;
import mathLibrary.func.ArrayFunc;
import mathLibrary.ode.solvers.DerivnFunction;
import mathLibrary.ode.solvers.OdeSystemSolver;
import mathLibrary.plot.MathChart;
import mathLibrary.polynom.util.MathUtils;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.models.Review;
import com.example.services.ReviewService;

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(path = "/home")
    public String homePage(Model model) {
        return "home";
    }

    @RequestMapping(path = "/profile")
    public String profilePage(Model model) {
        return "profile";
    }

    @RequestMapping(path = "/theory3")
    public String theory3Page(Model model) {
        return "theory3";
    }

    @RequestMapping(path = "/feedback")
    public String reviewsPage(Model model) {
        model.addAttribute("reviews", reviewService.getAll());
        return "reviews";
    }

    @RequestMapping(path = "/feedback/editor/submit", method = RequestMethod.POST)
    public String submitReview(@ModelAttribute Review review) {
        reviewService.save(review);
        return "redirect:../";
    }

    @RequestMapping(path = "/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping(path = "/solution")
    public String solve(Model model, HttpServletResponse response) throws IOException {
        /*DerivnFunction function = (x, y) -> new double[]{y[1], -y[0]};
        double x0 = 0.0;
        double[] y0 = {0.0, 1.0};

        OdeSystemSolver odeSystemSolver = new OdeSystemSolver(function, x0, y0);
        double[] x = MathUtils.linspace(0, 20.0, 1000);
        double[] y1Exact = ArrayFunc.apply(t -> Math.sin(t), x);
        double[] y2Exact = ArrayFunc.apply(t -> Math.cos(t), x);
        double[] xEuler = MathUtils.linspace(0, 20.0, 100);
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
        figure.show(true);*/

        double pi = Math.PI;

        flanagan.integration.DerivnFunction dfuncs = new flanagan.integration.DerivnFunction() {

            @Override
            public double[] derivn(double x, double[] yy) {
                double y = yy[0];
                double z = yy[1];
                return new double[]{z, -y};
            }
        };

        RungeKutta integrator = new RungeKutta();
        integrator.setStepSize(1e-4);
        integrator.setInitialValueOfX(0.0);
        integrator.setFinalValueOfX(10.0);
        integrator.setInitialValuesOfY(new double[]{0.0, 1.0});

        double[][] sol = integrator.fourthOrder(dfuncs, 100);

//		integrator.plot() ;

        //MathUtils.show(sol[0]);

        MathChart figure = new MathChart();
        figure.plot(sol[0], sol[1], "red", 1f, "1st");
        figure.plot(sol[0], sol[2], "blue", 1f, "2nd");
        figure.renderPlot();
        figure.run(true);
        figure.xlabel("x values");
        figure.ylabel("y(x) solution");
        figure.legendON();
        figure.markerON();

        //saveChartAsPNGImage(figure.getChart(), 500, 500, response);
        //writeChartAsPNGImage(figure.getChart(), 500, 500, response);


        return "solution";
    }

    private void writeChartAsPNGImage(final JFreeChart chart, final int width, final int height, HttpServletResponse response) throws IOException, IOException {
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height);

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        ChartUtilities.writeBufferedImageAsPNG(response.getOutputStream(), bufferedImage);
    }

    private void saveChartAsPNGImage(final JFreeChart chart, final int width, final int height, HttpServletResponse response) throws IOException, IOException {
        final BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        ChartUtilities.saveChartAsPNG(new File("C:\\Users\\enazy\\IdeaProjects\\Diplom\\src\\main\\resources\\static\\images\\plot.png"), chart, width, height);
    }
}
