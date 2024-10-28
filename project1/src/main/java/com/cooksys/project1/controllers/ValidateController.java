package com.cooksys.project1.controllers;

import com.cooksys.project1.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

    private final ValidateService validateService;

   @GetMapping("/username/exists/@{username}")
   public boolean doesUsernameExist(@PathVariable("username") String username){ return validateService.doesUsernameExist(username); }

    @GetMapping("/username/available/@{username}")
    public boolean isUsernameAvailable(@PathVariable("username") String username){ return validateService.isUsernameAvailable(username); }

    @GetMapping("/tag/exists/{label}")
    public Boolean doesLabelExist(@PathVariable("label") String label){
        return validateService.doesTagExist(label);
    }

    @GetMapping("tag/exists/randomTag")
    public Boolean doesRandomLabelExist(){
       return validateService.randomTagExists();
    }
}
