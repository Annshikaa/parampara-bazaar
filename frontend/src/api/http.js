import axios from "axios";

// Mock Fallback implementation for when backend is offline
const wait = (ms) => new Promise(res => setTimeout(res, ms));

const http = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

http.interceptors.response.use(
  (response) => response,
  async (error) => {
    // If backend isn't ready, mock the chatbot response
    if (error.config.url.includes("/chatbot") && !error.response) {
      console.warn("Backend offline, using mock chatbot response.");
      await wait(1000);
      return {
        data: {
          reply: `Let’s see what I can do for you... That's a bit low, but I appreciate the offer.`,
          accepted: false,
        }
      };
    }
    return Promise.reject(error);
  }
);

export default http;