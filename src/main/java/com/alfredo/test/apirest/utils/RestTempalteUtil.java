package com.alfredo.test.apirest.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Scope("singleton")
public class RestTempalteUtil {
    
    private RestTemplate restTemplateSingleton = null;

    public RestTemplate getRestTemplateSingleton(){
        return  (restTemplateSingleton == null) ? new RestTemplate() : restTemplateSingleton;
    }
}
