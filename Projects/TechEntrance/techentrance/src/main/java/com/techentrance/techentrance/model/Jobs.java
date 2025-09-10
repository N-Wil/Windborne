package com.techentrance.techentrance.model;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "jobs")
public class Jobs {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "jobid", updatable = false, nullable = false, columnDefinition = "VARCHAR(100)")
    @Type(type = "uuid-char")
    private UUID jobId;

    private String contents;

    private String name;

    private String location;

    private String jobType; //intern, FT

    private String companyName;

    @Column(name = "session_id", updatable = true, nullable = true, columnDefinition = "VARCHAR(100)")
    @Type(type = "uuid-char")
    private UUID sessionId;

    public Jobs() {

    }

    public Jobs(UUID jobId, String contents, String name, String location, String jobType, String companyName, UUID sessionId) {
        this.jobId = jobId;
        this.contents = contents;
        this.name = name;
        this.location = location;
        this.jobType = jobType;
        this.companyName = companyName;
        this.sessionId = sessionId;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }
}
