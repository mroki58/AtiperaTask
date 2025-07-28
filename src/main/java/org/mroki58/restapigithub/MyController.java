package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.mroki58.restapigithub.exception.MissingUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MyController {

    private final GithubService githubService;

    MyController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> index(@RequestParam(required = false) String username) {
        if(username == null || username.isEmpty()) {
            throw new MissingUsernameException();
        }

        List<RepositoryWithBranchDto> data = githubService.getPublicRepositoriesWithBranches(username);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
