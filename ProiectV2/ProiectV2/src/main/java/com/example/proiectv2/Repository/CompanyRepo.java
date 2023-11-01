package com.example.proiectv2.Repository;

import com.example.proiectv2.Entity.Company;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CompanyRepo extends ElasticsearchRepository<Company, Integer> {
}
