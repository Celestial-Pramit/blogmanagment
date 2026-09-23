(function () {
    "use strict";

    document.addEventListener("DOMContentLoaded", function () {
        var deleteBtn = document.getElementById("delete-btn");
        if (!deleteBtn) return;

        deleteBtn.addEventListener("click", function () {
            var confirmed = window.confirm("Delete this post? This can't be undone.");
            if (!confirmed) return;

            var postId = deleteBtn.dataset.postId;
            deleteBtn.disabled = true;

            fetch("/api/posts/" + postId, {
                method: "DELETE",
                headers: { "Accept": "application/json" }
            })
                .then(function (response) {
                    if (!response.ok && response.status !== 204) {
                        return response.json().catch(function () { return {}; }).then(function (body) {
                            throw new Error(body.message || "Couldn't delete this post.");
                        });
                    }
                    window.showToast("Post deleted.", "success");
                    setTimeout(function () { window.location.href = "/"; }, 600);
                })
                .catch(function (err) {
                    window.showToast(err.message, "error");
                    deleteBtn.disabled = false;
                });
        });
    });
})();