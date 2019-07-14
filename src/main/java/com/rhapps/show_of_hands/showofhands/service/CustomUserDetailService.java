package com.rhapps.show_of_hands.showofhands.service;

import com.rhapps.show_of_hands.showofhands.model.Usermodels.CustomUserDetails;
import com.rhapps.show_of_hands.showofhands.model.Usermodels.Users;
import com.rhapps.show_of_hands.showofhands.repository.UsersRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UsersRepository usersRepository;

    public CustomUserDetailService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Users> optionalUsers = usersRepository.findByUsername(username);

//        List<SimpleGrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(“user”));
        optionalUsers.orElseThrow(()-> new UsernameNotFoundException("USername not found"));
        return optionalUsers.map(CustomUserDetails::new).get();
    }
}
