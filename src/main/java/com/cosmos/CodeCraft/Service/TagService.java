/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Dto.TagsCreationDTO;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Repository.TagRepository;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagService {
    
    @Autowired
    private TagRepository tagRepository;
    
    
    @Transactional
    public TagResponseDTO create(TagsCreationDTO tagsCreationDTO){
        //Verificar que el tag no exista 
        Optional<TagEntity> tagOptional = this.tagRepository.findTagEntityByName(tagsCreationDTO.getName());
        if(tagOptional.isPresent()){
            throw new IllegalArgumentException("El tag ya existe");
        }
        ModelMapper modelMapper = new ModelMapper();
        TagEntity tagEntity = modelMapper.map(tagsCreationDTO, TagEntity.class);
        this.tagRepository.save(tagEntity);
        return modelMapper.map(tagEntity, TagResponseDTO.class);
    }
    
    //Verificar logica
    @Transactional
    public TagResponseDTO update(Long id, TagsCreationDTO tagsCreationDTO){
        //Verificar que el tag no exista
        TagEntity tagActual = this.tagRepository.findById(id).orElseThrow();
        Optional<TagEntity> tagOptional = this.tagRepository.findTagEntityByName(tagsCreationDTO.getName());
        if( tagOptional.isPresent() && !tagActual.getName().equalsIgnoreCase(tagsCreationDTO.getName()) ){
            throw new IllegalArgumentException("El tag ya existe en los registros");
        }
        ModelMapper modelMapper = new ModelMapper();
        tagActual = modelMapper.map(tagsCreationDTO, TagEntity.class);
        return modelMapper.map(tagActual, TagResponseDTO.class);
    }
    
}
