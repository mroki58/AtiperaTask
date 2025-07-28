package org.mroki58.restapigithub.utils;

import org.mroki58.restapigithub.dto.RepositoryDto;

import java.util.List;
import java.util.stream.Collectors;


public class FilterUtils {
    static public List<RepositoryDto> filterNotForks(List<RepositoryDto> repositories) {
        return repositories.stream()
                .filter(repo -> Boolean.FALSE.equals(repo.getFork()))
                .collect(Collectors.toList());
    }

}
