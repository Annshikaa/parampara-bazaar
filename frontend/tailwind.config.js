/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      fontFamily: {
        sans: ["Inter", "ui-sans-serif", "system-ui", "Segoe UI", "Roboto", "Arial"],
      },
      borderRadius: {
        xl: "14px",
        "2xl": "18px",
        "3xl": "26px",
        "4xl": "34px",
      },
      boxShadow: {
        soft: "0 18px 60px rgba(0,0,0,0.10)",
        lift: "0 22px 70px rgba(0,0,0,0.14)",
        glow: "0 0 0 7px rgba(99,102,241,0.16)",
      },
      keyframes: {
        floaty: {
          "0%, 100%": { transform: "translateY(0px)" },
          "50%": { transform: "translateY(-10px)" },
        },
        fadeUp: {
          "0%": { opacity: "0", transform: "translateY(10px)" },
          "100%": { opacity: "1", transform: "translateY(0px)" },
        },
        shimmer: {
          "0%": { transform: "translateX(-120%)" },
          "100%": { transform: "translateX(120%)" },
        },
      },
      animation: {
        floaty: "floaty 6s ease-in-out infinite",
        fadeUp: "fadeUp 500ms ease-out both",
        shimmer: "shimmer 1.6s ease-in-out infinite",
      },
    },
  },
  plugins: [],
};