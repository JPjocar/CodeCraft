/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Controller;

import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Dto.TagsCreationDTO;
import com.cosmos.CodeCraft.Service.TagService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tag")
public class TagController {
    
    @Autowired
    private TagService tagService;

    @GetMapping
    public List<TagResponseDTO> getAll(){
        return this.tagService.getAll();
    }

    @PostMapping
    public TagResponseDTO create(@RequestBody @Valid TagsCreationDTO tagsCreationDTO){
        return this.tagService.create(tagsCreationDTO);
    }
}
