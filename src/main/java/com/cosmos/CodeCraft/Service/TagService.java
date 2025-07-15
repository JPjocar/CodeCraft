/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Service;

import com.cosmos.CodeCraft.Dto.TagResponseDTO;
import com.cosmos.CodeCraft.Dto.TagsCreationDTO;
import com.cosmos.CodeCraft.Entity.TagEntity;
import com.cosmos.CodeCraft.Exception.ResourceNotFoundException;
import com.cosmos.CodeCraft.Repository.TagRepository;

import java.util.List;
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
            throw new IllegalArgumentException("el tag ya existe");
        }
        ModelMapper modelMapper = new ModelMapper();
        TagEntity tagEntity = modelMapper.map(tagsCreationDTO, TagEntity.class);
        this.tagRepository.save(tagEntity);
        return mapToResponse(tagEntity);
    }
    
    //Verificar logica
    @Transactional
    public TagResponseDTO update(Long id, TagsCreationDTO tagsCreationDTO){
        //Verificar que el tag no exista
        TagEntity tagActual = this.tagRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Tag", "id", id) );
        Optional<TagEntity> tagOptional = this.tagRepository.findTagEntityByName(tagsCreationDTO.getName());
        if( tagOptional.isPresent() && !tagActual.getName().equalsIgnoreCase(tagsCreationDTO.getName()) ){
            throw new IllegalArgumentException("El tag ya existe en los registros");
        }
        tagActual.setName(tagsCreationDTO.getName());
        tagActual.setDescription(tagsCreationDTO.getDescription());
        this.tagRepository.save(tagActual);
        return mapToResponse(tagActual);
    }
    
    
    public String delete(Long tag_id){
        this.tagRepository.findById(tag_id).orElseThrow( () -> new ResourceNotFoundException("Tag", "id", tag_id));
        this.tagRepository.deleteById(tag_id);
        return "Tag removed!";
    }

    public List<TagResponseDTO> getAll(){
        List<TagEntity> tags = this.tagRepository.findAll();
        return tags.stream().map(this::mapToResponse).toList();
    }

    private TagResponseDTO mapToResponse(TagEntity tagEntity){
        ModelMapper modelMapper = new ModelMapper();
        TagResponseDTO tagResponseDTO = modelMapper.map(tagEntity, TagResponseDTO.class);
        return tagResponseDTO;
    }
    
}
