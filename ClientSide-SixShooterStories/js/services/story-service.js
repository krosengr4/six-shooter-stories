let storyService;

class StoryService {

    loadStories() {
        const url = `${config.baseUrl}/stories`;

        axios.get(url)
            .then(response => {
                console.log(response.data);
                let data = {};
                data.stories = response.data;

                templateBuilder.build('story', data, 'content', this.enableButtons);

            }) 
            .catch(error => {
                const data = {
                    error: "Failed to load stories."
                };

                templateBuilder.append("error", data, "errors")
             })
    }

    enableButtons() {
        const buttons = [...document.querySelectorAll(".add-buttons")];

        if(userService.isLoggedIn()) {
            buttons.forEach(button => {
                button.classList.remove("invisible")
            });
        } else {
            buttons.forEach(button => {
                button.classList.add("invisible")
            });
        }
    }

}

document.addEventListener('DOMContentLoaded', () => {
    storyService = new StoryService();
})