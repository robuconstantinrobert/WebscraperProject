package com.example.proiectv2.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "companies")
public class Company {

    @Id
    private int id;
    @Field(type = FieldType.Text, name = "name")
    private String name;
    @Field(type = FieldType.Text, name = "website")
    private String website;
    @Field(type = FieldType.Text, name = "phoneNumber")
    private String phoneNumber;
    @Field(type = FieldType.Text, name = "address")
    private String address;
    @Field(type = FieldType.Text, name = "socialMediaWebsite")
    private String socialMediaWebsite;

    private double fillRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialMediaWebsite() {
        return socialMediaWebsite;
    }

    public void setSocialMediaWebsite(String socialMediaWebsite) {
        this.socialMediaWebsite = socialMediaWebsite;
    }

    public double getFillRate() {
        return fillRate;
    }

    public void setFillRate(double fillRate) {
        this.fillRate = fillRate;
    }
}
