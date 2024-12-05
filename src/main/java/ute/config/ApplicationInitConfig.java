package ute.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ute.entity.Account;
import ute.entity.Cart;
import ute.repository.AccountRepository;

import java.util.HashSet;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    // tao tai khoan admin mac dinh
    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository) {
        return args -> {
            if( accountRepository.findByUsername("admin").isEmpty()){
                var role = new HashSet<String>();
                role.add("ADMIN");
                ;
                Account account = Account.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .name("admin")
                        .is_active(true)
                        .is_admin(true)
                        .is_deleted(false)
                        .roles(role)
                        .build();
                Cart cart = new Cart();
                cart.setAccount(account);
                account.setCarts(cart);
                accountRepository.save(account);
                log.warn("Create admin account with username: admin and password: admin123");
            }
        };

    }
}
