package com.alfredo.test.apirest.entities;

import java.util.List;

import com.alfredo.test.apirest.models.Movement;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseConsultingMovements {
    public Integer statusCode;
    public String message;
    public String account;
    public List<Movement> movements;
}
