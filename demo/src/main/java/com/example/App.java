package com.example;

import java.io.IOException;

public class App{
    public static void main(String[] args) throws IOException{
        /**Using described page scraping function */
        PageScraper ps = new PageScraper("F:/rychkov_project/demo/resources/input.txt","F:/rychkov_project/demo/resources/output.txt");
        ps.PageScraper();
    }
}



    

    


