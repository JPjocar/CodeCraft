/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cosmos.CodeCraft.Tests;

import java.nio.file.Paths;
import org.modelmapper.ModelMapper;

/**
 *
 * @author Cosmos
 */
public class MainTests {
    public static void main(String[] args) {
        ModelMapper mopMapper = new ModelMapper();
        System.out.println(mopMapper.map(new Uno(), Dos.class));
        System.out.println(Paths.get("uploads"));
    }
}
