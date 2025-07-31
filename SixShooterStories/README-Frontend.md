# Six Shooter Stories - Frontend Setup with Mustache & Axios

This frontend implementation uses **Mustache.js** for templating and **Axios** for HTTP requests, providing a clean separation between data and presentation.

## Features Implemented

✅ **Mustache.js Templating**
- Modular template system
- Template loader with caching
- Clean separation of logic and presentation

✅ **Axios HTTP Client**
- Configured API services
- Automatic authentication headers
- Error handling and interceptors

✅ **Modern UI Components**
- Responsive Western-themed design
- Authentication forms (Login/Register)
- Story management (List, Detail, CRUD operations)
- User profiles with tabs
- Comment system

## Quick Start

1. **Install dependencies:**
   ```bash
   cd frontend-src
   npm install
   ```

2. **Development mode** (with hot reload):
   ```bash
   npm start
   ```
   Opens at http://localhost:3000 with API proxy to Spring Boot backend

3. **Build for production:**
   ```bash
   npm run build
   ```
   Outputs to `src/main/resources/static`

4. **Integrated build** (Maven automatically builds frontend):
   ```bash
   mvn clean package
   ```

## Architecture

### Mustache Templates (`src/templates/`)
- `home.mustache` - Landing page hero section
- `stories.mustache` - Story listing with CRUD actions  
- `story-detail.mustache` - Full story view with comments
- `profile.mustache` - User profile with tabs
- `auth.mustache` - Login/Register forms

### Services (`src/services/`)
- `StoryService.js` - Story CRUD operations
- `AuthService.js` - Authentication & JWT handling

### Utilities (`src/utils/`)
- `TemplateLoader.js` - Async template loading with caching

## Template Usage Example

```javascript
// Load and render a template
const template = await templateLoader.loadTemplate('stories');
const data = {
    pageTitle: 'All Stories',
    stories: processedStories,
    loading: false
};
const html = Mustache.render(template, data);
document.getElementById('main-content').innerHTML = html;
```

## API Integration

The frontend is configured to work with your Spring Boot backend:

- **Development**: Webpack dev server proxies `/api/*` to `http://localhost:8080`
- **Production**: Static files served by Spring Boot, API calls direct

### Axios Configuration
```javascript
// Automatic JWT token injection
this.api.interceptors.request.use((config) => {
    const token = this.getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});
```

## Development Workflow

1. **Start Spring Boot backend** (`mvn spring-boot:run`)
2. **Start frontend dev server** (`npm start`)
3. **Develop with hot reload** - Changes reload automatically
4. **Templates update** - Modify `.mustache` files, refresh browser
5. **API integration** - Services call your Spring Boot controllers

## Production Deployment

The Maven build automatically:
1. Installs Node.js and npm
2. Runs `npm install`  
3. Runs `npm run build`
4. Copies files to `src/main/resources/static`
5. Spring Boot serves the frontend at the root URL

## Next Steps

1. **Connect to your APIs** - Update service methods to match your controller endpoints
2. **Customize templates** - Modify Mustache templates for your design
3. **Add features** - Story creation/editing forms, advanced search, etc.
4. **Authentication** - Connect to your JWT authentication system

The foundation is set up - now you can focus on building your specific features! 🤠
