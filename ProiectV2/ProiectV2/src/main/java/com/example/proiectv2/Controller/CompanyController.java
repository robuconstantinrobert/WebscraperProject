package com.example.proiectv2.Controller;


import com.example.proiectv2.Entity.Company;
import com.example.proiectv2.Service.CompanyService;
import com.example.proiectv2.Service.ScraperService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/apis")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ScraperService scraperService;

    @GetMapping("/findAll")
    Iterable<Company> findAll(){
        return companyService.getCompanies();
    }

    @PostMapping("/insert")
    public Company insertCompany(@RequestBody Company company){
        return companyService.insertCompany(company);
    }


    @GetMapping("/totalWebsites")
    public ResponseEntity<Integer> getTotalWebsitesCount() throws CsvValidationException {
        String csvFile = "E:/sample-websites-company-names.csv";
        int colIndex = 0;
        int totalWebsites = 0;

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;
            boolean skipHeader = true;

            while ((nextLine = reader.readNext()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                if (nextLine.length > colIndex) {
                    totalWebsites++;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        return ResponseEntity.ok(totalWebsites);
    }

    @PostMapping("/scrape")
    public List<Company> scrapeWebsites() throws IOException, CsvValidationException {
        String csvFile = "E:/sample-websites-company-names.csv";
        int colIndex = 0;
        int colIndexName = 1;
        List<String> websites = scraperService.scrapeWebsitesFromCSV(csvFile, colIndex);
        List<String> names = scraperService.scrapeCompaniesFromCSV(csvFile, colIndexName);
        List<Company> companies = scraperService.fetchDataFromWebsites(websites, names);

        scraperService.indexScrapedCompanies(companies);
        String outputCsvFile = "E:/data.csv";
        scraperService.saveDataToCSV(companies, outputCsvFile);

        return companies;
    }
}