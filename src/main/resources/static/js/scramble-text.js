(function () {
    "use strict";

    var DURATION = 750;
    var CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz#$@%&*+=?!";

    function prefersReducedMotion() {
        return window.matchMedia && window.matchMedia("(prefers-reduced-motion: reduce)").matches;
    }

    function randomChar() {
        return CHARS.charAt((Math.random() * CHARS.length) | 0);
    }

    function scramble(el) {
        var finalHTML = el.innerHTML;
        var finalText = el.textContent;

        var content = document.createElement("span");
        content.className = "scramble-content";
        while (el.firstChild) content.appendChild(el.firstChild);

        var overlay = document.createElement("span");
        overlay.className = "scramble-overlay";
        overlay.setAttribute("aria-hidden", "true");

        el.appendChild(content);
        el.appendChild(overlay);
        el.classList.add("is-scrambling");

        var start = performance.now();

        function frame(now) {
            var progress = Math.min((now - start) / DURATION, 1);
            var locked = Math.floor(progress * finalText.length);
            var out = "";
            for (var i = 0; i < finalText.length; i++) {
                var ch = finalText.charAt(i);
                out += (i < locked || ch === " ") ? ch : randomChar();
            }
            overlay.textContent = out;

            if (progress < 1) {
                requestAnimationFrame(frame);
            } else {
                el.innerHTML = finalHTML;
                el.classList.remove("is-scrambling");
            }
        }

        requestAnimationFrame(frame);
    }

    function init() {
        var el = document.querySelector("[data-scramble]");
        if (!el || prefersReducedMotion()) return;
        scramble(el);
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();