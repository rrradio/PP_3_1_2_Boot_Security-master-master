package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void  setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if(user == null) {
            throw  new UsernameNotFoundException(String.format("User %s not found", username));
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()) );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }

    
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user, String[] selectedRoles) {
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        Set<Role> roles = new HashSet<>();
        Arrays.stream(selectedRoles).forEach(a -> roles.add(roleRepository.findRoleByName(a)));
        user.setRoles(roles);

        userRepository.save(user);
    }


    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        User user = getUsers().stream().filter(u -> Objects.equals(u.getId(), id))
                .findFirst().orElse(null);
        if (user == null) {
            throw new NullPointerException("Пользователь с указанным id не найден");
        }
        return user;
    }

    @Transactional
    public void update(Long id, User user, String[] selectedRoles) {
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);
        User neew = getUserById(id);
        neew.setUsername(user.getUsername());
        neew.setAge(user.getAge());
        neew.setEmail(user.getEmail());
        neew.setPassword(user.getPassword());
        Set<Role> roles = new HashSet<>();
        Arrays.stream(selectedRoles).forEach(x -> roles.add(roleRepository.findRoleByName(x)));
        neew.setRoles(roles);
        userRepository.save(neew);
    }
}
