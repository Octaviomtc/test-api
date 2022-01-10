package com.alfredo.test.apirest.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import com.alfredo.test.apirest.entities.CatMovement;
import com.alfredo.test.apirest.entities.ResponseConsultingMovements;
import com.alfredo.test.apirest.models.Account;
import com.alfredo.test.apirest.models.Movement;
import com.alfredo.test.apirest.repositories.IAccountRepository;
import com.alfredo.test.apirest.repositories.IMovementRepository;
import com.alfredo.test.apirest.utils.MapperUtil;
import com.alfredo.test.apirest.utils.RestTempalteUtil;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController()
@RequestMapping("/Examen")
public class ApiTestController {
    
    @Value("${url.mockapi}")
    private String urlMockApi;

    @Autowired
    private MapperUtil mapper;
    @Autowired
    private RestTempalteUtil restTemplate;

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IMovementRepository movementRepository;

    @ApiOperation(value = "Crea una cuenta", response = Account.class, tags = "createAccount")
    @PostMapping("/create-account")
    public ResponseEntity createAccount(@Valid @RequestBody Account account) throws JsonProcessingException {
        
        Account accountSaved = accountRepository.save(account);
        
        
        mapper.getMapper().setSerializationInclusion(Include.NON_NULL);
        String json =  mapper.getMapper().writeValueAsString(Account.builder().name(account.getName()).mail(account.getMail()).build());

        HttpEntity<String> request = new HttpEntity<String>(json);
        Account aReceived = restTemplate.getRestTemplateSingleton().postForObject(urlMockApi + "account", request, Account.class);

        String acc = "";
        if(aReceived.getId()<10){
            acc = "00" + aReceived.getId();
        } else if (aReceived.getId() < 100){
            acc = "0" + aReceived.getId();
        }
        accountSaved.setAccount(aReceived.getAccount() + acc);

        accountSaved = accountRepository.save(accountSaved);

        accountSaved.setStatusCode(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(accountSaved);
    }
   
    @ApiOperation(value = "Actualiza una cuenta", response = Account.class, tags = "updateAccount")
    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@Valid @RequestBody Account account) {

        Account accountStored = accountRepository.findByAccount(account.getAccount());
        if(accountStored == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la cuenta" + account.getAccount());
        }

        if(account.getBalance() != null){
            accountStored.setBalance( account.getBalance() + accountStored.getBalance());
        }

        accountStored = accountRepository.save(accountStored);
        accountStored.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(accountStored);
    }

    @ApiOperation(value = "Obtener una cuenta", response = Account.class, tags = "getAccount")
    @GetMapping("/account/{account}")
    public ResponseEntity<?> getAccount(@PathVariable String account) {

        Account accountStored = accountRepository.findByAccount(account);
        if(accountStored == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la cuenta" + account);
        }

        accountStored.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.ok().body(accountStored);
    }
    
    @ApiOperation(value = "Crear un movimiento", response = String.class, tags = "createMovement")
    @PostMapping("/account/movement")
    public ResponseEntity<?> createMovement(@Valid @RequestBody Movement movement){
        
        if(movement.getAccount() == null){
            return ResponseEntity.badRequest().body("Es necesario que se envie un número de cuenta." + movement.getAccount());
        }

        Account account = accountRepository.findByAccount(movement.getAccount());
        if(account == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la cuenta" + movement.getAccount());
        }
        
        if(account.getBalance() >= movement.getAmount() ){
            movement.setCreateDate(new Date());
            movementRepository.save(movement);
            account.setBalance(account.getBalance() - movement.getAmount());
            accountRepository.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body("Movimiento registrado");
        } else {
            return ResponseEntity.badRequest().body("No cuentas con saldo suficiente");
        }
    }

    @ApiOperation(value = "Actualiza un movimiento", response = String.class, tags = "updateMovement")
    @PutMapping("/account/movement")
    public ResponseEntity<?> updateMovement(@Valid @RequestBody Movement movement) throws IOException{

        Movement movementStored = movementRepository.findById(movement.getId()).orElse(null);
        if(movementStored == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El movimiento a actualizar no existe");
        }

        ResponseEntity<String> response = restTemplate.getRestTemplateSingleton().getForEntity(urlMockApi + "movements-type", String.class);
        List<CatMovement> catMovements = mapper.getMapper().readValue(response.getBody(), new TypeReference<List<CatMovement>>(){});

        for( CatMovement e : catMovements){
            if(e.code.equals(movementStored.getMovementCode())){
                movementStored.setMovementCode(movement.getMovementCode());
                movementRepository.save(movementStored);

                return ResponseEntity.ok().body("Movimiento actualizado");
            }
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El tipo de movimiento no existe");
    }

    @ApiOperation(value = "Obtiene todos los movimientos de una cuenta", response = String.class, tags = "createMovement")
    @GetMapping("/account/movements/{account}")
    private ResponseEntity<?> getMovementsPerAccount(@PathVariable String account ) throws JsonParseException, JsonMappingException, IOException{
        
        Account accountStored = accountRepository.findByAccount(account);
        if(accountStored == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe la cuenta" + account);
        }

        List<Movement> movements = movementRepository.findByAccount(account);
        if( movements.size() > 0){
            movements.stream().forEach(e -> {
                ResponseEntity<String> response  = restTemplate.getRestTemplateSingleton().getForEntity(urlMockApi + "movements-type/?code=" + e.getMovementCode(), String.class);
                try {
                    List<CatMovement> catMovements = mapper.getMapper().readValue(response.getBody(), new TypeReference<List<CatMovement>>(){});
                    e.setMovementName(catMovements.get(0).getName());
                } catch (IOException e1) {
                    
                }
            });
        }

        ResponseConsultingMovements response = ResponseConsultingMovements.builder().statusCode(200).message("Movimientos de cuenta").account(account).movements(movements).build();

        return ResponseEntity.ok().body(response);

    }
    
}
