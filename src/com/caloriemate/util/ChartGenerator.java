package com.caloriemate.util;

import com.caloriemate.model.Food;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.List;

public class ChartGenerator {
    public static JFreeChart createCalorieChart(List<Food> foods) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Food food : foods) {
            dataset.addValue(food.getCalories(), "Kalori", food.getCategory());
        }
        return ChartFactory.createBarChart(
            "Kalori per Kategori",
            "Kategori",
            "Kalori",
            dataset
        );
    }
}