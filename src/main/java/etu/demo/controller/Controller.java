package etu.demo.controller;

import etu.demo.domain.Phrase;
import etu.demo.repository.MotAmbiguRepository;
import etu.demo.repository.PhraseRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/visiteur")
@AllArgsConstructor
@NoArgsConstructor
public class Controller {


    @Autowired
    private PhraseRepository phraseRepository;
    @Autowired
    private MotAmbiguRepository motAmbiguRepository;

    @GetMapping("/phrase/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    private Phrase getPhrase(@PathVariable("id") int id)
    {
        Phrase phrase = phraseRepository.getById(id);
        return phrase;
    }

    @PostMapping("/add")

    private void addPhrase()
    {
        Phrase phrase = new Phrase();
        phrase.setPhrase("fsjgfhgfsdfgh");
      phraseRepository.save(phrase);
    }

}
