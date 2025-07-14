/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Exception;

/**
 *
 * @author Cosmos
 */
public class QuestionAlreadyExistsException extends RuntimeException{
    public QuestionAlreadyExistsException(){
        super("Question already exists");
    }
}
