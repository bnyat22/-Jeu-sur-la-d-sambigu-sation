package etu.demo.controller;

import etu.demo.domain.Phrase;
import etu.demo.repository.PhraseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("vis/")
public class VisController {

    @Autowired
    private PhraseRepository phraseRepository;

    @GetMapping("/getPhrases")
    public List<Phrase> getAll()
    {
        return phraseRepository.findAll();
    }
}
