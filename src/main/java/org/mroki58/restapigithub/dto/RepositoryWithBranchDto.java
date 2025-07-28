package org.mroki58.restapigithub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RepositoryWithBranchDto {
    private String repositoryName;
    private String ownerLogin;
    private List<Branch> branches;

    @Data
    @AllArgsConstructor
    public static class Branch {
        String name;
        String sha;
    }

}

