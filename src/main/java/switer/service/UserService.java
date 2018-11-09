package switer.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import switer.domains.Role;
import switer.domains.User;
import switer.repos.UserRepository;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailsender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");

        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepository.save(user);

        System.out.println("\nStep 1 - Starting Adding User!\n");
        try {
            sendMessage(user);
        } catch (InterruptedException e) {
        }
        System.out.println("\nStep 3 - Finished Adding User!\n");
        return true;
    }

    /*
     * public void sendMessage(User user) throws InterruptedException { String
     * message = String.format( "Hello, %s \n" +
     * "Welcome to Switer. Please visit next link: http://localhost:8989/activate/%s"
     * , user.getUsername(), user.getActivationCode());
     * 
     * mailsender.send(user.getEmail(), "Activation code", message);
     * System.out.println("\nStep 4 - End sending email.\n"); }
     */

    @Async
    private void sendMessage(User user) throws InterruptedException {

        String message = String.format(
                "Hello, %s \n" + "Welcome to Switer. Please visit next link: http://localhost:8989/activate/%s",
                user.getUsername(), user.getActivationCode());

        System.out.println("\nStep 2 - Starting ending email.\n");
        CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {

            @Override
            public void run() {
                try {
                    // Simulate a long-running Job
                    // simulateSlowService();
                    mailsender.send(user.getEmail(), "Activation code", message);
                } catch (Exception e) { // throw new IllegalStateException(e);
                    System.out.println("\nERROR: did not sent email\n");
                }
            }
        });
        System.out.println("\nStep 4 - End sending email.\n");
    }

    private void simulateSlowService() {
        try {
            long time = 20000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean activateUser(String code) {

        User user = userRepository.findByActivationCode(code);

        if (user == null)
            return false;

        user.setActivationCode(null);
        userRepository.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {

        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);

    }

    public void updateProfile(User user, String password, String email) {

        String userEmail = user.getEmail();
        if (userEmail == null && StringUtils.isEmpty(userEmail)) {
            userEmail = "";
        }
        boolean mailChanged = (email != null && !email.contains(userEmail))
                || (userEmail != null && !userEmail.contains(email));

        if (mailChanged) {
            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());

            }
        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }
        userRepository.save(user);

        if (mailChanged) {
            try {
                sendMessage(user);
            } catch (Exception e) {
            }
        }

    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);

        userRepository.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);

        userRepository.save(user);
    }
}
