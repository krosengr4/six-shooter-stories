let commentService;

class CommentService {
    
    loadComments() {
        const url = `${config.baseUrl}/comments/story/{storyId}`;

        axios.get(url)
        .then(response => {
            console.log(response);
        })
        .catch(err => {
            console.log(err);

            const data = {
                error: "Failed to load comments."
            };

            templateBuilder.append("error", data, "errors");
        })
    }

    addComment(storyId) {
        const url = `${config.baseUrl}/comments/${storyId}`;
        const comment = {
            content: content
        }

        axios.post(url, comment)
        .then(response => {
            console.log(response.data);


        })
        .catch(err => {
            console.log(err);

            const data = {
                error: "Failed to post comment."
            };

            templateBuilder.append("error", data, "errors");
        })
    }
}