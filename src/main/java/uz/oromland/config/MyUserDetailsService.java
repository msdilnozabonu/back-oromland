package uz.oromland.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.oromland.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (UsernameNotFoundException e) {
            // This is an expected exception, so we can rethrow it
            throw e;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error loading user by username: " + e.getMessage());
            // Rethrow as UsernameNotFoundException
            throw new UsernameNotFoundException("Error loading user: " + e.getMessage());
        }
    }
}
