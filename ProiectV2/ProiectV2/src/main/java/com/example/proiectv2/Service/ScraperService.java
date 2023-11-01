package com.example.proiectv2.Service;

import com.example.proiectv2.Entity.Company;
import com.example.proiectv2.Repository.CompanyRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScraperService {

    private static Integer uid = 0;

    public List<String> scrapeWebsitesFromCSV(String csvFile, int colIndex) throws IOException, CsvValidationException {
        List<String> websites = new ArrayList<>();
        boolean skipHeader = true;

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                if (nextLine.length > colIndex) {
                    String website = nextLine[colIndex];
                    if (!website.startsWith("http://") && !website.startsWith("https://")) {
                        website = "https://" + website;
                    }
                    websites.add(website);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return websites;
    }

    public List<String> scrapeCompaniesFromCSV(String csvFile, int colIndex) throws IOException, CsvValidationException {
        List<String> companies = new ArrayList<>();
        boolean skipHeader = true;

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                if (nextLine.length > colIndex) {
                    String company = nextLine[colIndex];

                    companies.add(company);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return companies;
    }

    public List<Company> fetchDataFromWebsites(List<String> websites, List<String> companyNames) {
        List<Company> companies = new ArrayList<>();

        for (int i = 0; i < websites.size(); i++) {
            String website = websites.get(i);
            String companyName = companyNames.get(i);

            try {
                System.out.println("Fetching content from: " + website);
                Document doc = Jsoup.connect(website).get();

                List<String> phoneNumbers = extractPhoneNumbers(doc.html());
                List<String> addresses = extractAddresses(doc);
                List<String> socialMediaLinks = extractSocialMediaLinks(doc);

                Company company = new Company();
                company.setWebsite(website);
                company.setName(companyName);

                company.setPhoneNumber(phoneNumbers.isEmpty() ? "NA" : String.join(", ", phoneNumbers));
                company.setAddress(addresses.isEmpty() ? "NA" : String.join(", ", addresses));
                company.setSocialMediaWebsite(socialMediaLinks.isEmpty() ? "NA" : String.join(", ", socialMediaLinks));
                company.setId(uid++);

                String aboutPageURL = website + "/about";


                int numColumns = 5;
                int numNonNullColumns = 0;
                if (!"NA".equals(company.getName())) numNonNullColumns++;
                if (!"NA".equals(company.getWebsite())) numNonNullColumns++;
                if (!"NA".equals(company.getPhoneNumber())) numNonNullColumns++;
                if (!"NA".equals(company.getAddress())) numNonNullColumns++;
                if (!"NA".equals(company.getSocialMediaWebsite())) numNonNullColumns++;

                company.setFillRate((double) numNonNullColumns / numColumns * 100);
                System.out.println("Fillrate: "+ company.getFillRate());
                companies.add(company);

                System.out.println("Data to be saved: " + company);
            } catch (java.net.UnknownHostException ex) {
                System.err.println("Error: Unable to resolve the host for " + website);
            } catch (IOException ex) {
                System.err.println("Error: An IOException occurred while connecting to " + website);
                ex.printStackTrace();
            }
        }

        return companies;
    }

    private List<String> extractPhoneNumbers(String text) {
        List<String> phoneNumbers = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b(?:(?:\\+?\\d{1,3}[-.\\s]?)?(?:\\(\\d{1,4}\\)[-\\s]?)?\\d{2,4}[-.\\s]?\\d{2,4}[-.\\s]?\\d{2,4})\\b");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            phoneNumbers.add(matcher.group());
        }

        return phoneNumbers;
    }

    private List<String> extractAddresses(Document document) {
        List<String> addresses = new ArrayList<>();
        Elements addressElements = document.select("address");

        for (Element addressElement : addressElements) {
            addresses.add(addressElement.text());
        }

        return addresses;
    }



    private List<String> extractSocialMediaLinks(Document document) {
        List<String> socialMediaLinks = new ArrayList<>();
        Elements socialMediaElements = document.select("a[href*=facebook.com], a[href*=twitter.com], a[href*=linkedin.com]");

        for (Element socialMediaElement : socialMediaElements) {
            socialMediaLinks.add(socialMediaElement.attr("href"));
        }

        return socialMediaLinks;
    }

    public void saveDataToCSV(List<Company> companies, String csvFileName) {
        try (FileWriter fileWriter = new FileWriter(csvFileName);
             CSVWriter csvWriter = new CSVWriter(fileWriter)) {

            String[] header = {"Name", "Website", "Phone Number", "Address", "Social Media Website"};
            csvWriter.writeNext(header);

            for (Company company : companies) {
                String[] data = {company.getName(), company.getWebsite(), company.getPhoneNumber(), company.getAddress(), company.getSocialMediaWebsite()};
                csvWriter.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Company> scrapeWebsitesFromCSVAndReturnData(String csvFile, int colIndex, int colIndexName) throws IOException, CsvValidationException {
        List<String> websites = scrapeWebsitesFromCSV(csvFile, colIndex);
        List<String> names = scrapeCompaniesFromCSV(csvFile, colIndexName);
        List<Company> companies = fetchDataFromWebsites(websites, names);
        indexScrapedCompanies(companies);
        return companies;
    }

    @Autowired
    private CompanyRepo companyRepo;

    public void indexScrapedCompanies(List<Company> companies) {
        for (Company company : companies) {
            companyRepo.save(company);
        }
    }
}
