package com.project.service;

import com.project.dto.UserInfoResponseDto;
import com.project.dto.UserSaveRequestDto;
import com.project.dto.UserUpdateRequestDto;
import com.project.dto.UserUpdateResponseDto;
import com.project.entity.UserEntity;
import com.project.exception.BusinessException;
import com.project.mapper.UserMapper;
import com.project.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    //private final MailClient mailClient;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void save(UserSaveRequestDto userSaveRequestDto){
        try {
            UserEntity userEntity = userMapper.userSaveRequestDtoToUserEntity(userSaveRequestDto);
            userRepository.save(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException("A user with the given details already exists");
        } catch (Exception e) {
            throw new BusinessException("An error occurred while saving the user");
        }
    }

    @Transactional
    public UserUpdateResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found with id: " + id));

        userMapper.updateUserFromDto(userUpdateDto, existingUser);

        UserEntity updatedUser = userRepository.save(existingUser);

        //mailClient.sendSmsToUpdateUser(existingUser); TODO BURASI kafka ile entegre edilecek.

        return userMapper.userEntityToUserUpdateResponseDto(updatedUser);

    }

    public UserInfoResponseDto info(Long userId){
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        UserEntity userEntity = optionalUserEntity.orElseThrow(BusinessException::new);
        return userMapper.userToInfoResponseDto(userEntity);
    }

    public UserInfoResponseDto infoByPhoneNumber(String phoneNumber){
        Optional<UserEntity> optionalUserEntity = userRepository.findByPhoneNumber(phoneNumber);
        UserEntity userEntity = optionalUserEntity.orElseThrow(BusinessException::new);
        return userMapper.userToInfoResponseDto(userEntity);
    }

    public Optional<UserEntity> findUserById(Long id){
        return userRepository.findById(id);
    }
}
