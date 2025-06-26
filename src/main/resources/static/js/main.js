// Main JavaScript file for Oromland application

// Check if user is authenticated
function isAuthenticated() {
    return localStorage.getItem('token') !== null;
}

// Redirect to login if not authenticated
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = '/auth/login-page';
    }
}

// Logout function
function logout() {
    localStorage.removeItem('token');
    window.location.href = '/';
}

// Add token to API requests
function getAuthHeader() {
    const token = localStorage.getItem('token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
}

// Make authenticated API request
async function apiRequest(url, method = 'GET', data = null) {
    const headers = {
        'Content-Type': 'application/json',
        ...getAuthHeader()
    };
    
    const options = {
        method,
        headers
    };
    
    if (data && (method === 'POST' || method === 'PUT')) {
        options.body = JSON.stringify(data);
    }
    
    try {
        const response = await fetch(url, options);
        
        if (response.status === 401) {
            // Unauthorized, clear token and redirect to login
            logout();
            return null;
        }
        
        return await response.json();
    } catch (error) {
        console.error('API request error:', error);
        return null;
    }
}

// Document ready function
document.addEventListener('DOMContentLoaded', function() {
    // Initialize any components or event listeners here
    
    // Example: Add event listener to logout buttons
    const logoutButtons = document.querySelectorAll('.logout-btn');
    logoutButtons.forEach(button => {
        button.addEventListener('click', logout);
    });
});