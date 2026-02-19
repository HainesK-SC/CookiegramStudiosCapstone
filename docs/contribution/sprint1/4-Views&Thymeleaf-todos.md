# Todos: Phase 4 Views & Thymeleaf Templates

Purpose: this file contains the steps on how to setup the project for the first sprint.

## **4.1 Public Landing Page**

### Create index.html Template
- [ ] Create `index.html` in `src/main/resources/templates/` directory
- [ ] Add Thymeleaf namespace: `xmlns:th="http://www.thymeleaf.org"`
- [ ] Add basic HTML structure (DOCTYPE, html, head, body)
- [ ] Link CSS file: `<link th:href="@{/css/public.css}" rel="stylesheet">`
- [ ] Link JavaScript file: `<script th:src="@{/js/order-form.js}"></script>`

### Design Header/Navigation
- [ ] Create header section with logo/brand name
- [ ] Add "Login" button/link: `<a th:href="@{/login}">Login</a>`
- [ ] Style header to be visually appealing
- [ ] Add note near login: "Staff Login" or similar
- [ ] Make header sticky/fixed (optional)

### Create Promotions Section
- [ ] Add section with heading: "Current Promotions"
- [ ] Create Thymeleaf loop to display promotions: `th:each="promo : ${promotions}"`
- [ ] Display promotion title: `th:text="${promo.title}"`
- [ ] Display promotion description: `th:text="${promo.description}"`
- [ ] Display promotion discount: `th:text="${promo.discountPercentage}"`
- [ ] Style promotions in attractive card/banner layout
- [ ] Add conditional display: `th:if="${not #lists.isEmpty(promotions)}"`
- [ ] Add empty state message if no promotions

### Create Order Placement Section (Placeholder for Now)
- [ ] Add section heading: "Order Your CookieGrams"
- [ ] Add placeholder text: "Cookie ordering coming soon!"
- [ ] Add placeholder cookie images/cards
- [ ] Style section to be visually appealing
- [ ] (Full functionality will be added in Phase 8)

### Add Footer
- [ ] Create footer section
- [ ] Add copyright text
- [ ] Add contact information (optional)
- [ ] Add social media links (optional)
- [ ] Style footer

### Test Landing Page
- [ ] Run application
- [ ] Access `http://localhost:8080/`
- [ ] Verify page loads without errors
- [ ] Verify promotions display dynamically (even if mock data)
- [ ] Verify "Login" button is visible and clickable
- [ ] Test responsive design on different screen sizes

---

## **4.2 Login Page**

### Create login.html Template
- [ ] Create `login.html` in `src/main/resources/templates/` directory
- [ ] Add Thymeleaf namespace
- [ ] Add basic HTML structure
- [ ] Link CSS file: `<link th:href="@{/css/main.css}" rel="stylesheet">`

### Design Login Form
- [ ] Add heading: "Employee & Admin Login"
- [ ] Create form with `th:action="@{/login}"` and `method="post"`
- [ ] Add username/email input field with `name="username"`
- [ ] Add password input field with `name="password"`
- [ ] Add "Remember Me" checkbox (optional): `name="remember-me"`
- [ ] Add submit button: "Login"
- [ ] Style form to be centered and visually appealing

### Add Error Message Display
- [ ] Add conditional error div: `th:if="${param.error}"`
- [ ] Display error message: "Invalid username or password"
- [ ] Style error message in red or with alert styling
- [ ] Add conditional logout message: `th:if="${param.logout}"`
- [ ] Display logout message: "You have been logged out successfully"

### Add Customer Note
- [ ] Add note below form: "Customers: No login required. Return to home to place orders."
- [ ] Add link back to home: `<a th:href="@{/}">Return to Home</a>`
- [ ] Style note to be subtle but visible

### Test Login Page
- [ ] Run application
- [ ] Access `http://localhost:8080/login`
- [ ] Verify page loads without errors
- [ ] Try submitting with invalid credentials - verify error message appears
- [ ] Try submitting with valid credentials - verify redirect to appropriate dashboard
- [ ] Verify "Return to Home" link works

---

## **4.3 Order Confirmation Page (Public)**

### Create order-confirmation.html Template
- [ ] Create `order-confirmation.html` in `src/main/resources/templates/` directory
- [ ] Add Thymeleaf namespace
- [ ] Add basic HTML structure
- [ ] Link CSS file

### Design Confirmation Message
- [ ] Add success icon or checkmark graphic
- [ ] Add heading: "Order Confirmed!"
- [ ] Display order number prominently: `th:text="${orderNumber}"`
- [ ] Add thank you message

### Display Order Summary (Placeholder for Now)
- [ ] Add section: "Order Summary"
- [ ] Display placeholder items (will be dynamic in Phase 8)
- [ ] Display placeholder delivery information
- [ ] Display placeholder total price
- [ ] Display placeholder delivery date

### Add Action Buttons
- [ ] Add "Place Another Order" button linking to home: `th:href="@{/}"`
- [ ] (Optional) Add "Track Order" button for future functionality
- [ ] Style buttons prominently

### Test Confirmation Page
- [ ] (Will be fully testable after Phase 8)
- [ ] For now, manually navigate to page to verify layout
- [ ] Verify styling is consistent with landing page

---

## **4.4 Employee Dashboard (Minimal Layout)**

### Create employee-dashboard.html Template
- [ ] Create `templates/employee/` directory
- [ ] Create `employee-dashboard.html` in this directory
- [ ] Add Thymeleaf namespace: `xmlns:th="http://www.thymeleaf.org"`
- [ ] Add basic HTML structure
- [ ] Link CSS file: `<link th:href="@{/css/dashboard.css}" rel="stylesheet">`

### Create Dashboard Header
- [ ] Add heading: "Employee Dashboard"
- [ ] Add welcome message with user name: `th:text="'Welcome, ' + ${user.firstName}"`
- [ ] Add logout link: `<a th:href="@{/logout}">Logout</a>`
- [ ] Style header

### Create Placeholder Dashboard Content
- [ ] Add main heading: "Order Management Vision"
- [ ] Add 3-4 placeholder sections with headings only:
    - "Today's Orders Overview" (with brief description of what will be here)
    - "Orders in Baking Queue" (with brief description)
    - "Orders Ready for Shipping" (with brief description)
    - "Production Capacity" (with brief description)
- [ ] Add simple placeholder text or mock data cards for visual layout
- [ ] Style sections with basic cards/containers to show intended layout

### Test Employee Dashboard
- [ ] Run application
- [ ] Login as employee
- [ ] Verify redirect to employee dashboard
- [ ] Verify page loads without errors
- [ ] Verify welcome message displays user's name
- [ ] Verify logout link works
- [ ] Verify basic styling is clean and professional
- [ ] Try accessing as admin - should be forbidden (403)

---

## **4.5 Admin Dashboard (Minimal Layout)**

### Create admin-dashboard.html Template
- [ ] Create `templates/admin/` directory
- [ ] Create `admin-dashboard.html` in this directory
- [ ] Add Thymeleaf namespace: `xmlns:th="http://www.thymeleaf.org"`
- [ ] Add basic HTML structure
- [ ] Link CSS file: `<link th:href="@{/css/dashboard.css}" rel="stylesheet">`

### Create Dashboard Header
- [ ] Add heading: "Admin Dashboard"
- [ ] Add welcome message with user name: `th:text="'Welcome, ' + ${user.firstName}"`
- [ ] Add logout link: `<a th:href="@{/logout}">Logout</a>`
- [ ] Style header

### Create Placeholder Dashboard Content
- [ ] Add main heading: "System Management Vision"
- [ ] Add 4-5 placeholder sections with headings only:
    - "System Overview & Statistics" (with brief description)
    - "User Management" (with brief description)
    - "All Orders Overview" (with brief description)
    - "Reports & Analytics" (with brief description)
    - "System Configuration" (with brief description)
- [ ] Add simple placeholder text or mock data cards for visual layout
- [ ] Style sections with basic cards/containers to show intended layout

### Test Admin Dashboard
- [ ] Run application
- [ ] Login as admin
- [ ] Verify redirect to admin dashboard
- [ ] Verify page loads without errors
- [ ] Verify welcome message displays user's name
- [ ] Verify logout link works
- [ ] Verify basic styling is clean and professional
- [ ] Try accessing as employee - should be forbidden (403)

---

## **4.6 Shared Components**

### Create Error Pages

#### Create 403.html (Forbidden)
- [ ] Create `templates/error/403.html`
- [ ] Add heading: "403 - Access Forbidden"
- [ ] Add message: "You don't have permission to access this page"
- [ ] Add link to return home: `<a th:href="@{/}">Return to Home</a>`
- [ ] Style to match overall design

#### Create 404.html (Not Found)
- [ ] Create `templates/error/404.html`
- [ ] Add heading: "404 - Page Not Found"
- [ ] Add message: "The page you're looking for doesn't exist"
- [ ] Add link to return home: `<a th:href="@{/}">Return to Home</a>`
- [ ] Style to match overall design

#### Create error.html (Generic Error)
- [ ] Create `templates/error/error.html`
- [ ] Add heading: "An Error Occurred"
- [ ] Add generic error message
- [ ] Display error details if available: `th:text="${error}"`
- [ ] Add link to return home: `<a th:href="@{/}">Return to Home</a>`
- [ ] Style to match overall design

### Test Error Pages
- [ ] Try accessing non-existent URL - should show 404
- [ ] Try accessing forbidden page with wrong role - should show 403
- [ ] Verify error pages match overall site design

