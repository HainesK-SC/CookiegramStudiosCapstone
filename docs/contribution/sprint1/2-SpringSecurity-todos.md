# Todos: Phase 2 Spring Security

Purpose: this file contains the steps on how to setup the project for the first sprint.

## **2.1 Security Dependencies**

Matthew
### Verify Dependencies in Build File
- [ ] Open `pom.xml` (Maven) or `build.gradle` (Gradle)
- [ ] Verify `spring-boot-starter-security` dependency is present
- [ ] Add if missing (Maven):
  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
  </dependency>
  ```
- [ ] Verify `thymeleaf-extras-springsecurity6` dependency is present (for Spring Boot 3.x)
- [ ] Add if missing (Maven):
  ```xml
  <dependency>
      <groupId>org.thymeleaf.extras</groupId>
      <artifactId>thymeleaf-extras-springsecurity6</artifactId>
  </dependency>
  ```
- [ ] Reload/refresh dependencies
- [ ] Verify no dependency conflicts in logs

---

## **2.2 UserDetailsService Implementation**

Matthew
### Create CustomUserDetailsService Class
- [ ] Create `CustomUserDetailsService` class in `security` or `service` package
- [ ] Add `@Service` annotation
- [ ] Implement `UserDetailsService` interface
- [ ] Inject `UserRepository` via constructor

Matthew
### Implement loadUserByUsername Method
- [ ] Override `loadUserByUsername(String username)` method
- [ ] Call `userRepository.findByEmail(username)` to fetch user
- [ ] Throw `UsernameNotFoundException` if user not found
- [ ] Return `org.springframework.security.core.userdetails.User` object
- [ ] Map user email to username
- [ ] Map user password to password
- [ ] Map user role to `GrantedAuthority` using `SimpleGrantedAuthority`
- [ ] Add "ROLE_" prefix to authority (e.g., "ROLE_ADMIN", "ROLE_EMPLOYEE")

### Test UserDetailsService
- [ ] Add logging to track when users are loaded
- [ ] Verify method compiles without errors
- [ ] (Optional) Write unit test for loadUserByUsername

---

## **2.3 Password Encoding**

Who?
### Configure PasswordEncoder Bean
- [ ] Create `SecurityConfig` class in `config` or `security` package (if not created yet)
- [ ] Add `@Configuration` annotation
- [ ] Create `@Bean` method for `PasswordEncoder`
- [ ] Return `new BCryptPasswordEncoder()` instance
- [ ] Verify bean is created on application startup (check logs)

Who?
### Update Seed Data to Use Encoder
- [ ] Go back to `DataSeeder` class from Phase 1
- [ ] Inject `PasswordEncoder` bean
- [ ] Update all password assignments to use `passwordEncoder.encode("plaintext")`
- [ ] Run application and verify users are created with encoded passwords
- [ ] Check database to confirm passwords are hashed (should start with `$2a$` or `$2b$`)

---

## **2.4 Security Configuration Class**

Who?
### Create SecurityConfig Class (if not already created)
- [ ] Create `SecurityConfig` class in `config` or `security` package
- [ ] Add `@Configuration` annotation
- [ ] Add `@EnableWebSecurity` annotation

Who?
### Create SecurityFilterChain Bean
- [ ] Create `@Bean` method returning `SecurityFilterChain`
- [ ] Inject `HttpSecurity` parameter
- [ ] Use method chaining to configure security

Who?
### Configure Public Endpoints
- [ ] Use `.authorizeHttpRequests()` to start authorization configuration
- [ ] Permit all access to: `"/", "/login", "/css/**", "/js/**", "/images/**", "/error"`
- [ ] Example: `.requestMatchers("/", "/login", "/css/**").permitAll()`
- [ ] Permit all access to H2 console if using H2: `"/h2-console/**"`

Who?
### Configure Role-Based Access
- [ ] Restrict `/employee/**` to EMPLOYEE role: `.requestMatchers("/employee/**").hasRole("EMPLOYEE")`
- [ ] Restrict `/admin/**` to ADMIN role: `.requestMatchers("/admin/**").hasRole("ADMIN")`
- [ ] Restrict `/customer/**` to CUSTOMER role (if implementing): `.requestMatchers("/customer/**").hasRole("CUSTOMER")`
- [ ] Add `.anyRequest().authenticated()` as fallback (or `.permitAll()` if most routes are public)

Who?
### Configure Form Login
- [ ] Add `.formLogin()` configuration
- [ ] Set custom login page: `.loginPage("/login")`
- [ ] Permit all access to login page: `.permitAll()`
- [ ] Set login processing URL (default `/login` is fine): `.loginProcessingUrl("/login")`
- [ ] Set default success URL or use custom success handler (see Phase 2.5)
- [ ] Set failure URL: `.failureUrl("/login?error=true")`

Who?
### Configure Logout
- [ ] Add `.logout()` configuration
- [ ] Set logout URL: `.logoutUrl("/logout")`
- [ ] Set logout success URL: `.logoutSuccessUrl("/")`
- [ ] Permit all access to logout: `.permitAll()`
- [ ] Invalidate session on logout: `.invalidateHttpSession(true)`
- [ ] Clear authentication on logout: `.clearAuthentication(true)`

Who?
### Additional Security Settings
- [ ] Disable CSRF for H2 console if using H2: `.csrf().ignoringRequestMatchers("/h2-console/**")`
- [ ] Allow frames for H2 console if using H2: `.headers().frameOptions().sameOrigin()`
- [ ] Build and return the `SecurityFilterChain`: `return http.build();`

### Test Security Configuration
- [ ] Run application and verify it starts without security errors
- [ ] Verify you're redirected to `/login` when accessing protected routes
- [ ] Verify public routes (/, /css, /js) are accessible without login

---

## 2.5 Seed Data

Matthew
### Decide on Seeding Approach
- [ ] Choose approach: CommandLineRunner (do this approach)
- [ ] Create seeding class (e.g., `DataSeeder`) if using programmatic approach
- [ ] Add `@Component` annotation to seeding class

Matthew
### Implement Data Seeding Logic
- [ ] Inject `UserRepository` into seeding class
- [ ] Inject `PasswordEncoder` bean into seeding class (for password encoding)
- [ ] Implement `run()` method (CommandLineRunner/ApplicationRunner)
- [ ] Add logic to check if users already exist (avoid duplicates on restart)

Matthew
### Create Customer User(s)
- [ ] Create customer user with:
    - Email: `customer@cookiegram.com` (or similar)
    - Encoded password: use `passwordEncoder.encode("password123")`
    - Role: `Role.CUSTOMER`
    - First name and last name
    - Created timestamp
- [ ] Save customer user to repository
- [ ] Log confirmation message

Matthew
### Create Employee User(s)
- [ ] Create employee user with:
    - Email: `employee@cookiegram.com` (or similar)
    - Encoded password: use `passwordEncoder.encode("password123")`
    - Role: `Role.EMPLOYEE`
    - First name and last name
    - Created timestamp
- [ ] Save employee user to repository
- [ ] Log confirmation message

Matthew
### Create Admin User(s)
- [ ] Create admin user with:
    - Email: `admin@cookiegram.com` (or similar)
    - Encoded password: use `passwordEncoder.encode("password123")`
    - Role: `Role.ADMIN`
    - First name and last name
    - Created timestamp
- [ ] Save admin user to repository
- [ ] Log confirmation message


## **2.6 Login Success Handler**

Matthew
### Create CustomAuthenticationSuccessHandler Class
- [ ] Create `CustomAuthenticationSuccessHandler` class in `security` package
- [ ] Add `@Component` annotation
- [ ] Implement `AuthenticationSuccessHandler` interface

Matthew
### Implement onAuthenticationSuccess Method
- [ ] Override `onAuthenticationSuccess()` method
- [ ] Accept parameters: `HttpServletRequest`, `HttpServletResponse`, `Authentication`
- [ ] Get user authorities from authentication object: `authentication.getAuthorities()`
- [ ] Initialize redirect URL variable

Matthew
### Add Role-Based Redirection Logic
- [ ] Check if user has ADMIN role:
    - If true, set redirect URL to `/admin/dashboard`
- [ ] Check if user has EMPLOYEE role:
    - If true, set redirect URL to `/employee/dashboard`
- [ ] Check if user has CUSTOMER role (if applicable):
    - If true, set redirect URL to `/customer/dashboard`
- [ ] Set default redirect URL to `/` if no role matches
- [ ] Use `response.sendRedirect(redirectUrl)` to redirect user

Matthew
### Integrate Success Handler with Security Config
- [ ] Go back to `SecurityConfig` class
- [ ] Inject `CustomAuthenticationSuccessHandler` via constructor
- [ ] In form login configuration, add: `.successHandler(customAuthenticationSuccessHandler)`
- [ ] Remove any conflicting `.defaultSuccessUrl()` configuration

### Test Success Handler
- [ ] Run application
- [ ] Login as admin user - verify redirect to `/admin/dashboard`
- [ ] Logout and login as employee - verify redirect to `/employee/dashboard`
- [ ] Logout and login as customer (if applicable) - verify redirect to `/customer/dashboard`
- [ ] Add logging to track redirections

