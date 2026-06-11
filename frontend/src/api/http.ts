import axios, { AxiosError } from "axios";
import { clearAuthStorage, getToken } from "../utils/auth-storage";

export const http = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? "/api",
  headers: {
    "Content-Type": "application/json",
  },
});

http.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

http.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    const status = error.response?.status;
    const currentPath = window.location.pathname;

    if (
      (status === 401 || status === 403) &&
      !currentPath.startsWith("/login") &&
      !currentPath.startsWith("/register")
    ) {
      clearAuthStorage();
      window.location.href = "/login";
    }

    return Promise.reject(error);
  }
);