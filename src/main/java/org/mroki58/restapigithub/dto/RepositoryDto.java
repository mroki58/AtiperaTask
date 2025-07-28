package org.mroki58.restapigithub.dto;

import lombok.Data;

@Data
public class RepositoryDto {
    private String name;
    private Owner owner;
    private Boolean fork;

    @Data
    public static class Owner{
        public String login;
    }
}
