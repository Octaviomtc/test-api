package com.alfredo.test.apirest.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class MapperUtil {

    private ObjectMapper mapperSingleton = null;

    public ObjectMapper getMapper(){
        return (mapperSingleton == null) ? new ObjectMapper() : mapperSingleton;
    }
    
}
