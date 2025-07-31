import axios from 'axios';

export class StoryService {
    constructor() {
        this.apiUrl = '/api/stories';
        this.api = axios.create({
            baseURL: '',
            headers: {
                'Content-Type': 'application/json'
            }
        });
    }

    async getAllStories() {
        try {
            const response = await this.api.get(this.apiUrl);
            return response.data;
        } catch (error) {
            console.error('Error fetching stories:', error);
            // Return mock data for now
            return this.getMockStories();
        }
    }

    async getStoryById(id) {
        try {
            const response = await this.api.get(`${this.apiUrl}/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching story:', error);
            throw error;
        }
    }

    async createStory(story) {
        try {
            const response = await this.api.post(this.apiUrl, story);
            return response.data;
        } catch (error) {
            console.error('Error creating story:', error);
            throw error;
        }
    }

    async updateStory(id, story) {
        try {
            const response = await this.api.put(`${this.apiUrl}/${id}`, story);
            return response.data;
        } catch (error) {
            console.error('Error updating story:', error);
            throw error;
        }
    }

    async deleteStory(id) {
        try {
            await this.api.delete(`${this.apiUrl}/${id}`);
        } catch (error) {
            console.error('Error deleting story:', error);
            throw error;
        }
    }

    // Mock data for development
    getMockStories() {
        return [
            {
                id: 1,
                title: "The Midnight Train",
                content: "The old locomotive whistle echoed through the canyon as Sheriff Morrison rode alongside the tracks. He had been chasing the notorious Black Hat Gang for three days now, and their trail led directly to this abandoned railway line. The moon cast long shadows across the desert landscape, creating an eerie atmosphere that made even the most seasoned lawman feel uneasy.",
                author: "John Doe",
                datePosted: "2024-01-15",
                tags: ["western", "adventure", "sheriff"]
            },
            {
                id: 2,
                title: "Dust and Gold",
                content: "Sarah wiped the sweat from her brow as she emerged from the mine shaft. Three months of digging, and still no sign of the gold vein her father had written about in his journal. The other miners had given up weeks ago, but Sarah refused to abandon her father's dream. Little did she know that someone else was watching her every move from the ridge above.",
                author: "Jane Smith",
                datePosted: "2024-01-20",
                tags: ["mining", "family", "perseverance"]
            },
            {
                id: 3,
                title: "The Saloon Showdown",
                content: "The batwing doors of the Rusty Spur saloon swung open as three strangers entered. The pianist stopped playing, and conversations died down to whispers. Everyone in Tombstone knew that when strangers arrived unannounced, trouble usually followed. The bartender, Old Pete, slowly reached for the shotgun beneath the bar as the strangers approached.",
                author: "Bob Johnson",
                datePosted: "2024-01-25",
                tags: ["saloon", "showdown", "strangers"]
            }
        ];
    }
}
