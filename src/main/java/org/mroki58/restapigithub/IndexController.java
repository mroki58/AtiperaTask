package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.mroki58.restapigithub.exception.MissingUsernameException;
import org.mroki58.restapigithub.exception.NoSuchUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;


@RestController
public class IndexController {

    private final GithubService githubService;

    IndexController(GithubService githubService) {
        this.githubService = githubService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> index(@RequestParam(required = false) String username) {
        if(username == null || username.isEmpty()) {
            throw new MissingUsernameException();
        }

        List<RepositoryWithBranchDto> data;
        try {
            data = githubService.getPublicRepositoriesWithBranches(username);
        }catch(HttpClientErrorException.NotFound e) {
            throw new NoSuchUserException(username);
        }
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
