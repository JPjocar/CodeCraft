/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Tests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.modelmapper.ModelMapper;

/**
 *
 * @author Cosmos
 */
public class MainTests {
    public static void main(String[] args) {
        System.out.println(Files.exists(Paths.get("src")));
    }
}
