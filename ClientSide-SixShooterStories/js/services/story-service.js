let storyService;

class StoryService {

    loadStories() {
        const url = `${config.baseUrl}/stories`;

        axios.get(url)
            .then(response => {
                console.log(response.data);
            }) 
            .catch(error => {
                const data = {
                    error: "Load profile failed."
                };

                templateBuilder.append("error", data, "errors")
             })
    }

}