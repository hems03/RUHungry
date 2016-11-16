package com.example.h.ruhungry;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by h on 11/16/2016.
 */

public class Menu {

    /**
     * location_name : Brower Commons
     * date : 1479298080625
     * meals : [{"meal_name":"Breakfast","meal_avail":true,"genres":[{"genre_name":"Breakfast Meats","items":["Grilled Turkey Sausage Links","Vegan Breakfast Patties"]},{"genre_name":"Breakfast Entrees","items":["Belgian Waffles","Hard Boiled Eggs","Potato Pancakes","Scramble Cholesterol Free Egg","Scrambled Eggs","Waffle Red Velvet"]},{"genre_name":"Breakfast Bakery","items":["Assorted Bagels","Blueberry Crumb Cake","Cheese Topped Cinnamon Bun","Glazed Cake Donut","Muffin Apple Cinnamon Raisin"]},{"genre_name":"Breakfast Misc","items":["Green Seedless Grapes","Honeydew Melon","Oatmeal","Vegan Grits Hot Cereal","Watermelon"]}]},{"meal_name":"Lunch","meal_avail":true,"genres":[{"genre_name":"Salad Bar","items":["Grilled Chicken Breast"]},{"genre_name":"Soups","items":["Italian Vegetable Soup","Red Onion W/ Chicken Soup"]},{"genre_name":"Deli Bar Entree","items":["Roast Beef","Shrimp Salad"]},{"genre_name":"Entrees","items":["Beef Pot Pie","Farfalla Broc. Rabe Aioli","Fried Chicken Patty","Garden Vegetable Pot Pie","Grilled Tuna Steak"]},{"genre_name":"Starch & Potatoes","items":["Breaded Onion Rings"]},{"genre_name":"Desserts","items":["Individual Carrot Cake With Cream Cheese Icing"]},{"genre_name":"Accompaniments","items":["Hamburger Bun 4\""]},{"genre_name":"Cook To Order Bar","items":["Baked Brown Rice","Chicken Stir Fry W/ Plum Sauce","Fried Rice With Egg","Korean Sweet Potato Noodles","Sesame Beef","Tofu And Wild Mushroom Stir Fry","Vegetable Lo Mein"]},{"genre_name":"Lunch To Go","items":["Chicken Buffalo Wings Fresh","Chicken Nuggets Knight Room","Grilled Chicken Breast","Knight Room Caesar Salad Plain","Natural Cut French Fry","P,B, & J","Potato Chips Individual","Pretzels","Southwest Chicken Salad","Vegan Nugget"]}]},{"meal_name":"Dinner","meal_avail":true,"genres":[{"genre_name":"Entrees","items":["Boneless Turkey Breast","Grilled Pork Chops","Manicotti","Salmon With Cucumber Salsa","Sweet And Spicy Garbanzo Stew","Vegan Stuffed Zucchini Boats"]},{"genre_name":"Sauces & Gravies","items":["Cucumber Salsa"]},{"genre_name":"Starch & Potatoes","items":["Bread Dressing","Fresh Mashed Potatoes"]},{"genre_name":"Veggies","items":["Baby Carrots With Green Beans","Grilled Asparagus"]},{"genre_name":"Desserts","items":["German Chocolate Layer Cake","Pecan Pie"]},{"genre_name":"Bakery Misc","items":["Brioche Cheese Dinner Roll","Garlic & Herb Dinner Roll"]},{"genre_name":"Cook To Order Bar","items":["Baked Brown Rice","Chicken Stir Fry W/ Plum Sauce","Fried Rice With Egg","Pasta Vegetable Side","Sesame Beef","Tofu And Wild Mushroom Stir Fry","Variety Of Sushi","Vegetable Egg Roll","Vegetable Lo Mein"]}]},{"meal_name":"Knight Room","meal_avail":true,"genres":[{"genre_name":"Knight Room","items":["12\" Flour Tortilla","Beef Burrito","Burrito Bar Fillings","Chicken Burrito Meat","Pinto Beans Seasoned","Pork Burrito","Sauteed Onion & Peppers","Seasoned Black Beans","Spanish Rice"]}]},{"meal_name":"Late Knight","meal_avail":false}]
     */
    @SerializedName("location_name")
    private String location_name;

    @SerializedName("date")
    private long date;

    @SerializedName("meals")
    private List<MealsBean> meals;

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public List<MealsBean> getMeals() {
        return meals;
    }

    public void setMeals(List<MealsBean> meals) {
        this.meals = meals;
    }

    public static class MealsBean {
        /**
         * meal_name : Breakfast
         * meal_avail : true
         * genres : [{"genre_name":"Breakfast Meats","items":["Grilled Turkey Sausage Links","Vegan Breakfast Patties"]},{"genre_name":"Breakfast Entrees","items":["Belgian Waffles","Hard Boiled Eggs","Potato Pancakes","Scramble Cholesterol Free Egg","Scrambled Eggs","Waffle Red Velvet"]},{"genre_name":"Breakfast Bakery","items":["Assorted Bagels","Blueberry Crumb Cake","Cheese Topped Cinnamon Bun","Glazed Cake Donut","Muffin Apple Cinnamon Raisin"]},{"genre_name":"Breakfast Misc","items":["Green Seedless Grapes","Honeydew Melon","Oatmeal","Vegan Grits Hot Cereal","Watermelon"]}]
         */

        private String meal_name;
        private boolean meal_avail;
        private List<GenresBean> genres;

        public String getMeal_name() {
            return meal_name;
        }

        public void setMeal_name(String meal_name) {
            this.meal_name = meal_name;
        }

        public boolean isMeal_avail() {
            return meal_avail;
        }

        public void setMeal_avail(boolean meal_avail) {
            this.meal_avail = meal_avail;
        }

        public List<GenresBean> getGenres() {
            return genres;
        }

        public void setGenres(List<GenresBean> genres) {
            this.genres = genres;
        }

        public static class GenresBean {
            /**
             * genre_name : Breakfast Meats
             * items : ["Grilled Turkey Sausage Links","Vegan Breakfast Patties"]
             */

            private String genre_name;
            private List<String> items;

            public String getGenre_name() {
                return genre_name;
            }

            public void setGenre_name(String genre_name) {
                this.genre_name = genre_name;
            }

            public List<String> getItems() {
                return items;
            }

            public void setItems(List<String> items) {
                this.items = items;
            }
        }
    }
}
