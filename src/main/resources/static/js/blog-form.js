(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {
        var form = document.getElementById("blog-form");
        if (!form) return;

        var errorBox = document.getElementById("form-error");
        var submitBtn = document.getElementById("submit-btn");
        var mode = form.dataset.mode;
        var postId = form.dataset.postId;

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            errorBox.hidden = true;
            submitBtn.disabled = true;

            var tagsRaw = form.tags.value.trim();
            var tags = tagsRaw ? tagsRaw.split(",").map(function (t) { return t.trim(); }).filter(Boolean) : [];

            var payload = {
                title: form.title.value.trim(),
                excerpt: form.excerpt.value.trim(),
                content: form.content.value,
                coverImageUrl: form.coverImageUrl.value.trim() || null,
                tags: tags,
                visibility: form.querySelector('input[name="visibility"]:checked').value
            };

            var url = mode === "edit" ? "/api/posts/" + postId : "/api/posts";
            var method = mode === "edit" ? "PUT" : "POST";

            fetch(url, {
                method: method,
                headers: { "Content-Type": "application/json", "Accept": "application/json" },
                body: JSON.stringify(payload)
            })
                .then(function (response) {
                    if (!response.ok) {
                        return response.json().catch(function () { return {}; }).then(function (body) {
                            throw new Error(body.message || "Something went wrong.");
                        });
                    }
                    return response.json();
                })
                .then(function (post) {
                    window.location.href = "/blogs/" + post.slug;
                })
                .catch(function (err) {
                    errorBox.textContent = err.message;
                    errorBox.hidden = false;
                    submitBtn.disabled = false;
                });
        });
    });
})();