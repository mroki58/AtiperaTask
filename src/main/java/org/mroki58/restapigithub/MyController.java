package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.mroki58.restapigithub.utils.FilterUtils.filterNotForks;

@RestController
public class MyController {

    private final String username = "mroki58";
    private final MyService service;

    MyController(MyService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<Object> index() {

        List<RepositoryDto> repositories = service.getAllUsersRepositories(username);
        List<RepositoryDto> notForks = filterNotForks(repositories);

        List<RepositoryWithBranchDto> response = new ArrayList<>();

        for(RepositoryDto repo: notForks) {
            List<BranchDto> branches  = service.getBranchesForRepository(username, repo.getName());
            List<RepositoryWithBranchDto.Branch> br = new ArrayList<>();
            for(var branch: branches) {
                br.add(new RepositoryWithBranchDto.Branch(branch.getName(), branch.getCommit().getSha()));
            }
            response.add(new RepositoryWithBranchDto(repo.getName(), repo.getOwner().getLogin(), br));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
