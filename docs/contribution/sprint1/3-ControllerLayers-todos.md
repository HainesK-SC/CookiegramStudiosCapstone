# Todos: Phase 3 Initial Controllers Setup

Purpose: this file contains the steps on how to setup the project for the first sprint.

## **3.1 Public Controller**

Matthew
### Create PublicController Class
- [ ] Create `PublicController` class in `controller.common` or `controller` package
- [ ] Add `@Controller` annotation
- [ ] (Optional) Add `@RequestMapping` for common path if needed

Matthew
### Create Home/Landing Page Endpoint
- [ ] Create method for root endpoint: `@GetMapping("/")`
- [ ] Method returns String: `"index"` (Thymeleaf template name)
- [ ] Add `Model` parameter to method
- [ ] Add mock/placeholder promotions data to model (for now)
- [ ] Example: `model.addAttribute("promotions", promotionsList)`

Matthew
### Create Login Page Endpoint
- [ ] Create method for login endpoint: `@GetMapping("/login")`
- [ ] Method returns String: `"login"` (Thymeleaf template name)
- [ ] Add optional `@RequestParam` for error parameter
- [ ] If error parameter exists, add error message to model
- [ ] Example: `model.addAttribute("error", "Invalid credentials")`

Matthew
### Create Order Confirmation Endpoint (Public - No Auth Required)
- [ ] Create method for order confirmation: `@GetMapping("/order/confirmation/{orderNumber}")`
- [ ] Method returns String: `"order-confirmation"` (Thymeleaf template name)
- [ ] Add `@PathVariable String orderNumber` parameter
- [ ] Add `Model` parameter to method
- [ ] Add orderNumber to model: `model.addAttribute("orderNumber", orderNumber)`
- [ ] Add placeholder order summary data to model (for now)
- [ ] No authentication required for this endpoint

### Test Public Controller
- [ ] Run application
- [ ] Access `http://localhost:8080/` - verify it doesn't throw errors (404 is ok for now, template not created yet)
- [ ] Access `http://localhost:8080/login` - verify it doesn't throw errors
- [ ] Access `http://localhost:8080/order/confirmation/TEST123` - verify it doesn't throw errors
- [ ] Verify no authentication is required for these endpoints

---

## **3.2 Employee Controller**

Who?
### Create EmployeeController Class
- [ ] Create `EmployeeController` class in `controller.employee` or `controller.user` package
- [ ] Add `@Controller` annotation
- [ ] Add `@RequestMapping("/employee")` for common path

Who?
### Secure Controller with Role-Based Access
- [ ] Add `@PreAuthorize("hasRole('EMPLOYEE')")` annotation (or rely on SecurityConfig)
- [ ] Verify SecurityConfig already restricts `/employee/**` to EMPLOYEE role

Who?
### Create Employee Dashboard Endpoint
- [ ] Create method for dashboard: `@GetMapping("/dashboard")`
- [ ] Method returns String: `"employee/employee-dashboard"` (template path)
- [ ] Add `Principal` or `Authentication` parameter to get logged-in user info
- [ ] Extract user email from principal
- [ ] Fetch user details from UserService (inject UserService)
- [ ] Add user info to model: `model.addAttribute("user", user)`

Who?
### Add Placeholder Methods for Future Features
- [ ] (Optional) Add commented-out methods for:
    - View orders: `@GetMapping("/orders")`
    - Update order status: `@PostMapping("/orders/{id}/status")`
    - View baking queue: `@GetMapping("/baking-queue")`
- [ ] Add TODO comments for implementation later

### Test Employee Controller
- [ ] Run application
- [ ] Try accessing `/employee/dashboard` without login - should redirect to login
- [ ] Try accessing `/employee/dashboard` as admin - should get 403 Forbidden
- [ ] Login as employee user - should redirect to employee dashboard
- [ ] Verify endpoint is accessible (404 for template is ok for now)

---

## **3.3 Admin Controller**

Matthew
### Create AdminController Class
- [ ] Create `AdminController` class in `controller.admin` or `controller.auth` package
- [ ] Add `@Controller` annotation
- [ ] Add `@RequestMapping("/admin")` for common path

Matthew
### Secure Controller with Role-Based Access
- [ ] Add `@PreAuthorize("hasRole('ADMIN')")` annotation (or rely on SecurityConfig)
- [ ] Verify SecurityConfig already restricts `/admin/**` to ADMIN role

Matthew
### Create Admin Dashboard Endpoint
- [ ] Create method for dashboard: `@GetMapping("/dashboard")`
- [ ] Method returns String: `"admin/admin-dashboard"` (template path)
- [ ] Add `Principal` or `Authentication` parameter to get logged-in user info
- [ ] Extract user email from principal
- [ ] Fetch user details from UserService (inject UserService)
- [ ] Add user info to model: `model.addAttribute("user", user)`

Matthew
### Add Placeholder Methods for Future Features
- [ ] (Optional) Add commented-out methods for:
    - User management: `@GetMapping("/users")`
    - View all orders: `@GetMapping("/orders")`
    - System reports: `@GetMapping("/reports")`
    - Manage promotions: `@GetMapping("/promotions")`
- [ ] Add TODO comments for implementation later

### Test Admin Controller
- [ ] Run application
- [ ] Try accessing `/admin/dashboard` without login - should redirect to login
- [ ] Try accessing `/admin/dashboard` as employee - should get 403 Forbidden
- [ ] Login as admin user - should redirect to admin dashboard
- [ ] Verify endpoint is accessible (404 for template is ok for now)

---

