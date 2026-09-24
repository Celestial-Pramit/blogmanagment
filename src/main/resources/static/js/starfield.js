(function () {
    "use strict";

    var REDUCED_MOTION = "(prefers-reduced-motion: reduce)";

    function prefersReducedMotion() {
        return window.matchMedia && window.matchMedia(REDUCED_MOTION).matches;
    }

    function init() {
        if (prefersReducedMotion()) return;
        var container = document.getElementById("hero-particles");
        if (!container || typeof window.tsParticles === "undefined") return;

        window.tsParticles.load("hero-particles", {
            fullScreen: { enable: false },
            fpsLimit: 30,
            detectRetina: true,
            particles: {
                number: { value: 70 },
                color: { value: ["#ffffff", "#d6d7dc", "#9a9ba3"] },
                shape: { type: "circle" },
                opacity: {
                    value: 0.45,
                    random: { enable: true, minimumValue: 0.1 }
                },
                size: {
                    value: { min: 0.6, max: 1.5 },
                    random: true
                },
                move: {
                    enable: true,
                    speed: 0.25,
                    direction: "top",
                    random: true,
                    straight: false,
                    outModes: { default: "out" }
                },
                links: { enable: false },
                collision: { enable: false }
            },
            interactivity: {
                events: {
                    onHover: { enable: false },
                    onClick: { enable: false }
                }
            }
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", init);
    } else {
        init();
    }
})();