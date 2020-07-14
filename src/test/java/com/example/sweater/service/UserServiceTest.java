package com.example.sweater.service;

import com.example.sweater.domain.Role;
import com.example.sweater.domain.User;
import com.example.sweater.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    public void subscribe() {
        User user = new User();
        user.setUsername("John");

        User currentUser = new User();
        currentUser.setUsername("Daria");

        boolean isAdded = userService.subscribe(currentUser, user);
        Assert.assertTrue(isAdded);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void unsubscribe() {
        User user = new User();
        user.setUsername("John");

        User currentUser = new User();
        currentUser.setUsername("Daria");

        user.getSubscribers().add(currentUser);

        boolean isRemoved = userService.unsubscribe(currentUser, user);
        Assert.assertTrue(isRemoved);

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void saveUser_NewUserAdded_ReturnTrue() {
        User user = new User();
        user.setEmail("some@mail.ru");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertTrue(isUserCreated);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.eq("Activation code"),
                        ArgumentMatchers.contains("Welcome to Sweater.")
                );
    }

    @Test
    public void saveUser_UserAlreadyExists_ReturnFalse() {
        User user = new User();

        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("John");

        boolean isUserCreated = userService.addUser(user);

        Assert.assertFalse(isUserCreated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void updateProfile_ProfileHasBeenUpdated_ReturnTrue() {
        User user = new User();

        boolean isUserChanged = userService.updateProfile(user, "123", "123", "some@gmail.com");

        Assert.assertTrue(isUserChanged);
        Assert.assertEquals("123", user.getPassword());
        Assert.assertEquals("some@gmail.com", user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.eq("Activation code"),
                        ArgumentMatchers.contains("Welcome to Sweater.")
                );
    }

    @Test
    public void updateProfile_ProfileHaNotBeenUpdated_ReturnTrue() {
        User user = new User();
        user.setPassword("123");
        user.setEmail("some@gmail.com");

        boolean isUserChanged = userService.updateProfile(user, "", "", null);

        Assert.assertTrue(isUserChanged);
        Assert.assertEquals("123", user.getPassword());
        Assert.assertEquals("some@gmail.com", user.getEmail());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
        Mockito.verify(mailSender, Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser_ActivationCodeExists_ReturnTrue() {
        User user = new User();

        user.setActivationCode("bingo!");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNotNull(user.getActivationCode());

        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUser_ActivationCodeNotFound_ReturnFalse() {
        boolean isUserActivated = userService.activateUser("activate me");

        Assert.assertFalse(isUserActivated);

        Mockito.verify(userRepository, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }

    @Test
    public void loadUserByUsername_CorrectUser_ReturnUser() {
        User user = new User();
        user.setUsername("John");

        Mockito.doReturn(new User())
                .when(userRepository)
                .findByUsername("John");

        UserDetails userIsLoaded = userService.loadUserByUsername("John");
        Assert.assertEquals(user, userIsLoaded);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_Exception_ReturnException() {
        User user = new User();
        user.setUsername("John");

        UserDetails userIsLoaded = userService.loadUserByUsername("John");
        Assert.assertNull(userIsLoaded);
    }

    @Test
    public void sendMessage_CorrectMessageSent_ReturnTrue() {
        User user = new User();
        user.setEmail("dasha27atr@gmail.com");

        boolean isMessageSent = userService.sendMessage(user);
        Assert.assertTrue(isMessageSent);
    }

    @Test
    public void sendMessage_SendingDoesNotOccur_ReturnFalse() {
        User user = new User();

        boolean isMessageSent = userService.sendMessage(user);
        Assert.assertFalse(isMessageSent);
    }
}