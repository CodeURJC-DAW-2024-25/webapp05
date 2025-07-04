package es.codeurjc.daw.alphagym.security;

import es.codeurjc.daw.alphagym.security.jwt.JwtRequestFilter;
import es.codeurjc.daw.alphagym.security.jwt.UnauthorizedHandlerJwt;
import es.codeurjc.daw.alphagym.service.NutritionCommentService;
import es.codeurjc.daw.alphagym.service.NutritionService;
import es.codeurjc.daw.alphagym.service.TrainingCommentService;
import es.codeurjc.daw.alphagym.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Autowired
	TrainingService trainingService;

	@Autowired
	NutritionService nutritionService;

	@Autowired
	TrainingCommentService trainingCommentService;

	@Autowired
	NutritionCommentService nutritionCommentService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Expose AuthenticationManager as a Bean to be used in other services
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
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://localhost:8443"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.securityMatcher("/api/**")
				.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

		http
				.authorizeHttpRequests(authorize -> authorize

						// AUTH ENDPOINTS
						.requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh","/api/v1/auth/logout").permitAll()

						// PRIVATE USER ENDPOINTS
						.requestMatchers(HttpMethod.GET, "/api/users/all").hasAnyRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/users/*/image").hasRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/users/*").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/users/*/image").hasAnyRole("USER", "ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/users/*").hasAnyRole("ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/users/*/image").hasRole("ADMIN")
						.requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/trainingList").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/nutritionList").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/trainingList").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/nutritionList").authenticated()
						.requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
						.requestMatchers(HttpMethod.GET, "/api/users/reportedComments").hasRole("ADMIN")

						// PUBLIC ENDPOINTS
						.requestMatchers(HttpMethod.POST, "/api/users/new").permitAll()

						// For Training
						.requestMatchers(HttpMethod.POST, "/api/trainings/").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/trainings/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/trainings/*/image").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PATCH, "/api/trainings/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/trainings/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/trainings/subscribed/*")
	.access((authentication, context) ->
			authentication.get().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
			authentication.get().getAuthorities().stream()
				.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
				? new AuthorizationDecision(true)
				: new AuthorizationDecision(false)
	)
						.requestMatchers(HttpMethod.POST, "/api/trainings/unsubscribed/*")
	.access((authentication, context) ->
			authentication.get().getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
			authentication.get().getAuthorities().stream()
				.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
				? new AuthorizationDecision(true)
				: new AuthorizationDecision(false)
	)

						.requestMatchers(HttpMethod.GET, "/api/trainings/isSubscribed/*")
	.access((authentication, context) ->
			authentication.get().getAuthorities().stream()
					.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
					authentication.get().getAuthorities().stream()
							.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
					? new AuthorizationDecision(true)
					: new AuthorizationDecision(false)
	)

						// For Nutrition
						.requestMatchers(HttpMethod.POST, "/api/nutritions/").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/nutritions/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PUT, "/api/nutritions/*/image").hasAnyRole("USER")
						.requestMatchers(HttpMethod.PATCH, "/api/nutritions/*").hasAnyRole("USER")
						.requestMatchers(HttpMethod.DELETE, "/api/nutritions/**").hasRole("ADMIN")
						.requestMatchers(HttpMethod.POST, "/api/nutritions/subscribed/*")
    .access((authentication, context) ->
        authentication.get().getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
        authentication.get().getAuthorities().stream()
            .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
            ? new AuthorizationDecision(true)
            : new AuthorizationDecision(false)
    )
						.requestMatchers(HttpMethod.POST, "/api/nutritions/unsubscribed/*")
    .access((authentication, context) ->
        authentication.get().getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
        authentication.get().getAuthorities().stream()
            .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
            ? new AuthorizationDecision(true)
            : new AuthorizationDecision(false)
    )
						.requestMatchers(HttpMethod.GET, "/api/nutritions/isSubscribed/*")
						.access((authentication, context) ->
								authentication.get().getAuthorities().stream()
										.anyMatch(a -> a.getAuthority().equals("ROLE_USER")) &&
										authentication.get().getAuthorities().stream()
												.noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
										? new AuthorizationDecision(true)
										: new AuthorizationDecision(false)
						)


						// For TrainingComments
						.requestMatchers(HttpMethod.POST, "/api/trainingComments/").hasAnyRole("USER","ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/trainingComments/*").hasAnyRole("USER","ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/trainingComments/**").hasRole("ADMIN")

						// For NutritionComments
						.requestMatchers(HttpMethod.POST, "/api/nutritionComments/").hasAnyRole("USER","ADMIN")
						.requestMatchers(HttpMethod.PUT, "/api/nutritionComments/*").hasAnyRole("USER","ADMIN")
						.requestMatchers(HttpMethod.DELETE, "/api/nutritionComments/**").hasRole("ADMIN")

						// PUBLIC ENDPOINTS
						.anyRequest().permitAll());

		// Disable Form login Authentication
		http.formLogin(formLogin -> formLogin.disable());

		// Disable CSRF protection (it is difficult to implement in REST APIs)
		http.csrf(csrf -> csrf.disable());

		// Disable Basic Authentication
		http.httpBasic(httpBasic -> httpBasic.disable());

		// Stateless session
		http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/spa/**").permitAll()
						.requestMatchers("/new/**").permitAll()
						.requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // Acceso a los recursos
																						// estáticos
						.requestMatchers("/index").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/register").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/trainings").permitAll()
						.requestMatchers("/trainings/*").permitAll()
						.requestMatchers("/nutritions").permitAll()
						.requestMatchers("/nutritions/*").permitAll()
						.requestMatchers("/trainingComments/*").permitAll()
						.requestMatchers("/nutritionComments/*").permitAll()
						.requestMatchers("/trainingComments/*/moreComments").permitAll()
						.requestMatchers("/nutritionComments/*/moreComments").permitAll()
						.requestMatchers("/training/image/**").permitAll()
						.requestMatchers("/nutrition/image/**").permitAll()
						.requestMatchers("/v3/api-docs*/**").permitAll()
						.requestMatchers("/swagger-ui.html").permitAll()
						.requestMatchers("/swagger-ui/**").permitAll()

						// PRIVATE PAGES
						.requestMatchers("/account/**").hasAnyRole("USER")
						.requestMatchers("/editAccount/**").hasAnyRole("USER")
						.requestMatchers("/nutritions/newNutrition").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/trainings/createRoutine").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/trainings/delete/*").hasAnyRole("ADMIN")
						.requestMatchers("/nutritions/delete/*").hasAnyRole("ADMIN")
						.requestMatchers(("/trainingComments/*")).hasAnyRole("ADMIN", "USER")
						.requestMatchers(("/trainingComments/*/newComment")).hasAnyRole("ADMIN", "USER")
						.requestMatchers(("/nutritionComments/*/newComment")).hasAnyRole("ADMIN", "USER")
						.requestMatchers(("/nutritionComments/*")).hasAnyRole("ADMIN", "USER")
						.requestMatchers("/admin").hasRole("ADMIN")
						.requestMatchers("/user/new").hasRole("USER")
						.requestMatchers("/search").hasRole("ADMIN")
						.requestMatchers("/trainingComments/*/*/delete").hasAnyRole("ADMIN")
						.requestMatchers("/nutritionComments/*/*/delete").hasAnyRole("ADMIN")
						.requestMatchers("/trainingComments/*/*").hasAnyRole("ADMIN")
						.requestMatchers("/nutritionComments/*/*").hasAnyRole("ADMIN")
						.requestMatchers("/trainingComments/*/*/report").hasAnyRole("USER")
						.requestMatchers("/nutritionComments/*/*/report").hasAnyRole("USER")
						.requestMatchers("/trainings/subscribe/*").hasAnyRole("USER")
						.requestMatchers("/trainings/unsubscribe/*").hasAnyRole("USER")
						.requestMatchers("/trainings/deleteFromList/*").hasAnyRole("USER")
						.requestMatchers("/nutritions/subscribe/*").hasAnyRole("USER")
						.requestMatchers("/nutritions/unsubscribe/*").hasAnyRole("USER")
						.requestMatchers("/nutritions/deleteFromList/*").hasAnyRole("USER")
						// SECURITY FOR EDIT PAGES
						// EDIT TRAINING
						.requestMatchers(HttpMethod.GET, "/trainings/editTraining/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/trainings/editTraining/{trainingId}")
						.access((authentication, context) -> {
							Long trainingId = Long.valueOf(context.getVariables().get("trainingId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingService.isOwner(trainingId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						// EDIT NUTRITION
						.requestMatchers(HttpMethod.GET, "/nutritions/editNutrition/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/nutritions/editNutrition/{nutritionId}")
						.access((authentication, context) -> {
							Long nutritionId = Long.valueOf(context.getVariables().get("nutritionId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionService.isOwner(nutritionId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						// EDIT TRAINING COMMENT
						.requestMatchers(HttpMethod.GET, "/trainingComments/{trainingId}/{commentId}/editcomment")
						.access((authentication, context) -> {
							Long commentId = Long.valueOf(context.getVariables().get("commentId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingCommentService.isOwnerComment(commentId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/trainingComments/{trainingId}/{commentId}")
						.access((authentication, context) -> {
							Long commentId = Long.valueOf(context.getVariables().get("commentId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									trainingCommentService.isOwnerComment(commentId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						// EDIT NUTRITION COMMENT
						.requestMatchers(HttpMethod.GET, "/nutritionComments/{nutritionId}/{commentId}/editcomment")
						.access((authentication, context) -> {
							Long commentId = Long.valueOf(context.getVariables().get("commentId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionCommentService.isOwnerComment(commentId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})
						.requestMatchers(HttpMethod.POST, "/nutritionComments/{nutritionId}/{commentId}")
						.access((authentication, context) -> {
							Long commentId = Long.valueOf(context.getVariables().get("commentId"));
							return authentication.get().getAuthorities().stream()
									.anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
									||
									nutritionCommentService.isOwnerComment(commentId, authentication.get())
											? new AuthorizationDecision(true)
											: new AuthorizationDecision(false);
						})

				);

		http.formLogin(formLogin -> formLogin
				.loginPage("/login")
				.failureUrl("/login?error=true")
				.defaultSuccessUrl("/index")
				.permitAll())

				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());

		return http.build();
	}
}
