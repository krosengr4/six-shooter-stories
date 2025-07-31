import './styles/main.css';
import Mustache from 'mustache';
import { StoryService } from './services/StoryService';
import { AuthService } from './services/AuthService';
import { templateLoader } from './utils/TemplateLoader';

class App {
    constructor() {
        this.storyService = new StoryService();
        this.authService = new AuthService();
        this.currentUser = this.authService.getUser();
        this.init();
    }

    async init() {
        // Preload templates
        await templateLoader.preloadTemplates();
        
        this.setupEventListeners();
        this.updateNavigation();
        this.loadHomePage();
    }

    setupEventListeners() {
        // Navigation
        document.querySelectorAll('nav a').forEach(link => {
            link.addEventListener('click', (e) => {
                e.preventDefault();
                const page = e.target.getAttribute('href').substring(1);
                this.navigateTo(page);
            });
        });

        // Listen for authentication changes
        window.addEventListener('authChanged', () => {
            this.currentUser = this.authService.getUser();
            this.updateNavigation();
        });
    }

    updateNavigation() {
        const nav = document.querySelector('nav ul');
        
        if (this.currentUser) {
            nav.innerHTML = `
                <li><a href="#home">Home</a></li>
                <li><a href="#stories">Stories</a></li>
                <li><a href="#profile">Profile</a></li>
                <li><a href="#" id="logout-btn">Logout</a></li>
            `;
            
            document.getElementById('logout-btn').addEventListener('click', async (e) => {
                e.preventDefault();
                await this.authService.logout();
                window.dispatchEvent(new Event('authChanged'));
                this.navigateTo('home');
            });
        } else {
            nav.innerHTML = `
                <li><a href="#home">Home</a></li>
                <li><a href="#stories">Stories</a></li>
                <li><a href="#auth">Login</a></li>
            `;
        }
    }

    navigateTo(page, params = {}) {
        const mainContent = document.getElementById('main-content');
        
        // Remove active class from nav items
        document.querySelectorAll('nav a').forEach(link => {
            link.classList.remove('active');
        });
        
        // Add active class to current nav item
        const currentNavItem = document.querySelector(`nav a[href="#${page}"]`);
        if (currentNavItem) {
            currentNavItem.classList.add('active');
        }
        
        switch(page) {
            case 'home':
                this.loadHomePage();
                break;
            case 'stories':
                this.loadStoriesPage();
                break;
            case 'story':
                this.loadStoryDetail(params.id);
                break;
            case 'profile':
                this.loadProfilePage(params.username);
                break;
            case 'auth':
                this.loadAuthPage();
                break;
            default:
                this.loadHomePage();
        }
    }

    async loadHomePage() {
        try {
            const template = await templateLoader.loadTemplate('home');
            const data = {
                title: 'Welcome to Six Shooter Stories',
                subtitle: 'Share your wild west adventures with the community',
                buttonText: this.currentUser ? 'Explore Stories' : 'Join the Adventure'
            };
            
            const html = Mustache.render(template, data);
            document.getElementById('main-content').innerHTML = html;

            document.getElementById('get-started').addEventListener('click', () => {
                if (this.currentUser) {
                    this.navigateTo('stories');
                } else {
                    this.navigateTo('auth');
                }
            });
        } catch (error) {
            console.error('Error loading home page:', error);
            this.showError('Error loading home page');
        }
    }

    async loadStoriesPage() {
        try {
            const template = await templateLoader.loadTemplate('stories');
            
            // Show loading state
            const loadingData = {
                pageTitle: 'All Stories',
                loading: true,
                loadingMessage: 'Loading stories...'
            };
            
            let html = Mustache.render(template, loadingData);
            document.getElementById('main-content').innerHTML = html;
            
            // Load stories
            const stories = await this.storyService.getAllStories();
            const processedStories = stories.map(story => ({
                ...story,
                excerpt: story.content.substring(0, 200) + (story.content.length > 200 ? '...' : ''),
                formattedDate: new Date(story.datePosted).toLocaleDateString(),
                canEdit: this.currentUser && this.currentUser.username === story.author
            }));

            const data = {
                pageTitle: 'All Stories',
                stories: processedStories.length > 0 ? processedStories : null,
                loading: false
            };
            
            html = Mustache.render(template, data);
            document.getElementById('main-content').innerHTML = html;
            
            this.setupStoryListeners();
            
        } catch (error) {
            console.error('Error loading stories:', error);
            const template = await templateLoader.loadTemplate('stories');
            const errorData = {
                pageTitle: 'All Stories',
                error: true,
                errorMessage: 'Error loading stories. Please try again later.'
            };
            const html = Mustache.render(template, errorData);
            document.getElementById('main-content').innerHTML = html;
        }
    }

    setupStoryListeners() {
        // Read more buttons
        document.querySelectorAll('.read-more-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const storyId = e.target.dataset.storyId;
                this.navigateTo('story', { id: storyId });
            });
        });

        // Edit buttons
        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const storyId = e.target.dataset.storyId;
                this.editStory(storyId);
            });
        });

        // Delete buttons
        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const storyId = e.target.dataset.storyId;
                this.deleteStory(storyId);
            });
        });

        // Add story button
        const addStoryBtn = document.getElementById('add-story-btn');
        if (addStoryBtn) {
            addStoryBtn.addEventListener('click', () => {
                if (this.currentUser) {
                    this.showCreateStoryForm();
                } else {
                    this.navigateTo('auth');
                }
            });
        }
    }

    async loadStoryDetail(storyId) {
        try {
            const template = await templateLoader.loadTemplate('story-detail');
            const story = await this.storyService.getStoryById(storyId);
            
            const data = {
                ...story,
                formattedDate: new Date(story.datePosted).toLocaleDateString(),
                canEdit: this.currentUser && this.currentUser.username === story.author,
                canComment: !!this.currentUser,
                comments: story.comments || [],
                commentCount: story.comments ? story.comments.length : 0
            };
            
            const html = Mustache.render(template, data);
            document.getElementById('main-content').innerHTML = html;
            
            // Setup story detail listeners
            document.getElementById('back-to-stories').addEventListener('click', () => {
                this.navigateTo('stories');
            });
            
        } catch (error) {
            console.error('Error loading story detail:', error);
            this.showError('Story not found');
        }
    }

    async loadProfilePage(username = null) {
        try {
            const template = await templateLoader.loadTemplate('profile');
            
            // If no username provided and user is logged in, show their profile
            if (!username && this.currentUser) {
                username = this.currentUser.username;
            }
            
            if (!username) {
                this.navigateTo('auth');
                return;
            }
            
            // For now, use mock data - you'd normally fetch from API
            const userData = this.getMockUserProfile(username);
            
            const data = {
                user: userData,
                isOwnProfile: this.currentUser && this.currentUser.username === username
            };
            
            const html = Mustache.render(template, data);
            document.getElementById('main-content').innerHTML = html;
            
            this.setupProfileListeners();
            
        } catch (error) {
            console.error('Error loading profile:', error);
            this.showError('Profile not found');
        }
    }

    async loadAuthPage() {
        if (this.currentUser) {
            this.navigateTo('profile');
            return;
        }
        
        try {
            const template = await templateLoader.loadTemplate('auth');
            const html = Mustache.render(template, {});
            document.getElementById('main-content').innerHTML = html;
            
            this.setupAuthListeners();
            
        } catch (error) {
            console.error('Error loading auth page:', error);
            this.showError('Error loading authentication page');
        }
    }

    setupProfileListeners() {
        // Tab switching
        document.querySelectorAll('.tab-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const tabName = e.target.dataset.tab;
                this.switchProfileTab(tabName);
            });
        });
    }

    switchProfileTab(tabName) {
        // Remove active class from all tabs and content
        document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(content => content.classList.add('hidden'));
        
        // Add active class to clicked tab
        document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
        document.getElementById(`${tabName}-tab`).classList.remove('hidden');
    }

    setupAuthListeners() {
        // Tab switching
        document.querySelectorAll('.auth-tab').forEach(tab => {
            tab.addEventListener('click', (e) => {
                const tabName = e.target.dataset.tab;
                this.switchAuthTab(tabName);
            });
        });

        // Form submissions
        document.getElementById('login-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleLogin(e.target);
        });

        document.getElementById('register-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.handleRegister(e.target);
        });
    }

    switchAuthTab(tabName) {
        document.querySelectorAll('.auth-tab').forEach(tab => tab.classList.remove('active'));
        document.querySelectorAll('.auth-form').forEach(form => form.classList.add('hidden'));
        
        document.querySelector(`[data-tab="${tabName}"]`).classList.add('active');
        document.getElementById(`${tabName}-form`).classList.remove('hidden');
    }

    async handleLogin(form) {
        try {
            const formData = new FormData(form);
            const credentials = {
                username: formData.get('username'),
                password: formData.get('password')
            };
            
            await this.authService.login(credentials);
            window.dispatchEvent(new Event('authChanged'));
            this.navigateTo('profile');
            
        } catch (error) {
            document.getElementById('login-error').textContent = 'Invalid username or password';
        }
    }

    async handleRegister(form) {
        try {
            const formData = new FormData(form);
            const userData = {
                username: formData.get('username'),
                email: formData.get('email'),
                password: formData.get('password'),
                confirmPassword: formData.get('confirmPassword')
            };
            
            if (userData.password !== userData.confirmPassword) {
                document.getElementById('register-error').textContent = 'Passwords do not match';
                return;
            }
            
            await this.authService.register(userData);
            this.switchAuthTab('login');
            document.querySelector('#login-form input[name="username"]').value = userData.username;
            
        } catch (error) {
            document.getElementById('register-error').textContent = 'Registration failed. Please try again.';
        }
    }

    getMockUserProfile(username) {
        return {
            username: username,
            displayName: username.charAt(0).toUpperCase() + username.slice(1),
            initials: username.charAt(0).toUpperCase(),
            bio: 'A storyteller from the wild west',
            storyCount: 5,
            commentCount: 12,
            joinDate: 'January 2024',
            userStories: [
                { id: 1, title: 'My First Adventure', excerpt: 'It was a dark and stormy night...', formattedDate: '2024-01-15' }
            ],
            userComments: [
                { content: 'Great story!', storyTitle: 'The Midnight Train', storyId: 1, formattedDate: '2024-01-20' }
            ]
        };
    }

    showError(message) {
        document.getElementById('main-content').innerHTML = `
            <div class="error-page">
                <h2>Oops!</h2>
                <p>${message}</p>
                <button onclick="history.back()">Go Back</button>
            </div>
        `;
    }

    // Placeholder methods for future implementation
    async editStory(storyId) {
        console.log('Edit story:', storyId);
        // TODO: Implement story editing
    }

    async deleteStory(storyId) {
        if (confirm('Are you sure you want to delete this story?')) {
            console.log('Delete story:', storyId);
            // TODO: Implement story deletion
        }
    }

    showCreateStoryForm() {
        console.log('Show create story form');
        // TODO: Implement story creation form
    }
}

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new App();
});
