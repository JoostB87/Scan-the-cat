package com.catapp.scanthecat;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

class Cat implements Serializable {

    private String length;
    private String origin;
    private String image_link;
    private Integer family_friendly;
    private Integer shedding;
    private Integer general_health;
    private Integer playfulness;
    private Integer meowing;
    private Integer children_friendly;
    private Integer stranger_friendly;
    private Integer grooming;
    private Integer intelligence;
    private Integer other_pets_friendly;
    private Float min_weight;
    private Float max_weight;
    private Float min_life_expectancy;
    private Float max_life_expectancy;
    private String name;

    public Cat() {
    }

    public Cat(String length, String origin, String image_link, Integer family_friendly, Integer shedding, Integer general_health, Integer playfulness, Integer meowing, Integer children_friendly, Integer stranger_friendly, Integer grooming, Integer intelligence, Integer other_pets_friendly, Float min_weight, Float max_weight, Float min_life_expectancy, Float max_life_expectancy, String name) {
        this.length = length;
        this.origin = origin;
        this.image_link = image_link;
        this.family_friendly = family_friendly;
        this.shedding = shedding;
        this.general_health = general_health;
        this.playfulness = playfulness;
        this.meowing = meowing;
        this.children_friendly = children_friendly;
        this.stranger_friendly = stranger_friendly;
        this.grooming = grooming;
        this.intelligence = intelligence;
        this.other_pets_friendly = other_pets_friendly;
        this.min_weight = min_weight;
        this.max_weight = max_weight;
        this.min_life_expectancy = min_life_expectancy;
        this.max_life_expectancy = max_life_expectancy;
        this.name = name;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public Integer getFamily_friendly() {
        return family_friendly;
    }

    public void setFamily_friendly(Integer family_friendly) {
        this.family_friendly = family_friendly;
    }

    public Integer getShedding() {
        return shedding;
    }

    public void setShedding(Integer shedding) {
        this.shedding = shedding;
    }

    public Integer getGeneral_health() {
        return general_health;
    }

    public void setGeneral_health(Integer general_health) {
        this.general_health = general_health;
    }

    public Integer getPlayfulness() {
        return playfulness;
    }

    public void setPlayfulness(Integer playfulness) {
        this.playfulness = playfulness;
    }

    public Integer getMeowing() {
        return meowing;
    }

    public void setMeowing(Integer meowing) {
        this.meowing = meowing;
    }

    public Integer getChildren_friendly() {
        return children_friendly;
    }

    public void setChildren_friendly(Integer children_friendly) {
        this.children_friendly = children_friendly;
    }

    public Integer getStranger_friendly() {
        return stranger_friendly;
    }

    public void setStranger_friendly(Integer stranger_friendly) {
        this.stranger_friendly = stranger_friendly;
    }

    public Integer getGrooming() {
        return grooming;
    }

    public void setGrooming(Integer grooming) {
        this.grooming = grooming;
    }

    public Integer getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(Integer intelligence) {
        this.intelligence = intelligence;
    }

    public Integer getOther_pets_friendly() {
        return other_pets_friendly;
    }

    public void setOther_pets_friendly(Integer other_pets_friendly) {
        this.other_pets_friendly = other_pets_friendly;
    }

    public Float getMin_weight() {
        return min_weight;
    }

    public void setMin_weight(Float min_weight) {
        this.min_weight = min_weight;
    }

    public Float getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(Float max_weight) {
        this.max_weight = max_weight;
    }

    public Float getMin_life_expectancy() {
        return min_life_expectancy;
    }

    public void setMin_life_expectancy(Float min_life_expectancy) {
        this.min_life_expectancy = min_life_expectancy;
    }

    public Float getMax_life_expectancy() {
        return max_life_expectancy;
    }

    public void setMax_life_expectancy(Float max_life_expectancy) {
        this.max_life_expectancy = max_life_expectancy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class CatDetail implements Serializable {

    private String name;
    private Taxonomy taxonomy;
    private List locations;
    private Chars characteristics;

    public CatDetail() {
    }

    public CatDetail(String name, Taxonomy taxonomy, List locations, Chars characteristics) {
        this.name = name;
        this.taxonomy = taxonomy;
        this.locations = locations;
        this.characteristics = characteristics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(Taxonomy taxonomy) {
        this.taxonomy = taxonomy;
    }

    public List getLocations() {
        return locations;
    }

    public void setLocations(List locations) {
        this.locations = locations;
    }

    public Chars getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Chars characteristics) {
        this.characteristics = characteristics;
    }
}

class Taxonomy implements Serializable {

    private String kingdom;
    private String phylum;
    private String clas;
    private String order;
    private String family;
    private String genus;
    private String scientific_name;

    public Taxonomy() {
    }

    public Taxonomy(String kingdom, String phylum, String clas, String order, String family, String genus, String scientific_name) {
        this.kingdom = kingdom;
        this.phylum = phylum;
        this.clas = clas;
        this.order = order;
        this.family = family;
        this.genus = genus;
        this.scientific_name = scientific_name;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getPhylum() {
        return phylum;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public String getClas() {
        return clas;
    }

    public void setClass(String clas) {
        clas = clas;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }
}

class Chars implements Serializable {

    private String slogan;
    //ToDo add more

    public Chars() {
    }

    public Chars(String slogan) {
        this.slogan = slogan;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }
}