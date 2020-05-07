package com.service.generatexls.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
public class User   {
    private String firstName;
    private String lastName;

    public String getFullName() {
        String firstLastName = firstName + " " + lastName;
        return firstLastName;
    }


}
