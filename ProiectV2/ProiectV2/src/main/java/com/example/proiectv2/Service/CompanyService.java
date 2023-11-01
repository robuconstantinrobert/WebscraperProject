package com.example.proiectv2.Service;

import com.example.proiectv2.Entity.Company;
import com.example.proiectv2.Repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepo;

    public Iterable<Company> getCompanies(){
        return companyRepo.findAll();
    }

    public Company insertCompany(Company company){
        return companyRepo.save(company);
    }

    public Company updateCompany(Company company, int id){
        Company company1 = companyRepo.findById(id).get();
        company1.setWebsite(company.getWebsite());
        return company1;
    }

    public void deleteCompany(int id){
        companyRepo.deleteById(id);
    }


}
