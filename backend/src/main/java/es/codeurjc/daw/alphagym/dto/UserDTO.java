package es.codeurjc.daw.alphagym.dto;

import java.util.List;

public record UserDTO( 
    Long id,
    String name,
    List<String> roles,
    String email) {}


