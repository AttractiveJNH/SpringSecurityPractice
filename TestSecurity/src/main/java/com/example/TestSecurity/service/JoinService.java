package com.example.TestSecurity.service;

import com.example.TestSecurity.dto.JoinDto;
import com.example.TestSecurity.entity.UserEntity;
import com.example.TestSecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDto joinDto){

        //db에 이미 동일한 username을 가진 사람이 있는지 확인하는 메서드가 있어야 한다.
        boolean isUser = userRepository.existsByUsername(joinDto.getUsername());

        if (isUser){
            return;
        }



        UserEntity data = new UserEntity();
        data.setUsername(joinDto.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDto.getPassword())); // 비밀번호 암호화 작업
        data.setRole("ROLE_ADMIN"); // 유저객체의 역할값을 설정할 때 "ROLE_USER"



        userRepository.save(data);
    }

}
