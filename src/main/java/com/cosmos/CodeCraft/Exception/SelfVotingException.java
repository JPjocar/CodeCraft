package com.cosmos.CodeCraft.Exception;

public class SelfVotingException extends RuntimeException{
    public SelfVotingException(String message){
        super(message);
    }
}
