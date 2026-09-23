(function () {
    "use strict";

    function ensureContainer() {
        var el = document.getElementById("toast-container");
        if (!el) {
            el = document.createElement("div");
            el.id = "toast-container";
            el.className = "toast-container";
            el.setAttribute("aria-live", "polite");
            document.body.appendChild(el);
        }
        return el;
    }

    window.showToast = function (message, type) {
        var container = ensureContainer();
        var toast = document.createElement("div");
        toast.className = "toast toast-" + (type || "success");
        toast.textContent = message;
        container.appendChild(toast);
        setTimeout(function () {
            toast.remove();
        }, 4500);
    };
})();