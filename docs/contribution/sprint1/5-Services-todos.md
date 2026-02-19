# Todos: Phase 5 Service Layer

Purpose: this file contains the steps on how to setup the project for the first sprint.


## **5.1 User Service**

Matthew
### Create UserService Interface
- [ ] Create `UserService` interface in `user` package
- [ ] Define method signatures:
    - `User getUserByEmail(String email);`
    - `User getUserById(Long id);`
    - `User createUser(User user);` (for future registration)
    - `User updateUser(Long id, User user);`
    - `void deleteUser(Long id);` (optional)
    - `List<User> getAllUsers();` (for admin user management)
    - `List<User> getUsersByRole(Role role);` (optional)

Matthew
### Create UserServiceImpl Implementation
- [ ] Create `UserServiceImpl` class in `service.impl` package
- [ ] Add `@Service` annotation
- [ ] Implement `UserService` interface
- [ ] Inject `UserRepository` via constructor
- [ ] Inject `PasswordEncoder` via constructor (for createUser method)

Matthew
### Implement getUserByEmail Method
- [ ] Call `userRepository.findByEmail(email)`
- [ ] Return user if found
- [ ] Throw `UsernameNotFoundException` or custom exception if not found
- [ ] Add logging for user retrieval

Matthew
### Implement getUserById Method
- [ ] Call `userRepository.findById(id)`
- [ ] Return user if found
- [ ] Throw `ResourceNotFoundException` or custom exception if not found
- [ ] Add logging

Matthew
### Implement createUser Method
- [ ] Validate user input (email not null, password not null)
- [ ] Check if user already exists by email
- [ ] Throw exception if user already exists
- [ ] Encode password: `user.setPassword(passwordEncoder.encode(user.getPassword()))`
- [ ] Set createdAt timestamp: `user.setCreatedAt(LocalDateTime.now())`
- [ ] Save user: `userRepository.save(user)`
- [ ] Return saved user
- [ ] Add logging

Matthew
### Implement updateUser Method
- [ ] Fetch existing user by id
- [ ] Throw exception if user not found
- [ ] Update allowed fields (firstName, lastName, email - but not role or password here)
- [ ] Save updated user: `userRepository.save(existingUser)`
- [ ] Return updated user
- [ ] Add logging

Matthew
### Implement getAllUsers Method (for Admin)
- [ ] Call `userRepository.findAll()`
- [ ] Return list of all users
- [ ] Add logging

Matthew
### Implement getUsersByRole Method (Optional)
- [ ] Call `userRepository.findAllByRole(role)`
- [ ] Return filtered list
- [ ] Add logging

Matthew
### Add Validation and Error Handling
- [ ] Create custom exception classes if needed:
    - `UserNotFoundException`
    - `UserAlreadyExistsException`
    - `InvalidUserDataException`
- [ ] Add validation for email format
- [ ] Add validation for password strength (for createUser)

### Test UserService
- [ ] (Optional) Write unit tests for each method
- [ ] Test getUserByEmail with existing and non-existing users
- [ ] Test createUser with valid and invalid data
- [ ] Test updateUser functionality
- [ ] Verify exceptions are thrown correctly


---

