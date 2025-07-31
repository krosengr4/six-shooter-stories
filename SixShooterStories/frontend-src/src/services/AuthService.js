import axios from 'axios';

export class AuthService {
    constructor() {
        this.apiUrl = '/api/auth';
        this.api = axios.create({
            baseURL: '',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        // Add token to requests if available
        this.api.interceptors.request.use((config) => {
            const token = this.getToken();
            if (token) {
                config.headers.Authorization = `Bearer ${token}`;
            }
            return config;
        });
    }

    async login(credentials) {
        try {
            const response = await this.api.post(`${this.apiUrl}/login`, credentials);
            if (response.data.token) {
                this.setToken(response.data.token);
                this.setUser(response.data.user);
            }
            return response.data;
        } catch (error) {
            console.error('Login error:', error);
            throw error;
        }
    }

    async register(userData) {
        try {
            const response = await this.api.post(`${this.apiUrl}/register`, userData);
            return response.data;
        } catch (error) {
            console.error('Registration error:', error);
            throw error;
        }
    }

    async logout() {
        try {
            await this.api.post(`${this.apiUrl}/logout`);
        } catch (error) {
            console.error('Logout error:', error);
        } finally {
            this.removeToken();
            this.removeUser();
        }
    }

    async getCurrentUser() {
        try {
            const response = await this.api.get(`${this.apiUrl}/me`);
            return response.data;
        } catch (error) {
            console.error('Error fetching current user:', error);
            throw error;
        }
    }

    setToken(token) {
        localStorage.setItem('authToken', token);
    }

    getToken() {
        return localStorage.getItem('authToken');
    }

    removeToken() {
        localStorage.removeItem('authToken');
    }

    setUser(user) {
        localStorage.setItem('currentUser', JSON.stringify(user));
    }

    getUser() {
        const user = localStorage.getItem('currentUser');
        return user ? JSON.parse(user) : null;
    }

    removeUser() {
        localStorage.removeItem('currentUser');
    }

    isAuthenticated() {
        const token = this.getToken();
        if (!token) return false;
        
        // Check if token is expired (basic check)
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp > Date.now() / 1000;
        } catch (error) {
            return false;
        }
    }
}
