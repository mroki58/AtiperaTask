package org.mroki58.restapigithub;

import org.mroki58.restapigithub.dto.BranchDto;
import org.mroki58.restapigithub.dto.RepositoryDto;
import org.mroki58.restapigithub.dto.RepositoryWithBranchDto;
import org.mroki58.restapigithub.exception.MissingUsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Object> index(@RequestParam(required = false) String username) {
        if(username == null || username.isEmpty()) {
            throw new MissingUsernameException();
        }

        List<RepositoryWithBranchDto> data = service.getPublicRepositoriesWithBranches(username);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
