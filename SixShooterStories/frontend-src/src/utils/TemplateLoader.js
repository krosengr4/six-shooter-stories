import axios from 'axios';

class TemplateLoader {
    constructor() {
        this.templates = new Map();
        this.loadingPromises = new Map();
    }

    async loadTemplate(templateName) {
        // Return cached template if available
        if (this.templates.has(templateName)) {
            return this.templates.get(templateName);
        }

        // Return existing loading promise if template is being loaded
        if (this.loadingPromises.has(templateName)) {
            return this.loadingPromises.get(templateName);
        }

        // Create loading promise
        const loadingPromise = this.fetchTemplate(templateName);
        this.loadingPromises.set(templateName, loadingPromise);

        try {
            const template = await loadingPromise;
            this.templates.set(templateName, template);
            this.loadingPromises.delete(templateName);
            return template;
        } catch (error) {
            this.loadingPromises.delete(templateName);
            throw error;
        }
    }

    async fetchTemplate(templateName) {
        try {
            const response = await axios.get(`/templates/${templateName}.mustache`);
            return response.data;
        } catch (error) {
            console.error(`Error loading template ${templateName}:`, error);
            throw new Error(`Failed to load template: ${templateName}`);
        }
    }

    // Preload commonly used templates
    async preloadTemplates(templateNames = []) {
        const defaultTemplates = ['home', 'stories', 'profile', 'auth'];
        const templatesToLoad = [...defaultTemplates, ...templateNames];
        
        const loadPromises = templatesToLoad.map(name => 
            this.loadTemplate(name).catch(error => {
                console.warn(`Failed to preload template ${name}:`, error);
                return null;
            })
        );

        await Promise.allSettled(loadPromises);
    }

    // Clear cache (useful for development)
    clearCache() {
        this.templates.clear();
        this.loadingPromises.clear();
    }
}

export const templateLoader = new TemplateLoader();
