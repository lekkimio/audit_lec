package com.example.auditlec.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserHistoryResponseDto {
    private Long id;
    private String revisionType;
    private String name;
    private String email;
    private AddressDto address;
    private List<AddressDto> shippingAddresses;
}