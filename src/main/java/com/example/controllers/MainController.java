package com.example.controllers;

import mathLib.func.ArrayFunc;
import mathLib.ode.solvers.DerivnFunction;
import mathLib.ode.solvers.OdeSystemSolver;
import mathLib.plot.MatlabChart;
import mathLib.polynom.util.MathUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
import java.io.OutputStream;
import java.net.http.HttpResponse;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value = "/home")
    public String homePage(Model model) {
        return "home";
    }

    @RequestMapping(value = "/feedback")
    public String reviewsPage(Model model) {
        model.addAttribute("reviews",   reviewService.getAll());
        return "reviews";
    }

    @RequestMapping(value = "/feedback/editor/submit", method = RequestMethod.POST)
    public String submitReview(@ModelAttribute Review review) {
        reviewService.save(review);
        return "redirect:../";
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        return "login";
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


        MatlabChart figure = new MatlabChart();
        //figure.plot(x, y2Exact, "b");
        figure.plot(xEuler, yEuler[0], "red", 1f, "1st");
        figure.plot(xEuler, yEuler[1], "blue", 1f, "2nd");
        figure.renderPlot();
        figure.legendON();
        figure.setFigLineWidth(1, 1);
        figure.xlabel("x values");
        figure.ylabel("y(x) solution");
        figure.show(true);

        //saveChartAsPNGImage(figure.getChart(), 500, 500, response);

        /*final DefaultCategoryDataset categoryDataset = buildHistoryOfInternetUsersCategoryDataset();
        final String title = "History of users of the Internet from";
        final String categoryAxisLabel = "Year";
        final String valueAxisLabel = "Num. of Users (in millions)";
        final boolean legend = true;
        final boolean tooltips = true;
        final boolean urls = true;

        final JFreeChart lineChart = ChartFactory.createLineChart(title, categoryAxisLabel, valueAxisLabel, categoryDataset, PlotOrientation.VERTICAL, legend, tooltips, urls);
        final CategoryPlot categoryPlot = (CategoryPlot) lineChart.getPlot();
        final CategoryItemRenderer categoryItemRenderer = categoryPlot.getRenderer();


        final ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.TOP_LEFT);*/


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


    /*private DefaultCategoryDataset buildHistoryOfInternetUsersCategoryDataset() {
        final Comparable<String> rowKey = "Num. of Users";
        final DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        //statisticsService.getsHistoryOfInternetUsers().forEach((history) -> categoryDataset.setValue(history.getMillionsOfUsers(), rowKey, Integer.valueOf(history.getYear())));

        return categoryDataset;


    }*/
}
