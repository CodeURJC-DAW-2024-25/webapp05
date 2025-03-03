package es.codeurjc.daw.alphagym.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

    

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Acceso a los recursos estáticos
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/loginerror").permitAll()
                        .requestMatchers("/trainings").permitAll()
                        .requestMatchers("/nutritions").permitAll()
                        .requestMatchers("/trainings/*").permitAll()
                        .requestMatchers("/nutritions").permitAll()
						.requestMatchers("/index").permitAll()
						.requestMatchers("/trainingComments/*").permitAll()
						.requestMatchers("/nutritionComments/*").permitAll()

						// PRIVATE PAGES
                        .requestMatchers("/account").authenticated() //o ponerlo de la misma manera que el resto 
						.requestMatchers("/newNutrition").hasAnyRole("USER")
						.requestMatchers("/newTraining").hasAnyRole("USER")
						.requestMatchers("/editNutrition/*").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/editTraining/*").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/removeTraining/*").hasAnyRole("ADMIN")
                        .requestMatchers("/removeNutrition/*").hasAnyRole("ADMIN")
						.requestMatchers(("/trainingComments/**")).hasAnyRole("ADMIN", "USER")
                        )

				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/error")
						.defaultSuccessUrl("/account")
						.permitAll())
						
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());

        // Disable CSRF at the moment 
        //http.csrf(csrf -> csrf.disable());
		return http.build();
	}
}
