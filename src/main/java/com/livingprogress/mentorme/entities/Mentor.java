package com.livingprogress.mentorme.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * The mentor.
 */
@Getter
@Setter
@Entity
public class Mentor extends InstitutionUser {
    /**
     * The mentee mentor programs .
     */
    @JsonIgnore
    @OneToMany(mappedBy = "mentor")
    private List<MenteeMentorProgram> menteeMentorPrograms;

    /**
     * The professional experience.
     */
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfessionalExperienceData> professionalExperiences;

    /**
     * The mentor type.
     */
    @Enumerated(EnumType.STRING)
    private MentorType mentorType;

    /**
     * The professional areas.
     */
    @ManyToMany
    @JoinTable(name = "mentor_professional_area",
            joinColumns = {@JoinColumn(name = "mentor_id")},
            inverseJoinColumns = {@JoinColumn(name = "professional_area_id")})
    private List<ProfessionalConsultantArea> professionalAreas;

    /**
     * The company name.
     */
    private String companyName;

    /**
     * The distance.
     */
    @Transient
    public Double distance;

    /**
     * The linkedin url.
     */
    private String linkedInUrl;


}

