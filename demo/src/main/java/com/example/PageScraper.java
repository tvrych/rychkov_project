package com.example;

import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.* ;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.regex.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

class PageScraper {
    static String s1;
    static String s2;
    PageScraper(String s1, String s2){  
        PageScraper.s1=s1;
        PageScraper.s2=s2;
    }
    /**Creating Page Scraper function*/
    static void PageScraper() throws IOException {    
        ArrayList<String> imglist = new ArrayList<>();  // list for image urls
        ArrayList<String> titlelist = new ArrayList<>();   // list for article titles
        ArrayList<String> urllist = new ArrayList<>();         // list for article urls 
        ArrayList<String> list = new ArrayList<>();               //list for final output
        /**Creating regex pattern for img urls*/
        Pattern imgp = Pattern.compile("http(s?)://([\\w-]+\\.)"    
                                    +"[\\w-]+(/[\\w- ./]*)"
                                    +"\\.(?:[gG][iI][fF]|[jJ][pP][gG]|[jJ][pP][eE][gG]|[pP][nN][gG]|[bB][mM][pP])"); 
        /** Creating regex pattern for empty string*/
        Pattern titlep = Pattern.compile("^[ \t\n]*$");             
        /** Creating regex pattern for urls*/
        Pattern urlp=Pattern.compile("((http|https)://)(www.)?"     
                                    + "[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]" 
                                    + "{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)");
        Matcher m1,m2,m3;                            
        Path path = Paths.get(s1);     //Getting input text path
        List<String> pathlist;
        String pl;
        try 
        {
            pathlist = Files.readAllLines(path); //Reading lines from input text and putting them into a list
            pl=pathlist.get(0);                //Going to limit myself with one function, which is first in the list
            /**Connecting Jsoup library to start page scrapping */
            Document mydoc = (Document) Jsoup.connect(pl).timeout(6000).get();
            /**Selecting class that indicates article list */
            org.jsoup.select.Elements body = ((Element) mydoc).select("ul.post-list");
            /**Scraping article titles*/
            for (Element e1 : body.select("div")){
                String title = e1.select("div.post-right h5").text();
                /**Sorting out Anything that isn't text */
                m1=titlep.matcher(title);
                if(m1.matches()==false){
                    titlelist.add("Article Title: " + title + ";    "); //writing information into a list           
                }
            }    
            /**Scraping thumbnail images*/
            for (Element e2 : body.select("div")){
                String img = e2.select("div.thumbnail img").attr("src");
                /**Sorting out Anything that isn't an img url */
                m2=imgp.matcher(img);
                if(m2.matches()){
                    imglist.add("Thumbnail Url: " + img);              //writing information into a list  
                }
            }
            /**Scraping article urls*/
            for (Element e3 : body.select("div")){
                String posturl = e3.select("div.post-right a").attr("href");
                /**Sorting out Anything that isn't a url */
                m3=urlp.matcher(posturl);
                if(m3.matches()){
                    urllist.add("Post Url: " + posturl + ";    " );    //writing information into a list          
                }
            }
            /**Preparing final output */
            for(int i=0; i < titlelist.size(); i++){
                int count = titlelist.size()-i;
                /**List which gathers all scraped info */
                list.add("POST #" + count + ";    " + titlelist.get(i) /*+ "\n"*/ + urllist.get(i) /*+ "\n"*/ + imglist.get(i) + "\n");
                                                    //you may uncomment "\n" if you see it as more convinient
            }
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        /**Writing everything into an output text file */
        FileWriter fw = new FileWriter(s2);
        for (String w : list){
            fw.write(w);
        }
        fw.close();

    }
}
