package org.mroki58.restapigithub.dto;

import lombok.Data;

@Data
public class BranchDto {
    private String name;
    private Commit commit;

    @Data
    public static class Commit{
        private String sha;
    }
}
