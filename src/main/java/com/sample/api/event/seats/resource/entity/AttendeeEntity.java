package com.sample.api.event.seats.resource.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity object for a seat.
 */
@Entity
@Table(name = "attendee")
public class AttendeeEntity implements Serializable {
    private static final long serialVersionUID = 528729541274912208L;
    private Long id;
    private String firstName;
    private String lastName;
    private String company;
    private String title;
    private String bio;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    @Column
    public void setCompany(String company) {
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
