/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Exception;

/**
 *
 * @author Cosmos
 */
public class InsufficientTagsException extends RuntimeException{
    public InsufficientTagsException(int count){
        super(String.format("Required 5 tags. Is found: %s tags", count));
    }
}
