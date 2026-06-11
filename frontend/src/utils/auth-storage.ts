import type { AuthResponse } from "../types/auth";

export type StoredUser = {
  userId: string;
  email: string;
  firstName: string;
  lastName: string;
  role: string;
};

const TOKEN_KEY = "token";
const USER_KEY = "user";

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function saveAuthResponse(response: AuthResponse) {
  const user: StoredUser = {
    userId: response.userId,
    email: response.email,
    firstName: response.firstName,
    lastName: response.lastName,
    role: response.role,
  };

  localStorage.setItem(TOKEN_KEY, response.token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getStoredUser(): StoredUser | null {
  const storedUser = localStorage.getItem(USER_KEY);

  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser) as StoredUser;
  } catch {
    clearAuthStorage();
    return null;
  }
}

export function clearAuthStorage() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}

export function isAuthenticated() {
  return Boolean(getToken());
}