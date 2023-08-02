package com.example.auditlec.service.impl;

import com.example.auditlec.model.Address;
import com.example.auditlec.model.ShippingAddress;
import com.example.auditlec.model.User;
import com.example.auditlec.model.dto.AddressDto;
import com.example.auditlec.model.dto.UserDto;
import com.example.auditlec.model.dto.UserHistoryResponseDto;
import com.example.auditlec.repo.AddressRepository;
import com.example.auditlec.repo.ShippingAddressRepository;
import com.example.auditlec.repo.UserRepository;
import com.example.auditlec.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ShippingAddressRepository shippingAddressRepository;

    @Override
    public User saveUser(UserDto user) {

        Address address = Address.builder()
                .state(user.getAddress().getState())
                .city(user.getAddress().getCity())
                .street(user.getAddress().getStreet())
                .zipCode(user.getAddress().getZipCode()).build();

        address = addressRepository.save(address);

        User newUser = User.builder()
                .name(user.getName())
                .email(user.getEmail())
                .address(address)
                .build();


        newUser.setShippingAddresses(
                saveShippingAddresses(user.getShippingAddresses(), newUser));

        return userRepository.save(newUser);
    }

    private List<ShippingAddress> saveShippingAddresses(List<AddressDto> shippingAddresses, User user) {

        List<ShippingAddress> savedShippingAddresses = new ArrayList<>();
        for (AddressDto addressDto : shippingAddresses) {
            Address address = Address.builder()
                    .state(addressDto.getState())
                    .city(addressDto.getCity())
                    .street(addressDto.getStreet())
                    .zipCode(addressDto.getZipCode()).build();
            addressRepository.save(address);
            ShippingAddress shippingAddress = ShippingAddress.builder()
                    .address(address)
                    .isActivated(false)
                    .build();

            savedShippingAddresses.add(shippingAddress);
        }
        user.setShippingAddresses(savedShippingAddresses);

        return shippingAddressRepository.saveAll(savedShippingAddresses);

    }

    @Override
    public User updateUser(Long id, UserDto user) {
        User existingUser = userRepository.findById(id).get();

        if (user.getName() != null)
            existingUser.setName(user.getName());
        if (user.getEmail() != null)
            existingUser.setEmail(user.getEmail());
        if (user.getAddress() != null) {
            Address address = existingUser.getAddress();
            if (user.getAddress().getState() != null)
                address.setState(user.getAddress().getState());
            if (user.getAddress().getCity() != null)
                address.setCity(user.getAddress().getCity());
            if (user.getAddress().getStreet() != null)
                address.setStreet(user.getAddress().getStreet());
            if (user.getAddress().getZipCode() != null)
                address.setZipCode(user.getAddress().getZipCode());
            addressRepository.save(address);
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<UserHistoryResponseDto> getUserHistory(Long id) {
        List<Revision<Long, User>> revisions = userRepository.findRevisions(id).getContent();
        List<UserHistoryResponseDto> users = new ArrayList<>();
        for (Revision<Long, User> revision : revisions) {
            users.add(UserHistoryResponseDto.builder()
                    .id(revision.getEntity().getId())
                    .revisionType(revision.getMetadata().getRevisionType().name())
                    .name(revision.getEntity().getName())
                    .email(revision.getEntity().getEmail())
                    .address(AddressDto.builder()
                            .state(revision.getEntity().getAddress().getState())
                            .city(revision.getEntity().getAddress().getCity())
                            .street(revision.getEntity().getAddress().getStreet())
                            .zipCode(revision.getEntity().getAddress().getZipCode())
                            .build())
                    .build());
        }
        return users;
    }
}
