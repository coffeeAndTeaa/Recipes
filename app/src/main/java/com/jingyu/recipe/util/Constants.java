package com.jingyu.recipe.util;

public class Constants {

    public static final String BaseUrl = "https://recipesapi.herokuapp.com";

    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 10; // 10 seconds
    public static final int WRITE_TIMEOUT = 10; // 10 seconds

    public static final String API_KEY = "";

    public static final int NETWORK_TIMEOUT = 3000;

    public static final long RECIPE_REFRESH_TIME = 60 * 60 * 24 * 30; // 30 days to refresh recipe

    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "barbeque",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };

}
